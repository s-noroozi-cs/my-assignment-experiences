package com.backbase.model.service;

import com.backbase.exception.CircuitBreakerException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResilienceWrapperService {
    private static final Logger logger = LoggerFactory.getLogger(ResilienceWrapperService.class);

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final Map<String, CircuitBreaker> circuitBreakerMap = new ConcurrentHashMap<>();

    public ResilienceWrapperService() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .permittedNumberOfCallsInHalfOpenState(3)
                .minimumNumberOfCalls(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .recordExceptions(CircuitBreakerException.class)
                .build();
        circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

    private CircuitBreaker getCircuitBreaker(String serviceName) {
        return circuitBreakerMap.computeIfAbsent(serviceName,
                name -> {
                    CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(name);
                    cb.getEventPublisher().onEvent(e -> {
                        logger.info(e.toString());
                    });
                    return cb;
                });
    }

    public void decorateTaskAction(String serviceName, Runnable task) {
        Try.run(() -> getCircuitBreaker(serviceName).executeRunnable(task));
    }


}
