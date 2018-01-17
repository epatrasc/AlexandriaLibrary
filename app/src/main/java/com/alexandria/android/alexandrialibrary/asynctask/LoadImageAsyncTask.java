package com.alexandria.android.alexandrialibrary.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView iamgeview;
    private String path;

    public LoadImageAsyncTask(ImageView iamgeview) {
        this.iamgeview = iamgeview;
        this.path = iamgeview.getTag().toString();
    }

    @Override
    protected Bitmap doInBackground(String ...urlImage) {
        URL url;
        ImageView imageView = null;
        Bitmap bitmap = null;
        System.out.println(urlImage);

        try {
            url = new URL("http://lorempixel.com/400/400/sports/");

            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bitmap);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        if (!iamgeview.getTag().toString().equals(path)) {
               /* The path is not same. This means that this
                  image view is handled by some other async task.
                  We don't do anything and return. */
            return;
        }

        if(result != null && iamgeview != null){
            iamgeview.setVisibility(View.VISIBLE);
            iamgeview.setImageBitmap(result);
        }else{
            iamgeview.setVisibility(View.GONE);
        }
    }

}