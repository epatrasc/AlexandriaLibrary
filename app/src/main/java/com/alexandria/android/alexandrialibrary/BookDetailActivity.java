package com.alexandria.android.alexandrialibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.adaptor.listener.BookListListener;
import com.alexandria.android.alexandrialibrary.asynctask.ActionTask;
import com.alexandria.android.alexandrialibrary.asynctask.ImageLoaderTask;
import com.alexandria.android.alexandrialibrary.helper.SessionManager;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.alexandria.android.alexandrialibrary.model.StatusResponse;
import com.google.gson.Gson;

public class BookDetailActivity extends AppCompatActivity {
    private LibroAction libroAction;
    private Activity activity;
    private SessionManager session;
    private Button actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        session = new SessionManager(getApplicationContext());

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("confirm-event"));


        setContentView(R.layout.activity_book_detail);
        Intent intent = getIntent();

        libroAction = new Gson().fromJson(intent.getStringExtra("libroAction"), LibroAction.class);
        Libro libro = libroAction.getLibro();

        updateViewText();

        // update image
        ImageView imageView = (ImageView) findViewById(R.id.book_detail_image);
        ImageLoaderTask imageLoader = new ImageLoaderTask(getApplicationContext());
        imageLoader.DisplayImage(libro.getImageUrl(), imageView);

        // back button
        Button backButton = findViewById(R.id.book_detail_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // action button
        String action = libroAction.getAction();
        String actionLabel = action.equals(LibroAction.NO_ACTION) ? "In Prestito" : action;

        actionButton = findViewById(R.id.book_detail_btn_action);
        actionButton.setTag(action);
        actionButton.setText(actionLabel);
        actionButton.setEnabled(!action.equals(LibroAction.NO_ACTION));

        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (session.isAdministrator()) {
                    Intent intent = new Intent(getApplicationContext(), AskUserActivity.class);
                    intent.putExtra("libroAction", new Gson().toJson(libroAction));
                    startActivity(intent);
                    return;

                }

                executeAction(session.getIdUtente());
            }
        });
    }

    private void executeAction(int idUtente) {

        String action = libroAction.getAction();
        ActionTask task = new ActionTask(actionButton, libroAction.getLibro().getId(), idUtente);

        task.setOnActionExecuted(new BookListListener() {
            @Override
            public void onActionExecuted(View view, StatusResponse statusResponse) {
                Toast.makeText(activity.getApplicationContext(), statusResponse.getMessaggio(), Toast.LENGTH_SHORT).show();

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

    private void updateViewText() {
        TextView titolo = findViewById(R.id.book_detail_title);
        TextView autori = findViewById(R.id.book_detail_autori);
        TextView editore = findViewById(R.id.book_detail_editore);
        TextView descrizione = findViewById(R.id.book_detail_description);

        Libro libro = libroAction.getLibro();
        titolo.setText(libro.getTitolo());
        autori.setText(libro.getAutori());
        editore.setText(libro.getEditore());
        descrizione.setText(libro.getDescrizione());
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
