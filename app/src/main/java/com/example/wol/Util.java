package com.example.wol;

public class Util {
    private static String DEFAULT_IP;

    private static int DEFAULT_PORT;
    private static String KEY;
    private static final String FORMAT = "application/json";

    private static int devices = 0;
    private static MainActivity mainActivity;

    public static String DEFAULT_IP() {
        return DEFAULT_IP;
    }

    public static int DEFAULT_PORT() {
        return DEFAULT_PORT;
    }

    public static String DEFAULT_KEY() {
        return KEY;
    }

    public static String DEFAULT_FORMAT() {
        return FORMAT;
    }

    public static void setDefaultIp(String ip) {
        DEFAULT_IP = ip;
    }

    public static void setDefaultPort(int port) {
        DEFAULT_PORT = port;
    }

    public static void setDefaultKey(String key) {
        KEY = key;
    }

    public static void startHTTPCall(int initDevices, MainActivity initMainActivity) {
        devices = initDevices;
        mainActivity = initMainActivity;
    }

    public static void endHTTPCall(){
        devices--;
        if(devices == 0){
            mainActivity.disableLoading();
        }
    }


}
