package com.alexandria.android.alexandrialibrary.asynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.MainActivity;
import com.alexandria.android.alexandrialibrary.adaptor.BookListAdapter;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.alexandria.android.alexandrialibrary.service.CatalogoLibriService;

import java.util.List;

public class CatalogoLibriTask extends AsyncTask<Void, Void, List<LibroAction>> {
    private MainActivity activity;

    public CatalogoLibriTask(MainActivity activitiy) {
        this.activity = activitiy;
    }

    @Override
    protected List<LibroAction> doInBackground(Void... params) {
        CatalogoLibriService service = new CatalogoLibriService(activity.getApplicationContext());

        return service.getLibri();
    }

    @Override
    protected void onPostExecute(final List<LibroAction> libriAction) {
        activity.showProgress(false);

        if (libriAction != null && libriAction.size() > 0) {
            activity.updateBookView(new BookListAdapter(activity, libriAction));
        } else {
            onCancelled();
        }
    }

    @Override
    protected void onCancelled() {
        activity.showProgress(false);
    }
}
