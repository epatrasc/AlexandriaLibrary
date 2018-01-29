package com.alexandria.android.alexandrialibrary.asynctask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.MainActivity;
import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.adaptor.BookListAdapter;
import com.alexandria.android.alexandrialibrary.adaptor.listener.BookListListener;
import com.alexandria.android.alexandrialibrary.helper.GlobalSettings;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.alexandria.android.alexandrialibrary.model.StatusResponse;
import com.alexandria.android.alexandrialibrary.service.PrestitiService;
import com.alexandria.android.alexandrialibrary.service.PrestitoService;

public class ActionTask extends AsyncTask<String, Void, LibroAction> {
    private Activity activity;
    private BookListListener listener;
    private int idLibro;
    private int idUtente;
    private PrestitoService service = null;

    public ActionTask(Activity activity, int idLibro, int idUtente) {
        this.activity = activity;
        this.idLibro = idLibro;
        this.idUtente = idUtente;
        this.service = new PrestitoService(activity.getApplicationContext());
        this.listener = null;
    }

    @Override
    protected LibroAction doInBackground(String... params) {
        String action = params != null ? params[0] : null;

        if (action == null) return null;

        if (action.equals(LibroAction.PRESTA)) return service.presta(idLibro, idUtente);
        if (action.equals(LibroAction.RESTITUISCI)) return service.restituisci(idLibro);

        return null;
    }

    @Override
    protected void onPostExecute(final LibroAction libroAction) {
        if (listener != null) {
            listener.onActionExecuted(libroAction);
        }
    }

    public void setOnActionExecuted(BookListListener listener) {
        this.listener = listener;
    }

}
