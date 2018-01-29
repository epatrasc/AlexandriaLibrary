package com.alexandria.android.alexandrialibrary.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.model.Utente;
import com.google.gson.Gson;

public class GlobalSettings {

    public static Utente getUtente(Context context){
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.app_share_base), Context.MODE_PRIVATE);
        String json =  prefs.getString(context.getString(R.string.shared_preferences_utente), null);

        if(json!=null){
            return new Gson().fromJson(json, Utente.class);
        }

        return null;
    }
    public static int getIdUtente(Context context){
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.app_share_base), Context.MODE_PRIVATE);
        return prefs.getInt(context.getString(R.string.shared_preferences_id_utente), 0);
    }

    public static boolean isAdministrator(Context context){
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.app_share_base), Context.MODE_PRIVATE);
        return prefs.getBoolean(context.getString(R.string.shared_preferences_is_admin), false);
    }
}
