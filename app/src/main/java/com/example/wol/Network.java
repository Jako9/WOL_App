package com.example.wol;

import static com.example.wol.Util.*;

import android.util.Log;

import java.io.IOException;

import okhttp3.*;
public class Network {
    private static final OkHttpClient client = new OkHttpClient();

    public static void sendGetRequest(Device device, CallbackStatus callback) {
        String ip = device.getIp().equals("") ? DEFAULT_IP() : device.getIp();
        int port = device.getPort() == 0 ? DEFAULT_PORT() : device.getPort();
        String key = device.getKey().equals("") ? DEFAULT_KEY() : device.getKey();
        Request request = buildRequest(RequestType.GET,ip,port,device.getMac(),key,DEFAULT_FORMAT());
        if(request == null){
            callback.onFailure("Invalid request");
            return;
        }
        processGetRequest(request, callback);
    }

    public static void sendPostRequest(Device device, CallbackStatus callback) {
        String ip = device.getIp().equals("") ? DEFAULT_IP() : device.getIp();
        int port = device.getPort() == 0 ? DEFAULT_PORT() : device.getPort();
        String key = device.getKey().equals("") ? DEFAULT_KEY() : device.getKey();
        Request request = buildRequest(RequestType.POST,ip,port,device.getMac(),key,DEFAULT_FORMAT());
        if(request == null){
            callback.onFailure("Invalid request");
            return;
        }
        processPostRequest(request, callback);
    }

    private static void processPostRequest(Request request, CallbackStatus callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    callback.onSuccess(myResponse);
                    return;
                }
                callback.onFailure(response.message());
            }
        });
    }

    private static void processGetRequest(Request request, CallbackStatus callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    callback.onSuccess(myResponse);
                    return;
                }
                callback.onFailure(response.message());
            }
        });
    }

    public static Request buildRequest(RequestType type, String ip, int port,String mac, String key, String format) {
        try {
            if (type == RequestType.GET) {
                return new Request.Builder()
                        .url(ip + ":" + port + "?mac=" + mac)
                        .header("Authorization", key)
                        .header("Content-Type", format)
                        .build();
            } else if (type == RequestType.POST) {
                String postBody = "{\"mac\":\"" + mac + "\"}";
                return new Request.Builder()
                        .url(ip + ":" + port)
                        .header("Authorization", key)
                        .header("Content-Type", format)
                        .post(RequestBody.create(postBody, MediaType.parse(format)))
                        .build();
            } else {
                return null;
            }
        }
        catch (Exception e) {
            Log.e("Error", "Error while building request: " + e.getMessage());
            return null;
        }

    }
}
