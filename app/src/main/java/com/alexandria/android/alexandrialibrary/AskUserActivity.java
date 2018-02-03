package com.alexandria.android.alexandrialibrary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.model.LibroAction;
import com.alexandria.android.alexandrialibrary.model.Utente;
import com.alexandria.android.alexandrialibrary.service.LibroActionService;
import com.alexandria.android.alexandrialibrary.service.LoginService;
import com.alexandria.android.alexandrialibrary.service.UtentiService;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class AskUserActivity extends AppCompatActivity {
    private HashMap<String, Integer> spinnerMap;
    private Spinner spinner;
    private int idUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // confirm button
        Button confirmButton = findViewById(R.id.admin_ask_user_btn_conferma);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nomeUtente = (String) spinner.getSelectedItem();
                idUtente = spinnerMap.get(nomeUtente);
                Toast.makeText(getApplicationContext(), nomeUtente + ": " + idUtente, Toast.LENGTH_SHORT).show();
                sendMessage();
            }
        });

        // back button
        Button backButton = findViewById(R.id.admin_ask_user_btn_annulla);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goBack();
            }
        });

        AskUserTask task = new AskUserTask();
        task.execute();
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("confirm-event");
        intent.putExtra("idUtente",idUtente);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    private void goBack() {
        finish();
    }

    public void createSpinner(List<Utente> utenti) {
        //spinner
        String[] spinnerArray = new String[utenti.size()];
        spinnerMap = new HashMap<String, Integer>();
        for(int i=0; i<utenti.size();i++){
            Utente utente = utenti.get(i);
            spinnerMap.put(utente.getNome(), utente.getId());
            spinnerArray[i] = utente.getNome();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner = findViewById(R.id.spinner_ask_utente);
        spinner.setAdapter(adapter);
    }

    public class AskUserTask extends AsyncTask<Void, Void, List<Utente>> {

        @Override
        protected List<Utente> doInBackground(Void... params) {
            UtentiService service = new UtentiService(getApplicationContext());
            return service.getAllUsers();
        }

        @Override
        protected void onPostExecute(List<Utente> utenti) {
            if (utenti != null) {
                createSpinner(utenti);
            }
        }
    }
}
