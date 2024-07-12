package com.java.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomlyStrategy implements LoadBalancingStrategy {

    @Override
    public ServerInstance get(Map<String, ServerInstance> repository) {
        if (repository == null || repository.size() == 0)
            throw new IllegalArgumentException("The repository is empty");
        List<ServerInstance> instances = new ArrayList<>(repository.values());
        int index = new Random().nextInt(repository.size()) ;//0 till last item
        return instances.get(index);
    }
}
