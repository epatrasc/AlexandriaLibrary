package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.helper.HTTPClients;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.helper.Utils;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CatalogoLibriService extends MainService{
    private final Context context;
    private String urlCatalogo;
    private DefaultHttpClient client;
    private boolean enableStub;
    private SessionManager session;

    public CatalogoLibriService(Context context) {
        super(context);
        this.context = context;
        this.client = HTTPClients.getDefaultHttpClient();
        this.session = new SessionManager(context);

        String baseUrl = context.getString(R.string.upstream_base_url);
        this.urlCatalogo = baseUrl + context.getString(R.string.upstream_catalogo_visualizza_path);
    }

    List<LibroAction> getLibri(){
        if (session.isEnableStub()) {
            return stub();
        }

        try {
            // setup request
            DefaultHttpClient client = HTTPClients.getDefaultHttpClient();
            HttpGet get = new HttpGet(urlCatalogo);
            HttpResponse response = client.execute(get);

            // read response
            InputStream in = new BufferedInputStream(response.getEntity().getContent());

            String json = Utils.readStream(in);
            if (!StringUtils.isEmpty(json)) {
                return getCatalogoFromJson(json);
            }

        } catch (MalformedURLException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }

        return new ArrayList<>();
    }

    private List<LibroAction> getCatalogoFromJson(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
        return gson.fromJson(json, new TypeToken<List<LibroAction>>() {
        }.getType());
    }

    private List<Libro> stub() {
        String json = null;
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(R.raw.libri);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            return   new Gson().fromJson(json,  new TypeToken<List<Libro>>(){}.getType());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }
}
