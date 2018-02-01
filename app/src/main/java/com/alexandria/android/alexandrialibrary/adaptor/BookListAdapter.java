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
import com.alexandria.android.alexandrialibrary.MainActivity;
import com.alexandria.android.alexandrialibrary.R;
import com.alexandria.android.alexandrialibrary.asynctask.ImageLoaderTask;
import com.alexandria.android.alexandrialibrary.fragment.DialogAskUser;
import com.alexandria.android.alexandrialibrary.helper.GlobalSettings;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends ArrayAdapter<LibroAction> {

    private final Activity context;
    private final String[] urls;
    private static LayoutInflater inflater = null;
    private List<LibroAction> libriAction = new ArrayList<>();
    private ImageLoaderTask imageLoader;

    public BookListAdapter(Activity context, List<LibroAction> libriAction) {
        super(context, R.layout.catalogo_list, libriAction);

        this.context = context;
        this.libriAction = libriAction;
        this.urls = new String[libriAction.size()];
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoaderTask(context.getApplicationContext());
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View rowView = view;

        if (view == null)
            rowView = inflater.inflate(R.layout.catalogo_list, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.list_book_title);
        TextView txtAutori = (TextView) rowView.findViewById(R.id.list_book_autori);
        TextView txtEditore = (TextView) rowView.findViewById(R.id.list_book_editore);

        Libro libro = libriAction.get(position).getLibro();

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

                int idUtente = GlobalSettings.getIdUtente(view.getContext());
                String action = (String) view.getTag();
                Libro libro = libriAction.get(position).getLibro();

                if (!action.equals(LibroAction.NO_ACTION)) {
                    boolean isAdministrator = GlobalSettings.isAdministrator(context.getApplicationContext());

                    if (action.equals(LibroAction.PRESTA) && isAdministrator) {
                        showDialogAskUser(actionButton, action, libro.getId());
                        return;
                    }
                    ((MainActivity)context).createActionTask(actionButton, action, libro.getId(), idUtente);
                }

            }
        });

        // detail button
        Button detailButton = rowView.findViewById(R.id.detail_button);

        detailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("libroAction", new Gson().toJson(libriAction.get(position)));
                context.startActivity(intent);
            }
        });

        return rowView;
    }

    public void showDialogAskUser(final Button button, String action, int idLibro) {
        // Create an instance of the dialog fragment and show it
        DialogAskUser dialog = DialogAskUser.newInstance(action, idLibro);
        dialog.show(((MainActivity)context).getSupportFragmentManager(), "DialogAskUserFragment");
    }

    public int getCount() {
        return urls.length;
    }

    public long getItemId(int position) {
        return position;
    }
}