package com.java.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements LoadBalancingStrategy {
    private AtomicInteger index = new AtomicInteger(-1);

    @Override
    public ServerInstance get(Map<String, ServerInstance> repository) {
        List<ServerInstance> instances = new ArrayList<>(repository.values());
        return instances.get(index.incrementAndGet() % instances.size());
    }
}
