package com.example.wol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangeDefaultDialogue extends AppCompatDialogFragment {

    private EditText editIP;
    private EditText editPort;
    private EditText editKey;
    private AddDeviceDialogueListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_default_dialogue,null);

        builder.setView(view)
                .setTitle("Change Deafults")
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("ok",(dialog, which) -> {
                    String newIP = editIP.getText().toString();
                    int newPort = -1;
                    try {
                        newPort = Integer.parseInt(editPort.getText().toString());
                    } catch (NumberFormatException e) {
                    }
                    String newKey = editKey.getText().toString();
                    listener.applyUpdates(newIP,newPort,newKey);
                });

        editIP = view.findViewById(R.id.default_ip);
        editPort = view.findViewById(R.id.default_port);
        editKey = view.findViewById(R.id.default_key);

        //Change content of edit boxes to current default values
        editIP.setText(Util.DEFAULT_IP());
        editPort.setText(Util.DEFAULT_PORT() + "");
        editKey.setText(Util.DEFAULT_KEY());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddDeviceDialogueListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement AddDeviceDialogueListener");
        }
    }

    public interface AddDeviceDialogueListener {
        void applyUpdates(String ip, int port, String key);
    }

}
