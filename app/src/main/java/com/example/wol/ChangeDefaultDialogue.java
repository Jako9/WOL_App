package com.example.wol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class ChangeDefaultDialogue extends AppCompatDialogFragment {

    private EditText editIP;
    private EditText editPort;
    private EditText editKey;
    private ChangeDeviceDialogueListener listener;
    private final boolean cancelAllowed;

    public ChangeDefaultDialogue(boolean cancelAllowed) {
        this.cancelAllowed = cancelAllowed;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        setCancelable(cancelAllowed);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_default_dialogue,null);

        if(cancelAllowed) {
            builder.setView(view)
                    .setTitle("Change Defaults")
                    .setNegativeButton("Cancel", (dialog, which) -> {
                    })
                    .setPositiveButton("Save", null);
        }
        else{
            builder.setView(view)
                    .setTitle("Change Defaults")
                    .setPositiveButton("Save", null);
        }

        editIP = view.findViewById(R.id.default_ip);
        editPort = view.findViewById(R.id.default_port);
        editKey = view.findViewById(R.id.default_key);

        //Change content of edit boxes to current default values
        editIP.setText(Util.DEFAULT_IP());
        if(Util.DEFAULT_PORT() == 0) {
            editPort.setText("");
        } else {
            editPort.setText(String.valueOf(Util.DEFAULT_PORT()));
        }
        editKey.setText(Util.DEFAULT_KEY());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> ((AlertDialog) dialogInterface)
                .getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(this::positiveButtonClicked));
        return dialog;
    }

    private void positiveButtonClicked(View view) {
        String newIP = editIP.getText().toString();
        int newPort = -1;
        try {
            newPort = Integer.parseInt(editPort.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("ChangeDefaultDialogue", "Port is not a number");
        }
        String newKey = editKey.getText().toString();

        //Check if the new values are valid
        if (newIP.equals("") || newPort <= 0 || newPort > 65535 || newKey.equals("")) {
            Snackbar.make(view, "Invalid values", Snackbar.LENGTH_SHORT).show();
        }else {
            listener.applyUpdates(newIP, newPort, newKey);
            dismiss();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChangeDeviceDialogueListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement AddDeviceDialogueListener");
        }
    }

    public interface ChangeDeviceDialogueListener {
        void applyUpdates(String ip, int port, String key);
    }

}
