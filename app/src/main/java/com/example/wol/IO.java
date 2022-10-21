package com.example.wol;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;

public class IO {
    public static boolean saveDevice(ArrayList<Device> devices) {
        try{
            throw new IOException("Not implemented");
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Device> loadDevices() {
        ArrayList<Device> devices = new ArrayList<>();
        devices.add(new Device("Test", "00:00:00:00:00:00"));
        devices.add(new Device("Test2", "00:00:00:00:00:01"));
        try{
            throw new IOException();//openFileInput(device.getMac());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return devices;
    }
}
