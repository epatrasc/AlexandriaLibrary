package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.helper.HTTPClients;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.helper.Utils;
import com.alexandria.android.alexandrialibrary.model.Utente;
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

public class UtentiService extends MainService {
    private final Context context;
    private String url;
    private DefaultHttpClient client;
    private boolean enableStub;
    private SessionManager session;

    public UtentiService(Context context) {
        super(context);
        this.context = context;
        this.client = HTTPClients.getDefaultHttpClient();
        this.session = new SessionManager(context);

        String baseUrl = context.getString(R.string.upstream_base_url);
        this.url = baseUrl + context.getString(R.string.upstream_utenti_cliente);
    }

    public List<Utente> getAllUsers() {
        if (session.isEnableStub()) {
            return stub();
        }

        try {
            // setup request
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);

            // read response
            InputStream in = new BufferedInputStream(response.getEntity().getContent());

            String json = Utils.readStream(in);
            if (!StringUtils.isEmpty(json)) {
                return getUtentiFromJson(json);
            }

        } catch (MalformedURLException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }

        return new ArrayList<>();
    }

    private List<Utente> getUtentiFromJson(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
        return gson.fromJson(json, new TypeToken<List<Utente>>() {
        }.getType());
    }

    private List<Utente> stub() {
        String json = getRawResource(R.raw.utenti);
        return getUtentiFromJson(json);
    }

}
