package com.alexandria.android.alexandrialibrary.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.adaptor.listener.BookListListener;
import com.alexandria.android.alexandrialibrary.asynctask.ImageLoaderTask;
import com.alexandria.android.alexandrialibrary.asynctask.ActionTask;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.alexandria.android.alexandrialibrary.model.Prestito;
import com.alexandria.android.alexandrialibrary.model.StatusResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PrestitiListAdapter extends ArrayAdapter<Prestito> {

    private final Activity context;
    private final String[] urls;
    private static LayoutInflater inflater = null;
    private List<Prestito> prestiti = new ArrayList<>();
    private ImageLoaderTask imageLoader;
    private SessionManager session;
    private TextView txtStato;

    public PrestitiListAdapter(Activity context, List<Prestito> prestiti) {
        super(context, R.layout.catalogo_list, prestiti);

        this.context = context;
        this.prestiti = prestiti;
        this.urls = new String[prestiti.size()];
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoaderTask(context.getApplicationContext());
        this.session = new SessionManager(context.getApplicationContext());
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View rowView = view;

        if (view == null)
            rowView = inflater.inflate(R.layout.prestiti_list, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.prestiti_list_icon);

        TextView txtUtente = (TextView) rowView.findViewById(R.id.prestiti_list_utente);
        TextView txtDataPrestito = (TextView) rowView.findViewById(R.id.prestiti_list_data_prestito);
        TextView txtDataRestituzione = (TextView) rowView.findViewById(R.id.prestiti_list_data_restituzione);

        Prestito prestito = prestiti.get(position);

        String nomeUtente = prestito.getNomeUtente();
        String dataPrestito = prestito.getDataPrestito() != null ? prestito.getDataPrestito().toString() : "-";
        String dataRestituzione = prestito.getDataRestituzione() != null ? prestito.getDataRestituzione().toString() : "-";

        View rowUtente = rowView.findViewById(R.id.prestito_row_utente);
        rowUtente.setVisibility(View.INVISIBLE);

        if (session.isAdministrator()) {
            txtUtente.setText(StringUtils.capitalize(nomeUtente));
            rowUtente.setVisibility(View.VISIBLE);
        }

        txtDataPrestito.setText(dataPrestito);
        txtDataRestituzione.setText(dataRestituzione);

        txtStato = (TextView) rowView.findViewById(R.id.prestiti_list_stato);
        txtStato.setText(prestito.isRestituito() ? "Restituito" : "In prestito");

        //TODO aggiornare model e service in modo che ritorni l'immagine del libro
        imageLoader.DisplayImage(prestito.getUrlImageLibro(), imageView);

        // restituisci button
        final Button restituisciButton = rowView.findViewById(R.id.restituisci_button);
        restituisciButton.setVisibility(Button.GONE);
        if (!prestito.isRestituito()) {
            restituisciButton.setVisibility(Button.VISIBLE);
            restituisciButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Prestito prestito = prestiti.get(position);
                    ActionTask task = new ActionTask(restituisciButton, prestito.getIdLibro());
                    // set task action listener
                    task.setOnActionExecuted(new BookListListener() {
                        @Override
                        public void onActionExecuted(View view, StatusResponse statusResponse) {
                            Toast.makeText(context.getApplicationContext(), statusResponse.getMessaggio(), Toast.LENGTH_SHORT).show();

                            if (statusResponse.isDone()) {
                                context.finish();
                                context.startActivity(context.getIntent());
                            } else {
                                view.setEnabled(true);
                            }
                        }
                    });

                    task.execute(LibroAction.RESTITUISCI);
                }
            });
        }


        return rowView;
    }

    public ImageLoaderTask getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoaderTask imageLoader) {
        this.imageLoader = imageLoader;
    }

    public int getCount() {
        return urls.length;
    }

    public long getItemId(int position) {
        return position;
    }
}