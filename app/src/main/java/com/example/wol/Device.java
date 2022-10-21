package com.example.wol;

import java.io.Serializable;

public class Device implements Serializable {

    private String name;
    private String mac;
    private String ip;
    private int port;


    public Device(String name, String mac) {
        this.name = name;
        this.mac = mac;
        this.ip = Util.DEFAULT_IP;
        this.port = Util.DEFAULT_PORT;
    }

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
