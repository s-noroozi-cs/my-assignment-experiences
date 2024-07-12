package com.java.load_balancer;

import java.util.Map;

public interface LoadBalancingStrategy {
    ServerInstance get(Map<String,ServerInstance> repository);
}
