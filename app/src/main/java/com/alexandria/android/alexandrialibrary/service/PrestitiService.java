package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.helper.HTTPClients;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.helper.Utils;
import com.alexandria.android.alexandrialibrary.model.Prestito;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class PrestitiService extends MainService {
    private Context context;
    private String urlRequestPrestiti;
    private DefaultHttpClient client;
    private boolean enableStub;
    private SessionManager session;

    public PrestitiService(Context context) {
        super(context);
        this.context = context;
        this.client = HTTPClients.getDefaultHttpClient();
        this.session = new SessionManager(context);

        String baseUrl = context.getString(R.string.upstream_base_url);
        String path = context.getString(R.string.upstream_prestiti_path);
        this.urlRequestPrestiti = baseUrl + path;
    }

    public List<Prestito> getPrestiti() {
        if (session.isEnableStub()) {
            return stub();
        }

        try {
            // setup request
            HttpGet get = new HttpGet(urlRequestPrestiti);

            HttpResponse response = client.execute(get);

            // read response
            InputStream in = new BufferedInputStream(response.getEntity().getContent());

            String json = Utils.readStream(in);
            if (!StringUtils.isEmpty(json)) {
                return getPrestitiFromJson(json);
            }

        } catch (MalformedURLException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }

        return new ArrayList<>();
    }

    private List<Prestito> stub() {
        String json = getRawResource(R.raw.prestiti);
        return getPrestitiFromJson(json);
    }

    private List<Prestito> getPrestitiFromJson(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
        return gson.fromJson(json, new TypeToken<List<Prestito>>() {
        }.getType());
    }
}
