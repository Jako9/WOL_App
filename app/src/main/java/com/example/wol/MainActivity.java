package com.example.wol;


import static com.example.wol.Status.*;
import static com.example.wol.Util.setDefaultIp;
import static com.example.wol.Util.setDefaultKey;
import static com.example.wol.Util.setDefaultPort;

import com.example.wol.Util.*;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wol.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddDeviceDialogue.AddDeviceDialogueListener, ChangeDefaultDialogue.AddDeviceDialogueListener {

    private ActivityMainBinding binding;
    private ArrayList<Device> devices;
    private DeviceAdapter currentDeviceAdapter;
    private Toolbar mToolbar;

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
        addDevice(name, mac.toLowerCase());
    }

    private void addDevice(String name, String mac) {
        Device device = new Device(name, mac);
        devices.add(device);
        if(!IO.saveDevice(this, devices)){
            devices.remove(device);
            Snackbar.make(binding.getRoot(), "Failed to save device", Snackbar.LENGTH_SHORT).show();
            return;
        }
        currentDeviceAdapter = new DeviceAdapter(devices);
        binding.deviceList.setAdapter(currentDeviceAdapter);
        refreshDeviceStatus();
    }

    @Override
    public void applyUpdates(String ip, int port, String key){
        if (ip.length() == 0) {
            Snackbar.make(binding.getRoot(), "IP cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (port < 0 || port > 65535) {
            Snackbar.make(binding.getRoot(), "Invalid port", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (key.length() == 0) {
            Snackbar.make(binding.getRoot(), "Key cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }
        IO.saveDefault(this, ip, port, key);
        setDefaultIp(ip);
        setDefaultPort(port);
        setDefaultKey(key);
        Snackbar.make(binding.getRoot(), "Default updated", Snackbar.LENGTH_SHORT).show();
    }


    public void apply(String name, String mac) {
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
        addDevice(name, mac.toLowerCase());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mToolbar = binding.toolbar;
        setSupportActionBar(mToolbar);

        devices = IO.loadDevices(this);
        RecyclerView recyclerView = binding.deviceList;
        currentDeviceAdapter = new DeviceAdapter(devices);
        recyclerView.setAdapter(currentDeviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HashMap<String, String> defaults = IO.loadDefaults(this);
        setDefaultIp(defaults.get("ip"));
        setDefaultPort(Integer.parseInt(defaults.get("port")));
        setDefaultKey(defaults.get("key"));


        binding.addNewDevice.setOnClickListener(view -> {
            openDialogNewDevice();
        });

        refreshDeviceStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.refresh:
                refreshDeviceStatus();
                return true;
            case R.id.settings:
                openSettings();
                return true;
            default:
                return false;
        }
    }

    public void openSettings(){
        ChangeDefaultDialogue changeDefaultDialogue = new ChangeDefaultDialogue();
        changeDefaultDialogue.show(getSupportFragmentManager(),"change defaults dialog");
    }

    private void openDialogNewDevice() {
        AddDeviceDialogue addDeviceDialogue = new AddDeviceDialogue();
        addDeviceDialogue.show(getSupportFragmentManager(),"add device dialog");
    }

    private void refreshDeviceStatus() {
        for (Device device : devices) {
            Network.sendGetRequest(device, new CallbackStatus() {

                public void onSuccess(String response) {
                    if(response.equals("Online")) {
                        currentDeviceAdapter.setStatus(device, ONLINE);
                    } else {
                        currentDeviceAdapter.setStatus(device, OFFLINE);
                    }
                    System.out.println("Updated status of " + device.getName() + " to " + response);
                }

                public void onFailure(String error) {
                    System.out.println("Error while updating Status Failure: " + error);
                    currentDeviceAdapter.setStatus(device, OFFLINE);
                }
            });
        }
    }

}