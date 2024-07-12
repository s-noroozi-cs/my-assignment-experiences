package com.java.sample;

import java.util.Map;

public interface LoadBalancingStrategy {
    ServerInstance get(Map<String,ServerInstance> repository);
}
