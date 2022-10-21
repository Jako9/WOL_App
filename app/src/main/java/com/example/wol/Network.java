package com.example.wol;

import android.util.Log;

import java.io.IOException;

import okhttp3.*;
public class Network {
    private static final OkHttpClient client = new OkHttpClient();

    public static void sendGetRequest(String ip, int port,String mac, String key, String format) {
        Request request = buildRequest(RequestType.GET,ip,port,mac,key,format);
        processGetRequest(request);
    }

    public static void sendPostRequest(String ip, int port,String mac, String key, String format) {
        Request request = buildRequest(RequestType.POST,ip,port,mac,key,format);
        processPostRequest(request);
    }

    private static void processPostRequest(Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Log.d("Response", myResponse);
                }
            }
        });
    }

    private static void processGetRequest(Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Log.d("Response", myResponse);
                }
            }
        });
    }

    public static Request buildRequest(RequestType type, String ip, int port,String mac, String key, String format) {
        if(type == RequestType.GET) {
            return new Request.Builder()
                    .url(ip + ":" + port + "?mac=" + mac)
                    .header("Authorization", key)
                    .header("Content-Type", format)
                    .build();
        }
        else if(type == RequestType.POST) {
            String postBody = "{\"mac\":\"" + mac + "\"}";
            return new Request.Builder()
                    .url(ip + ":" + port)
                    .header("Authorization", key)
                    .header("Content-Type", format)
                    .post(RequestBody.create(postBody, MediaType.parse(format)))
                    .build();
        }
        else {
            return null;
        }

    }
}
