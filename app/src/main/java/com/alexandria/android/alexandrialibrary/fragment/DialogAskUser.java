package com.alexandria.android.alexandrialibrary.fragment;


import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.alexandria.android.alexandrialibrary.R;

public class DialogAskUser  extends DialogFragment {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = getActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    builder.setView(inflater.inflate(R.layout.dialog_signin, null))
            // Add action buttons
            .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            // sign in the user ...
        }
    })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            LoginDialogFragment.this.getDialog().cancel();
        }
    });
    return builder.create();
}