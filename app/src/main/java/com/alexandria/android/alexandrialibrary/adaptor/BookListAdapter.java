package com.alexandria.android.alexandrialibrary.adaptor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandria.android.alexandrialibrary.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.alexandria.android.alexandrialibrary.asynctask.LoadImageAsyncTask;

public class BookListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public BookListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.my_list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=null;//imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.my_list, null,true);

        ImageView imageView =(ImageView) rowView.findViewById(R.id.icon);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname[position]);
        extratxt.setText("Description "+itemname[position]);
        LoadImageAsyncTask loadImage = new LoadImageAsyncTask(imageView);
        loadImage.execute();
        return rowView;

    };
}