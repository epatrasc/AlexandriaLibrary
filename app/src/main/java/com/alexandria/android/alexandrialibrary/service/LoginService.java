package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.helper.HTTPClients;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.helper.Utils;

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

public class LoginService extends MainService {
    private final Context context;
    private String loginUrl;
    private DefaultHttpClient client;
    private boolean enableStub;
    private SessionManager session;

    public LoginService(Context context) {
        super(context);
        this.context = context;
        this.client = HTTPClients.getDefaultHttpClient();
        this.session = new SessionManager(context);

        String baseUrl = context.getString(R.string.upstream_base_url);
        this.loginUrl = baseUrl + context.getString(R.string.upstream_login_path);
    }

    public String getUtente(String utente, String password) {
        if (session.isEnableStub()) {
            return stub();
        }

        try {
            // setup request
            HttpPost post = new HttpPost(loginUrl);

            //add request post body
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("nome", utente));
            postParams.add(new BasicNameValuePair("password", password));
            postParams.add(new BasicNameValuePair("isAndroid", "true"));
            post.setEntity(new UrlEncodedFormEntity(postParams));

            HttpResponse response = client.execute(post);

            // read response
            InputStream in = new BufferedInputStream(response.getEntity().getContent());
            return Utils.readStream(in);
        } catch (MalformedURLException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    private String stub() {
        String jsonUtente = getRawResource(R.raw.utente);

        return String.format("{\"done\": true, utente: %s}", jsonUtente);
    }
}
