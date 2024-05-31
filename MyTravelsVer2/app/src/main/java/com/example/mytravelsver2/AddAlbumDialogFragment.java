package com.example.mytravelsver2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddAlbumDialogFragment extends DialogFragment {

    private EditText edtAlbumName;
    // Use this instance of the interface to deliver action events
    DialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView =  inflater.inflate(R.layout.dialog_layout, null);
        edtAlbumName = dialogView.findViewById(R.id.edtAlbumName);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DialogListener so we can send events to the host
            listener = (DialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getTargetFragment()
                    + " must implement DialogListener");
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(AddAlbumDialogFragment.this, edtAlbumName.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(AddAlbumDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface DialogListener {
        public void onDialogPositiveClick(AddAlbumDialogFragment dialog, String text);
        public void onDialogNegativeClick(AddAlbumDialogFragment dialog);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
