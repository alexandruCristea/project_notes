package com.example.alexandrucristea.memories;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExampleDialog extends AppCompatDialogFragment {

    private EditText editTextUsername;
    private EditText editTextPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Add Note").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                listener.applyTexts(username, password);

                firebaseAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child(user.getUid()).setValue(username);
                databaseReference.child(user.getUid()).setValue(password);
            }
        });

        editTextUsername = view.findViewById(R.id.title);
        editTextPassword = view.findViewById(R.id.description);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
try {
    listener = (ExampleDialogListener) context;
} catch (ClassCastException e) {
   throw new ClassCastException(context.toString() + "implement ExampleDialogListener");
}
    }


    public interface ExampleDialogListener {
        void applyTexts(String title, String description);
        }

}
