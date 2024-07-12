package com.java.sample;

public interface LoadBalancer {
    public static final int MAX_LOAD_BALANCER = 10;

    ServerInstance registration(String address);

    ServerInstance getServerInstance();

}
