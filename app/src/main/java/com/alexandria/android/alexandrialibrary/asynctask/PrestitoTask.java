package com.alexandria.android.alexandrialibrary.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.MainActivity;
import com.alexandria.android.alexandrialibrary.service.PrestitoService;

public class PrestitoTask extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private int idLibro;
    private int idUtente;

    public PrestitoTask(Context context,int idLibro,int idUtente) {
        this.context = context;
        this.idLibro = idLibro;
        this.idUtente = idUtente;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        PrestitoService service = new PrestitoService();
        try{

            Thread.sleep(2000);
        }catch (InterruptedException ex){
            Log.d("task", "doInBackground: sleep error");
        }

        return service.presta();
    }

    @Override
    protected void onPostExecute(final Boolean response) {
        String messagge = response ? String.format("Presitito OK | idLibro: %s, idUtente: %s",idLibro,idUtente) : "Errore durante il tentativo di prestito";
        Toast.makeText(context.getApplicationContext(), messagge, Toast.LENGTH_SHORT).show();
    }

}
