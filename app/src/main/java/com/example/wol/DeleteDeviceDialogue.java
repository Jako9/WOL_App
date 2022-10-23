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

import com.google.android.material.snackbar.Snackbar;

public class DeleteDeviceDialogue extends AppCompatDialogFragment {

    private int position;
    private DeleteDeviceDialogueListener listener;

    public DeleteDeviceDialogue(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_device_dialogue,null);

        builder.setView(view)
                .setTitle("Delete Device")
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("ok",(dialog, which) -> {
                    listener.deleteDevice(position);
                });

        AlertDialog dialog = builder.create();
        return dialog;

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteDeviceDialogueListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DeleteDeviceDialogueListener");
        }
    }

    public interface DeleteDeviceDialogueListener {
        void deleteDevice(int position);
    }

}

