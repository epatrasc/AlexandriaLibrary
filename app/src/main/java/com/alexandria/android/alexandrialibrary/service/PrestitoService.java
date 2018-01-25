package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.helper.HTTPClients;
import com.alexandria.android.alexandrialibrary.helper.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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

    public boolean presta(int idLibro, int idUtente) {
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
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.optString("done").equals("true") ? true : false;
            }

        } catch (MalformedURLException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        } catch (JSONException e) {
            return false;
        }

        return false;
    }

    public boolean restituisci(int idLibro) {
        // TODO implement request to the upstream
        return true;
    }

}
