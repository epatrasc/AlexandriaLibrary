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

public class BookListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] urls;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public BookListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.my_list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.urls= new String[itemname.length];//imgid;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader=new ImageLoader(context.getApplicationContext());
    }

    public View getView(int position,View view,ViewGroup parent) {
        View rowView = view;

        if(view==null)
            rowView=inflater.inflate(R.layout.my_list, null,true);

        ImageView imageView =(ImageView) rowView.findViewById(R.id.icon);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.list_book_title);
        TextView txtAutori = (TextView) rowView.findViewById(R.id.list_book_autori);
        TextView txtEditore = (TextView) rowView.findViewById(R.id.list_book_editore);

        txtTitle.setText(itemname[position]);
        txtAutori.setText(itemname[position]);
        txtEditore.setText("Editore: Feltrinelli");
        //imageLoader.DisplayImage(itemname[position], imageView);
        imageLoader.DisplayImage("http://via.placeholder.com/400x400", imageView);
        return rowView;

    };

    public int getCount() {
        return urls.length;
    }

    public long getItemId(int position) {
        return position;
    }
}