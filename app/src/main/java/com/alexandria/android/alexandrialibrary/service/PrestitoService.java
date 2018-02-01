package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;
import android.util.Log;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.helper.HTTPClients;
import com.alexandria.android.alexandrialibrary.helper.Utils;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.google.gson.Gson;

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
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class PrestitoService {
    private Context context;
    private String urlPresta;
    private String urlRestituisci;


    public PrestitoService(Context context) {
        this.context = context;
        String baseUrl = context.getString(R.string.upstream_base_url);
        this.urlPresta = baseUrl + context.getString(R.string.upstream_prestito_presta_path);
        this.urlRestituisci = baseUrl + context.getString(R.string.upstream_prestito_restituisci_path);

    }

    public LibroAction presta(int idLibro, int idUtente) {
        try {
            // setup request
            DefaultHttpClient client = HTTPClients.getDefaultHttpClient();
            HttpPost post = new HttpPost(urlPresta);

            HttpResponse response = client.execute(post);
            //add request post body
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("idLibro", Integer.toString(idLibro)));
            postParams.add(new BasicNameValuePair("idUtente", Integer.toString(idUtente)));
            postParams.add(new BasicNameValuePair("isAndroid", "true"));
            post.setEntity(new UrlEncodedFormEntity(postParams));

            // read response
            InputStream in = new BufferedInputStream(response.getEntity().getContent());

            String json = Utils.readStream(in);
            if (!StringUtils.isEmpty(json)) {
                return new Gson().fromJson(json, LibroAction.class);
            }

        } catch (MalformedURLException  ex) {
            Log.d("prestio-service", ex.getMessage());
        } catch (IOException  ex) {
            Log.d("prestio-service", ex.getMessage());
        }

        return null;
    }

    public LibroAction restituisci(int idLibro) {
        try {
            // setup request
            DefaultHttpClient client = HTTPClients.getDefaultHttpClient();
            HttpPost post = new HttpPost(urlRestituisci);

            HttpResponse response = client.execute(post);
            //add request post body
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("idLibro", Integer.toString(idLibro)));
            postParams.add(new BasicNameValuePair("isAndroid", "true"));
            post.setEntity(new UrlEncodedFormEntity(postParams));

            // read response
            InputStream in = new BufferedInputStream(response.getEntity().getContent());

            String json = Utils.readStream(in);
            if (!StringUtils.isEmpty(json)) {
                return new Gson().fromJson(json, LibroAction.class);
            }

        } catch (MalformedURLException  ex) {
            Log.d("prestio-service", ex.getMessage());
        } catch (IOException  ex) {
            Log.d("prestio-service", ex.getMessage());
        }

        return null;
    }

}
