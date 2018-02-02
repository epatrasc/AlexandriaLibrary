package com.alexandria.android.alexandrialibrary.service;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class MainService {
    private Context context;

    MainService(Context context) {
        this.context = context;
    }

    String getRawResource(int id) {
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(id);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
