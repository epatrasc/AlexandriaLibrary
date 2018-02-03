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
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.AskUserActivity;
import com.alexandria.android.alexandrialibrary.BookDetailActivity;
import com.alexandria.android.alexandrialibrary.R;

import com.alexandria.android.alexandrialibrary.adaptor.listener.BookListListener;
import com.alexandria.android.alexandrialibrary.asynctask.ImageLoaderTask;
import com.alexandria.android.alexandrialibrary.asynctask.ActionTask;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.alexandria.android.alexandrialibrary.model.StatusResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends ArrayAdapter<LibroAction> {

    private final Activity context;
    private final String[] urls;
    private static LayoutInflater inflater = null;
    private List<LibroAction> libriAction = new ArrayList<>();
    private ImageLoaderTask imageLoader;
    private SessionManager session;

    public BookListAdapter(Activity context, List<LibroAction> libriAction) {
        super(context, R.layout.catalogo_list, libriAction);

        this.context = context;
        this.libriAction = libriAction;
        this.urls = new String[libriAction.size()];
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoaderTask(context.getApplicationContext());
        this.session = new SessionManager(context.getApplicationContext());
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View rowView = view;

        if (view == null)
            rowView = inflater.inflate(R.layout.catalogo_list, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.list_book_title);
        TextView txtAutori = (TextView) rowView.findViewById(R.id.list_book_autori);
        TextView txtEditore = (TextView) rowView.findViewById(R.id.list_book_editore);

        final Libro libro = libriAction.get(position).getLibro();

        txtTitle.setText(libro.getTitolo());
        txtAutori.setText(libro.getAutori());
        txtEditore.setText(libro.getEditore());

        imageLoader.DisplayImage(libro.getImageUrl(), imageView);

        // action button
        String action = libriAction.get(position).getAction();
        String actionLabel = action.equals(LibroAction.NO_ACTION) ? "In Prestito" : action;

        final Button actionButton = rowView.findViewById(R.id.action_button);
        actionButton.setTag(action);
        actionButton.setText(actionLabel);
        actionButton.setEnabled(!action.equals(LibroAction.NO_ACTION));


        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.setEnabled(false);
                Intent intent = new Intent(context.getApplicationContext(), AskUserActivity.class);
                context.startActivity(intent);
                int idUtente = session.getIdUtente();
                String action = (String) view.getTag();
                Libro libro = libriAction.get(position).getLibro();

                if (!action.equals(LibroAction.NO_ACTION)) {
                    ActionTask task = new ActionTask(actionButton, libro.getId(), idUtente);

                    // set task action listener
                    task.setOnActionExecuted(new BookListListener() {
                        @Override
                        public void onActionExecuted(View view, StatusResponse statusResponse) {
                            Toast.makeText(context.getApplicationContext(), statusResponse.getMessaggio(), Toast.LENGTH_SHORT).show();

                            if (statusResponse.isDone()) {
                                String newAction = view.getTag().equals(LibroAction.PRESTA) ? LibroAction.RESTITUISCI : LibroAction.PRESTA;
                                view.setTag(newAction);
                                actionButton.setText(newAction);
                            }
                            view.setEnabled(true);
                        }
                    });
                    task.execute(action);
                }

            }
        });

        // detail button
        Button detailButton = rowView.findViewById(R.id.detail_button);

        detailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("idLibro", libriAction.get(position).getLibro().getId());
                context.startActivity(intent);
            }
        });

        return rowView;
    }

    public int getCount() {
        return urls.length;
    }

    public long getItemId(int position) {
        return position;
    }
}