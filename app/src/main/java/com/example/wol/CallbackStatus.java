package com.example.wol;

public interface CallbackStatus {

    void onSuccess(String response);
    void onFailure(String message);
}
