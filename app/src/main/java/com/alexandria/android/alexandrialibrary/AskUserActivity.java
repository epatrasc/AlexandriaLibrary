package com.alexandria.android.alexandrialibrary;

import android.content.Intent;
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
import com.google.gson.Gson;

import java.util.HashMap;

public class AskUserActivity extends AppCompatActivity {
    private LibroAction libroAction;
    private HashMap<String, Integer> spinnerMap;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        libroAction = new Gson().fromJson(intent.getStringExtra("libroAction"), LibroAction.class);

        setContentView(R.layout.activity_ask_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //spinner
        String[] spinnerArray = new String[2];
        spinnerMap = new HashMap<String, Integer>();
        spinnerMap.put("Dante", 1);
        spinnerArray[0] = "Dante";
        spinnerMap.put("Verdi", 2);
        spinnerArray[1] = "Verdi";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner = findViewById(R.id.spinner_ask_utente);
        spinner.setAdapter(adapter);

        // confirm button
        Button confirmButton = findViewById(R.id.admin_ask_user_btn_conferma);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nomeUtente = (String) spinner.getSelectedItem();
                int idUtente = spinnerMap.get(nomeUtente);
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
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("cconfirm-event");
        intent.putExtra("libroAction", new Gson().toJson(libroAction));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void goBack() {
        Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);
        intent.putExtra("libroAction", new Gson().toJson(libroAction));
        startActivity(intent);
    }
}
