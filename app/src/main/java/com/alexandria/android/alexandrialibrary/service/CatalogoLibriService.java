package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.StatusResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CatalogoLibriService {

    public List<Libro> getLibri(Context context){
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
