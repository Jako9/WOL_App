package com.example.wol;

import java.io.Serializable;

public class Device implements Serializable {

    private String name;
    private String mac;
    private String ip;
    private String key;
    private int port;


    public Device(String name, String mac, String ip,int port, String key) {
        this.name = name;
        this.mac = mac;
        this.ip = ip;
        this.key = key;
        this.port = port;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
