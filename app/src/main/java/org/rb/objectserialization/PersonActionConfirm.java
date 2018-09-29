package org.rb.objectserialization;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.UUID;

public class PersonActionConfirm extends DialogFragment {

    public static final String MSG = "msg";
    public static final String PERSON_ID = "personID";

    interface IPersonActionConfirm {
        void onPersonActionConfirmOK(UUID personID);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        String msg = args.getString(MSG);
        final String personId = args.getString(PERSON_ID);

        if(msg==null) msg= "Confirm to delete!";
        builder.setTitle("Confirm")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onPersonActionConfirmOK(UUID.fromString(personId));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();

    }

    private IPersonActionConfirm mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (IPersonActionConfirm) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement IConfirm interface");
        }
    }
}
