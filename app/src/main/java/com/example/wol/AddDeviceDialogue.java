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

public class AddDeviceDialogue extends AppCompatDialogFragment {

    private EditText editName;
    private EditText editMac;
    private AddDeviceDialogueListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialogue,null);

        builder.setView(view)
                .setTitle("Add device")
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("ok",(dialog, which) -> {
                    String nameDevice = editName.getText().toString();
                    String macAddress = editMac.getText().toString();
                    listener.applyTexts(nameDevice,macAddress);
                });

        editName = view.findViewById(R.id.editName);
        editMac = view.findViewById(R.id.editMac);
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
        void applyTexts(String name, String mac);
    }

}
