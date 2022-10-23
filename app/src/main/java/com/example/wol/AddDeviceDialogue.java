package com.example.wol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class AddDeviceDialogue extends AppCompatDialogFragment {

    private EditText editName;
    private EditText editMac;
    private EditText editIP;
    private EditText editPort;
    private EditText editKey;

    private TextView textIP;
    private TextView textPort;
    private TextView textKey;

    private ImageView expand;
    private ImageView collapse;

    private AddDeviceDialogueListener listener;
    private Device device;
    private int position;

    public AddDeviceDialogue(Device device, int position) {
        this.device = device;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_device_dialogue,null);

        builder.setView(view)
                .setTitle(device == null ? "Add device" : "Configure  " + device.getName())
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("ok",null);

        editName = view.findViewById(R.id.editName);
        editMac = view.findViewById(R.id.editMac);
        editIP = view.findViewById(R.id.customIP);
        editPort = view.findViewById(R.id.customPort);
        editKey = view.findViewById(R.id.customKey);

        textIP = view.findViewById(R.id.customIPText);
        textPort = view.findViewById(R.id.customPortText);
        textKey = view.findViewById(R.id.customKeyText);

        expand = view.findViewById(R.id.expandAddDevice);
        collapse = view.findViewById(R.id.collapseAddDevice);

        editIP.setHint(Util.DEFAULT_IP());
        editPort.setHint(String.valueOf(Util.DEFAULT_PORT()));

        if(device != null){
            editName.setText(device.getName());
            editMac.setText(device.getMac());
            editIP.setText(device.getIp());
            editPort.setText(device.getPort() == 0 ? "" : String.valueOf(device.getPort()));
            editKey.setText(device.getKey());
        }

        expand.setOnClickListener(v -> {
            editIP.setVisibility(View.VISIBLE);
            editPort.setVisibility(View.VISIBLE);
            editKey.setVisibility(View.VISIBLE);
            textIP.setVisibility(View.VISIBLE);
            textPort.setVisibility(View.VISIBLE);
            textKey.setVisibility(View.VISIBLE);
            collapse.setVisibility(View.VISIBLE);

            expand.setVisibility(View.GONE);
        });

        collapse.setOnClickListener(v -> {
            editIP.setVisibility(View.GONE);
            editPort.setVisibility(View.GONE);
            editKey.setVisibility(View.GONE);
            textIP.setVisibility(View.GONE);
            textPort.setVisibility(View.GONE);
            textKey.setVisibility(View.GONE);
            collapse.setVisibility(View.GONE);

            expand.setVisibility(View.VISIBLE);
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> ((AlertDialog) dialogInterface)
                .getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(this::positiveButtonClicked));
        return dialog;

    }

    private void positiveButtonClicked(View view) {
        String nameDevice = editName.getText().toString();
        String macAddress = editMac.getText().toString();
        String ip = editIP.getText().toString();
        int port = 0;
        try{
            port = Integer.parseInt(editPort.getText().toString());
        }
        catch (NumberFormatException e){
        }
        String key = editKey.getText().toString();


        if (nameDevice.length() == 0) {
            Snackbar.make(view, "Name cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }
        //if mac address not valid
        if (!macAddress.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            Snackbar.make(view, "Invalid MAC address", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(port < 0 || port > 65535){
            Snackbar.make(view, "Invalid port", Snackbar.LENGTH_SHORT).show();
            return;
        }
        listener.handleDevice(device, position, nameDevice,macAddress, ip, port, key);
        dismiss();
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
        void handleDevice(Device device, int position, String name, String mac, String ip, int port, String key);
    }

}
