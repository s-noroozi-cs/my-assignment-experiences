package com.java.load_balancer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleLoadBalancer implements LoadBalancer {
    private final Map<String, ServerInstance> repository = new ConcurrentHashMap<>();
    private LoadBalancingStrategy strategy;

    public SimpleLoadBalancer(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    public SimpleLoadBalancer() {
    }

    @Override
    public ServerInstance getServerInstance() {
        return strategy.get(repository);
    }

    @Override
    public ServerInstance registration(String address) {
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Address is not valid");
        if (repository.size() < MAX_LOAD_BALANCER) {
            synchronized (repository) {
                if (repository.get(address.trim()) == null) {
                    if (repository.size() < MAX_LOAD_BALANCER) {
                        ServerInstance instance = new ServerInstance(address);
                        repository.put(address.trim(), instance);
                        return instance;
                    }
                } else {
                    throw new DuplicationAddressException();
                }
            }
        }
        throw new LoadBalancerFullException();
    }
}
