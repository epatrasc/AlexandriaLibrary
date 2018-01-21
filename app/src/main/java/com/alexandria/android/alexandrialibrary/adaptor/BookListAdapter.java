package com.alexandria.android.alexandrialibrary.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandria.android.alexandrialibrary.R;

import com.alexandria.android.alexandrialibrary.asynctask.ImageLoader;
import com.alexandria.android.alexandrialibrary.model.Libro;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends ArrayAdapter<Libro> {

    private final Activity context;
    private final String[] urls;
    private static LayoutInflater inflater = null;
    private List<Libro> libri = new ArrayList<>();
    public ImageLoader imageLoader;

    public BookListAdapter(Activity context, List<Libro> libri) {
        super(context, R.layout.my_list, libri);

        this.context = context;
        this.libri = libri;
        this.urls = new String[libri.size()];
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoader(context.getApplicationContext());
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = view;

        if (view == null)
            rowView = inflater.inflate(R.layout.my_list, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.list_book_title);
        TextView txtAutori = (TextView) rowView.findViewById(R.id.list_book_autori);
        TextView txtEditore = (TextView) rowView.findViewById(R.id.list_book_editore);

        Libro libro = libri.get(position);

        txtTitle.setText(libro.getTitolo());
        txtAutori.setText(libro.getAutori());
        txtEditore.setText(libro.getEditore());

        imageLoader.DisplayImage(libro.getImageUrl(), imageView);

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