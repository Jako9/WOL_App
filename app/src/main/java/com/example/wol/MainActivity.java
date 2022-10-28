package com.example.wol;


import static com.example.wol.Status.*;
import static com.example.wol.Util.setDefaultIp;
import static com.example.wol.Util.setDefaultKey;
import static com.example.wol.Util.setDefaultPort;

import com.example.wol.Util.*;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wol.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddDeviceDialogue.AddDeviceDialogueListener, ChangeDefaultDialogue.ChangeDeviceDialogueListener, DeleteDeviceDialogue.DeleteDeviceDialogueListener {

    private ActivityMainBinding binding;
    private ArrayList<Device> devices;
    private DeviceAdapter currentDeviceAdapter;
    private Toolbar mToolbar;

    @Override
    public void handleDevice(Device device, int position, String name, String mac, String ip, int port, String key) {

        if(device == null){
            addDevice(name, mac.toLowerCase(), ip, port, key);
        }
        else{
            updateDevice(device,position, name, mac, ip, port, key);
        }

    }

    private void updateDevice(Device device, int position, String name, String mac, String ip, int port, String key) {
        device.setName(name);
        device.setMac(mac);
        device.setIp(ip);
        device.setPort(port);
        device.setKey(key);

        Log.d("Added", "Device updated: \"" + device.getName() + "\" " + device.getMac() );
        devices.set(position, device);
        currentDeviceAdapter = new DeviceAdapter(devices, getSupportFragmentManager());
        binding.deviceList.setAdapter(currentDeviceAdapter);
        refreshDeviceStatus();

    }

    private void addDevice(String name, String mac, String ip, int port, String key) {
        Snackbar.make(binding.getRoot(), "Device added", Snackbar.LENGTH_SHORT).show();
        Log.d("Added", "New device added: \"" + name + "\" " + mac );
        Device device = new Device(name, mac, ip, port, key);
        devices.add(device);
        if(!IO.saveDevice(this, devices)){
            devices.remove(device);
            Snackbar.make(binding.getRoot(), "Failed to save device", Snackbar.LENGTH_SHORT).show();
            return;
        }
        currentDeviceAdapter = new DeviceAdapter(devices, getSupportFragmentManager());
        binding.deviceList.setAdapter(currentDeviceAdapter);
        refreshDeviceStatus();
    }

    @Override
    public void applyUpdates(String ip, int port, String key){
        IO.saveDefault(this, ip, port, key);
        setDefaultIp(ip);
        setDefaultPort(port);
        setDefaultKey(key);
        Snackbar.make(binding.getRoot(), "Default updated", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HashMap<String, String> defaults = IO.loadDefaults(this);
        if(defaults.get("ip").length() == 0 || defaults.get("port").length() == 0 || defaults.get("key").length() == 0){
            openSettings(false);
        }else{
            setDefaultIp(defaults.get("ip"));
            setDefaultPort(Integer.parseInt(defaults.get("port")));
            setDefaultKey(defaults.get("key"));
        }

        mToolbar = binding.toolbar;
        setSupportActionBar(mToolbar);

        devices = IO.loadDevices(this);
        RecyclerView recyclerView = binding.deviceList;
        currentDeviceAdapter = new DeviceAdapter(devices, getSupportFragmentManager());
        recyclerView.setAdapter(currentDeviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        binding.addNewDevice.setOnClickListener(view -> {
            AddDeviceDialogue addDeviceDialogue = new AddDeviceDialogue(null,0);
            addDeviceDialogue.show(getSupportFragmentManager(),"add device dialog");
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
                openSettings(true);
                return true;
            default:
                return false;
        }
    }

    public void openSettings(boolean cancelAllowed){
        ChangeDefaultDialogue changeDefaultDialogue = new ChangeDefaultDialogue(cancelAllowed);
        changeDefaultDialogue.show(getSupportFragmentManager(),"change defaults dialog");
    }

    private void refreshDeviceStatus() {
        runOnUiThread(() -> {
            //Display loading icon
            binding.loading.setVisibility(android.view.View.VISIBLE);

        });
        Util.startHTTPCall(devices.toArray().length,this);
        for (Device device : devices) {
            Network.sendGetRequest(device, new CallbackStatus() {

                public void onSuccess(String response) {
                    if(response.equals("Online")) {
                        runOnUiThread(() -> {
                            currentDeviceAdapter.setStatus(device, ONLINE);
                        });
                    } else {
                        runOnUiThread(() -> {
                            currentDeviceAdapter.setStatus(device, OFFLINE);
                        });
                    }
                    Util.endHTTPCall();
                    Log.d("Update","Updated status of " + device.getName() + " to " + response);
                }

                public void onFailure(String error) {
                    Log.e("Error", "Error while updating Status Failure: " + error);
                    Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_SHORT).show();
                    runOnUiThread(() -> {
                        currentDeviceAdapter.setStatus(device, OFFLINE);
                    });
                    Util.endHTTPCall();
                }
            });
        }
    }

    public void disableLoading(){
        runOnUiThread(() -> {
            binding.loading.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void deleteDevice(int position) {
        Device device = devices.remove(position);
        if(!IO.saveDevice(this, devices)){
            Snackbar.make(binding.getRoot(), "Failed to delete device", Snackbar.LENGTH_SHORT).show();
            devices.add(position, device);
            return;
        }
        currentDeviceAdapter = new DeviceAdapter(devices, getSupportFragmentManager());
        binding.deviceList.setAdapter(currentDeviceAdapter);
        refreshDeviceStatus();
        Snackbar.make(binding.getRoot(), "Device deleted", Snackbar.LENGTH_SHORT).show();
    }
}