package com.java.load_balancer;

public class ServerInstance {
    private String address;

    public ServerInstance(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
