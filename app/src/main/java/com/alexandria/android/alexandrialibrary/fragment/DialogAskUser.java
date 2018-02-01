package com.alexandria.android.alexandrialibrary.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexandria.android.alexandrialibrary.R;

import java.util.HashMap;

public class DialogAskUser extends DialogFragment {
    private DialogAskUserListener mListener;
    private Spinner spinner;
    private String action;
    private int idLibro;
    private HashMap<String, Integer> spinnerMap;

    public static DialogAskUser newInstance(String action, int idLibro) {
        DialogAskUser dialogAskUser = new DialogAskUser();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("action", action);
        args.putInt("idLibro", idLibro);
        dialogAskUser.setArguments(args);

        return dialogAskUser;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        action = (String) args.get("action");
        idLibro = (int) args.get("idLibro");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ask_user, null);

        Button btnEsegui = (Button) view.findViewById(R.id.dialog_ask_user_btn_esegui);
        Button btnAnulla = (Button) view.findViewById(R.id.dialog_ask_user_btn_annulla);
        btnEsegui.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nomeUtente = (String) spinner.getSelectedItem();
                        int idUtente = spinnerMap.get(nomeUtente);
                        mListener.onDialogPositiveClick(action, idLibro, idUtente);
                        DialogAskUser.this.getDialog().cancel();
                    }
                }
        );
        btnAnulla.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogAskUser.this.getDialog().cancel();
                    }
                });

        builder.setView(view);
        String[] spinnerArray = new String[2];
        spinnerMap = new HashMap<String, Integer>();
        spinnerMap.put("Dante", 1);
        spinnerArray[0] = "Dante";
        spinnerMap.put("Verdi", 2);
        spinnerArray[1] = "Verdi";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner = view.findViewById(R.id.diaslog_ask_utente_spinner_id_utente);
        spinner.setAdapter(adapter);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogAskUserListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DialogAskUserListener");
        }
    }

    public interface DialogAskUserListener {
        public void onDialogPositiveClick(String action, int idLibro, int idUtente);
    }
}