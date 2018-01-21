package com.alexandria.android.alexandrialibrary.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alexandria.android.alexandrialibrary.MainActivity;
import com.alexandria.android.alexandrialibrary.service.PrestitoService;

public class PrestitoTask extends AsyncTask<String, Void, Boolean> {
    private Context context;
    private int idLibro;
    private int idUtente;
    private PrestitoService service = null;

    public static final String ACTION_PRESTA = "presta";
    public static final String ACTION_RESTITUISCI = "restituisci";

    public PrestitoTask(Context context,int idLibro) {
        this.context = context;
        this.idLibro = idLibro;
        this.service  = new PrestitoService();
    }

    public PrestitoTask(Context context,int idLibro,int idUtente) {
        this.context = context;
        this.idLibro = idLibro;
        this.idUtente = idUtente;
        this.service  = new PrestitoService();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try{

            Thread.sleep(2000);
        }catch (InterruptedException ex){
            Log.d("task", "doInBackground: sleep error");
        }

        if(params!=null &&  params[0].equals(ACTION_PRESTA)){
                return service.presta(idLibro, idUtente);
        }

        return service.restituisci(idLibro);

    }

    @Override
    protected void onPostExecute(final Boolean response) {
        String messagge = response ? String.format("Presitito OK | idLibro: %s, idUtente: %s",idLibro,idUtente) : "Errore durante il tentativo di prestito";
        Toast.makeText(context.getApplicationContext(), messagge, Toast.LENGTH_SHORT).show();
    }

}
