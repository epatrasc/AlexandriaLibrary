package com.alexandria.android.alexandrialibrary.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandria.android.alexandrialibrary.BookDetailActivity;
import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.asynctask.ImageLoaderTask;
import com.alexandria.android.alexandrialibrary.asynctask.PrestitoTask;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.Prestito;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PrestitiListAdapter extends ArrayAdapter<Prestito> {

    private final Activity context;
    private final String[] urls;
    private static LayoutInflater inflater = null;
    private List<Prestito> prestiti = new ArrayList<>();
    public ImageLoaderTask imageLoader;

    public PrestitiListAdapter(Activity context, List<Prestito> prestiti) {
        super(context, R.layout.catalogo_list, prestiti);

        this.context = context;
        this.prestiti = prestiti;
        this.urls = new String[prestiti.size()];
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoaderTask(context.getApplicationContext());
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View rowView = view;

        if (view == null)
            rowView = inflater.inflate(R.layout.prestiti_list, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.prestiti_list_icon);

        TextView txtUtente = (TextView) rowView.findViewById(R.id.prestiti_list_utente);
        TextView txtDataPrestito = (TextView) rowView.findViewById(R.id.prestiti_list_data_prestito);
        TextView txtDataRestituzione = (TextView) rowView.findViewById(R.id.prestiti_list_data_restituzione);
        TextView txtStato = (TextView) rowView.findViewById(R.id.prestiti_list_stato);

        Prestito prestito = prestiti.get(position);

        String idUtente = Integer.toString(prestito.getIdUtente());
        String dataPrestito = prestito.getDataPrestito() != null ? prestito.getDataPrestito().toString() : "-";
        String dataRestituzione = prestito.getDataRestituzione() != null ? prestito.getDataRestituzione().toString() : "-";

        txtUtente.setText(idUtente);
        txtDataPrestito.setText(dataPrestito);
        txtDataRestituzione.setText(dataRestituzione);
        txtStato.setText(prestito.isRestituito() ? "Restituito" : "In prestito");

        //TODO aggiornare model e service in modo che ritorni l'immagine del libro
        imageLoader.DisplayImage("http://lorempixel.com/400/200/city/?xx="+position, imageView);

        // restituisci button
        Button restituisciButton = rowView.findViewById(R.id.restituisci_button);
        restituisciButton.setVisibility(Button.GONE);
        if (!prestito.isRestituito()) {
            restituisciButton.setVisibility(Button.VISIBLE);
            restituisciButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Prestito prestito = prestiti.get(position);

                    PrestitoTask task = new PrestitoTask(view.getContext(), prestito.getIdLibro());
                    task.execute(PrestitoTask.ACTION_RESTITUISCI);
                }
            });
        }


        return rowView;
    }

    ;

    public int getCount() {
        return urls.length;
    }

    public long getItemId(int position) {
        return position;
    }
}