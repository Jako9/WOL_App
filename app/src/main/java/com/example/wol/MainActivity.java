package com.example.wol;


import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wol.databinding.ActivityMainBinding;

import static com.example.wol.Util.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddDeviceDialogue.AddDeviceDialogueListener {

    private ActivityMainBinding binding;
    private ArrayList<Device> devices;

    private String MAC = "18:c0:4d:39:43:9d";


    @Override
    public void applyTexts(String name, String mac) {
        if (name.length() == 0) {
            Snackbar.make(binding.getRoot(), "Name cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }
        //if mac address not valid
        if (!mac.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            Snackbar.make(binding.getRoot(), "Invalid MAC address", Snackbar.LENGTH_SHORT).show();
            return;
        }


        Snackbar.make(binding.getRoot(), "Device added", Snackbar.LENGTH_SHORT).show();
        Log.d("Added", "New device added: \"" + name + "\" " + mac );
        addDevice(name, mac);
    }

    private void addDevice(String name, String mac) {
        Device device = new Device(name, mac);
        devices.add(device);
        if(!IO.saveDevice(devices)){
            Snackbar.make(binding.getRoot(), "Failed to save device", Snackbar.LENGTH_SHORT).show();
            return;
        }
        binding.deviceList.setAdapter(new DeviceAdapter(devices));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        devices = IO.loadDevices();
        RecyclerView recyclerView = binding.deviceList;
        recyclerView.setAdapter(new DeviceAdapter(devices));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Network.sendGetRequest(DEFAULT_IP,DEFAULT_PORT,MAC,KEY,FORMAT);
        Network.sendPostRequest(DEFAULT_IP,DEFAULT_PORT,MAC,KEY,FORMAT);

        binding.addNewDevice.setOnClickListener(view -> {
            openDialogNewDevice();
        });
    }

    private void openDialogNewDevice() {
        AddDeviceDialogue addDeviceDialogue = new AddDeviceDialogue();
        addDeviceDialogue.show(getSupportFragmentManager(),"add device dialog");
    }

}