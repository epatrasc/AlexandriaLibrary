package com.alexandria.android.alexandrialibrary.asynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.MainActivity;
import com.alexandria.android.alexandrialibrary.adaptor.BookListAdapter;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.service.CatalogoLibriService;

import java.util.List;

public class CatalogoLibriTask extends AsyncTask<Void, Void, List<Libro>> {
    private MainActivity activity;

    public CatalogoLibriTask(MainActivity activitiy) {
        this.activity = activitiy;
    }

    @Override
    protected List<Libro> doInBackground(Void... params) {
        CatalogoLibriService service = new CatalogoLibriService();

        return service.getLibri(activity.getApplicationContext());
    }

    @Override
    protected void onPostExecute(final List<Libro> libri) {
        activity.showProgress(false);

        if (libri != null && libri.size() > 0) {
            BookListAdapter adapter = new BookListAdapter(activity, libri);

            activity.getBookView().setAdapter(adapter);
            activity.getBookView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Libro libro = libri.get(+position);
                    Toast.makeText(activity.getApplicationContext(), libro.getTitolo(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            onCancelled();
            // TODO visualizza messaggio catalogo vuoto
        }
    }

    @Override
    protected void onCancelled() {
        activity.showProgress(false);
    }
}
