package com.alexandria.android.alexandrialibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.adaptor.listener.BookListListener;
import com.alexandria.android.alexandrialibrary.asynctask.ImageLoaderTask;
import com.alexandria.android.alexandrialibrary.asynctask.ActionTask;
import com.alexandria.android.alexandrialibrary.fragment.DialogAskUser;
import com.alexandria.android.alexandrialibrary.helper.GlobalSettings;
import com.alexandria.android.alexandrialibrary.model.Libro;
import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.google.gson.Gson;

public class BookDetailActivity extends AppCompatActivity {
    private Libro libro;
    private Activity activity;
    private Button actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        setContentView(R.layout.activity_book_detail);
        Intent intent = getIntent();

        libro = new Gson().fromJson(intent.getStringExtra("libro"), Libro.class);

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
                int idUtente = GlobalSettings.getIdUtente(view.getContext());
                String action = libro.isDisponibile() ? LibroAction.PRESTA : LibroAction.PRESTA;
                ActionTask task = new ActionTask(activity, libro.getId(), idUtente);

                task.setOnActionExecuted(new BookListListener() {
                    @Override
                    public void onActionExecuted(LibroAction libroAction) {
                        Toast.makeText(view.getContext(), statusResponse.getMessaggio(), Toast.LENGTH_SHORT).show();

                if (statusResponse.isDone()) {
                    String newAction = view.getTag().equals(LibroAction.PRESTA) ? LibroAction.RESTITUISCI : LibroAction.PRESTA;
                    actionButton.setTag(newAction);
                    actionButton.setText(newAction);
                }
                actionButton.setEnabled(true);
                    }
                });

                task.execute(action);
            }
        });

        task.execute(action);
    }

    public void showDialogAskUser(String action, int idLibro) {
        // Create an instance of the dialog fragment and show it
        DialogAskUser dialog = DialogAskUser.newInstance(action, idLibro);
        dialog.show(getSupportFragmentManager(), "DialogAskUserFragment");
    }

    @Override
    public void onDialogPositiveClick(String action, int idLibro, int idUtente) {
        createActionTask(action, idLibro, idUtente);
    }

    @Override
    public void onDialogPositiveClick(Button button, String action, int idLibro, int idUtente) {
        // Not yet Implemented
    }

    private void updateViewText() {
        TextView titolo = findViewById(R.id.book_detail_title);
        TextView autori = findViewById(R.id.book_detail_autori);
        TextView editore = findViewById(R.id.book_detail_editore);
        TextView descrizione = findViewById(R.id.book_detail_description);

        titolo.setText(libro.getTitolo());
        autori.setText(libro.getAutori());
        editore.setText(libro.getEditore());
        descrizione.setText(libro.getDescrizione());
    }
}
