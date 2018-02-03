package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;
import android.util.Log;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.helper.HTTPClients;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.helper.Utils;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LibroActionService extends MainService {
    private Context context;
    private String url;
    private DefaultHttpClient client;
    private boolean enableStub;
    private SessionManager session;

    public LibroActionService(Context context) {
        super(context);
        this.context = context;
        this.client = HTTPClients.getDefaultHttpClient();
        this.session = new SessionManager(context);

        String baseUrl = context.getString(R.string.upstream_base_url);
        this.url = baseUrl + context.getString(R.string.upstream_prestito_presta_path);

    }

    public LibroAction get(int idLibro) {
        if (session.isEnableStub()) {
            return stub();
        }

        try {
            // setup request
            HttpPost post = new HttpPost(url);

            //add request post body
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("idLibro", Integer.toString(idLibro)));
            postParams.add(new BasicNameValuePair("isAndroid", "true"));
            post.setEntity(new UrlEncodedFormEntity(postParams));

            // read response
            HttpResponse response = client.execute(post);
            InputStream in = new BufferedInputStream(response.getEntity().getContent());

            String json = Utils.readStream(in);
            if (!StringUtils.isEmpty(json)) {
                return new Gson().fromJson(json, LibroAction.class);
            }

        } catch (MalformedURLException ex) {
            Log.d("prestio-service", ex.getMessage());
        } catch (IOException ex) {
            Log.d("prestio-service", ex.getMessage());
        }

        return null;
    }

    private LibroAction stub() {
        String json = getRawResource(R.raw.libro_action);
        return getLibroActionFromJson(json);
    }

    private LibroAction getLibroActionFromJson(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
        return gson.fromJson(json, LibroAction.class);
    }
}
