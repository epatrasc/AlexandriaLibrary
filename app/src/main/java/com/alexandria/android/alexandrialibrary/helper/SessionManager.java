package com.alexandria.android.alexandrialibrary.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.model.Utente;
import com.google.gson.Gson;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "com.alexandria.android.alexandrialibrary";
    private static final String KEY_IS_LOGGED_IN = "isLogged";
    private static final String KEY_UTENTE = "utente";
    private static final String KEY_ID_UTENTE = "idUtente";
    private static final String KEY_IS_ADMIN = "isAdmin";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void reset() {
        editor.clear();
        editor.apply();

        Log.d(TAG, "Reset Session");
    }

    public void setisLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setUtente(Utente utente) {
        editor.putString(KEY_UTENTE, new Gson().toJson(utente).toString());

        // commit changes
        editor.commit();

        Log.d(TAG, "Utente modificato!");
    }

    public void setIdUtente(int idUtente) {
        editor.putInt(KEY_ID_UTENTE, idUtente);

        // commit changes
        editor.commit();

        Log.d(TAG, "Id Utente modificato!");
    }

    public void setIsAdministrator(boolean isAdministrator) {
        editor.putBoolean(KEY_IS_ADMIN, isAdministrator);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public Utente getUtente() {
        String json = pref.getString(KEY_UTENTE, null);

        if (json != null) {
            return new Gson().fromJson(json, Utente.class);
        }

        return null;
    }

    public int getIdUtente() {
        return pref.getInt(KEY_ID_UTENTE, 0);
    }

    public boolean isAdministrator() {
        return pref.getBoolean(KEY_IS_ADMIN, false);
    }
}
