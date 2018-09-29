package org.rb.objectserialization;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import org.rb.objectserialization.model.Person;

/**
 * DialogFragment fragment should be android.support.v4.app.DialogFragment
 * Reference:
 * See Note in https://developer.android.com/guide/topics/ui/dialogs
 *
 */
public class PersonFormDlg extends DialogFragment {

    public static final int ACTION_ADD = 1;
    public static final int ACTION_EDIT = 2;
    private static final String PERSON = "person";
    private static final String ACTION_TYPE = "actionType";

    private int action;


    public interface IPersonFormDlgCallBack {
        void onPersonFormDlgOk(Person person);
        void onPersonEditOK(Person person);

    }

    public Bundle initArguments(int action, Person person){
        Bundle args = new Bundle();
        args.putInt(ACTION_TYPE,action);
        args.putString(PERSON,person.csv());
        return args;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        action = ACTION_ADD;
        final Person personForEdit = new Person();
        Bundle args = getArguments();

        if(args!=null){
           action = args.getInt(ACTION_TYPE);
           String personCSV =  args.getString(PERSON);
           personForEdit.copyCSVFields(personCSV);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
         LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.person_form, null);
        final EditText edName = view.findViewById(R.id.edName);
        final EditText edAddress= view.findViewById(R.id.edAddress);
        final EditText edAge = view.findViewById(R.id.edAge);
        if(action == ACTION_EDIT){
            edName.setText(personForEdit.getName());
            edAddress.setText(personForEdit.getAddress());
            edAge.setText(String.valueOf(personForEdit.getAge()));
        }
        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Person person = new Person();
                        person.setId(personForEdit.getId());
                        person.setName(edName.getText().toString());
                        person.setAddress(edAddress.getText().toString());
                        person.setAge(Integer.parseInt(edAge.getText().toString()));
                        switch (action){
                            case ACTION_ADD:
                                mListener.onPersonFormDlgOk(person);
                                break;
                            case ACTION_EDIT:
                                mListener.onPersonEditOK(person);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    private IPersonFormDlgCallBack mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (IPersonFormDlgCallBack) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement IPersonFormDlgCallBack");
        }

    }


}
