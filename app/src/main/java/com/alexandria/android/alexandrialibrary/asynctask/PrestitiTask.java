package com.alexandria.android.alexandrialibrary.asynctask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.MainActivity;
import com.alexandria.android.alexandrialibrary.PrestitiActivity;
import com.alexandria.android.alexandrialibrary.adaptor.BookListAdapter;
import com.alexandria.android.alexandrialibrary.adaptor.PrestitiListAdapter;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.Prestito;
import com.alexandria.android.alexandrialibrary.service.CatalogoLibriService;
import com.alexandria.android.alexandrialibrary.service.PrestitiService;

import java.util.List;

public class PrestitiTask extends AsyncTask<Void, Void, List<Prestito>> {
    private PrestitiActivity activity;

    public PrestitiTask(PrestitiActivity activitiy) {
        this.activity = activitiy;
    }

    @Override
    protected List<Prestito> doInBackground(Void... params) {
        PrestitiService service = new PrestitiService();

        return service.getPrestiti(activity.getApplicationContext());
    }

    @Override
    protected void onPostExecute(final List<Prestito> prestiti) {
        activity.showProgress(false);

        if (prestiti != null && prestiti.size() > 0) {
            PrestitiListAdapter adapter = new PrestitiListAdapter(activity, prestiti);

            activity.getPrestitiView().setAdapter(adapter);
        } else {
            onCancelled();
            // TODO visualizza messaggio errore
        }
    }

    @Override
    protected void onCancelled() {
        activity.showProgress(false);
    }
}
