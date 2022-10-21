package com.example.wol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class IO {
    public static boolean saveDevice(Context context, ArrayList<Device> devices) {
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("devices", ObjectSerializer.serialize(devices)).apply();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Device> loadDevices(Context context) {
        ArrayList<Device> devices = new ArrayList<>();
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
            devices = (ArrayList<Device>) ObjectSerializer.deserialize(sharedPreferences.getString("devices", ObjectSerializer.serialize(new ArrayList<Device>())));
        }
        catch(InvalidClassException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return devices;
    }


    private static class ObjectSerializer {
        public static String serialize(Serializable obj) throws IOException {
            if (obj == null) return "";
            try {
                    ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
                    ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
                    objStream.writeObject(obj);
                    objStream.close();
                    return encodeBytes(serialObj.toByteArray());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public static Object deserialize(String str) throws IOException {
                if (str == null || str.length() == 0) return null;
                try {
                    ByteArrayInputStream serialObj = new ByteArrayInputStream(decodeBytes(str));
                    ObjectInputStream objStream = new ObjectInputStream(serialObj);
                    return objStream.readObject();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public static String encodeBytes(byte[] bytes) {
                StringBuffer strBuf = new StringBuffer();

                for (int i = 0; i < bytes.length; i++) {
                    strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
                    strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
                }

                return strBuf.toString();
            }

            public static byte[] decodeBytes(String str) {
                byte[] bytes = new byte[str.length() / 2];
                for (int i = 0; i < str.length(); i+=2) {
                    char c = str.charAt(i);
                    bytes[i/2] = (byte) ((c - 'a') << 4);
                    c = str.charAt(i+1);
                    bytes[i/2] += (c - 'a');
                }
                return bytes;
            }

        }

    }
