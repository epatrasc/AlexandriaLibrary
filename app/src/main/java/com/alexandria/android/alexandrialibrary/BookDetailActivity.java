package com.alexandria.android.alexandrialibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
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
import com.alexandria.android.alexandrialibrary.model.Utente;
import com.alexandria.android.alexandrialibrary.service.LibroActionService;
import com.alexandria.android.alexandrialibrary.service.LoginService;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private LibroAction libroAction;
    private Activity activity;
    private SessionManager session;
    private Button actionButton;
    private View bookDetailView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        activity = this;
        session = new SessionManager(getApplicationContext());
        bookDetailView = findViewById(R.id.book_detail_card);
        progressView = findViewById(R.id.detail_book_progressBar);


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("confirm-event"));

        Bundle extras = getIntent().getExtras();
        int idLibro;

        if (extras != null) {
            idLibro = extras.getInt("idLibro");
            retrieveLibroAction(idLibro);
        }
    }

    private void retrieveLibroAction(int idLibro) {
        showProgress(true);
        BookDetailTask task = new BookDetailTask(libroAction, idLibro);
        task.execute();
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            bookDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
            bookDetailView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bookDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            bookDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void updateView(final LibroAction libroAction) {
        this.libroAction = libroAction;
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int idUtente = intent.getIntExtra("idUtente",-1);
            ActionTask task = new ActionTask(actionButton, libroAction.getLibro().getId(), idUtente);

            // set task action listener
            task.setOnActionExecuted(new BookListListener() {
                @Override
                public void onActionExecuted(View view, StatusResponse statusResponse) {
                    Toast.makeText(getApplicationContext(), statusResponse.getMessaggio(), Toast.LENGTH_SHORT).show();

                    if (statusResponse.isDone()) {
                        finish();
                        startActivity(getIntent());
                    }
                }
            });
            task.execute(LibroAction.PRESTA);

            Log.d("receiver", "Got message: " + idUtente);
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public class BookDetailTask extends AsyncTask<Void, Void, LibroAction> {
        private int idLibro;

        BookDetailTask(LibroAction libroAction, int idLibro) {
            this.idLibro = idLibro;
        }

        @Override
        protected LibroAction doInBackground(Void... params) {
            LibroActionService service = new LibroActionService(getApplicationContext());
            return service.get(idLibro);
        }

        @Override
        protected void onPostExecute(LibroAction libroAction) {
            if (libroAction != null) {
                updateView(libroAction);
            }
            showProgress(false);
        }
    }
}
