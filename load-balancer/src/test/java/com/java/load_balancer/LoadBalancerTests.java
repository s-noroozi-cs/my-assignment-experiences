package com.java.load_balancer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancerTests {
    @Test
    void check_exist_load_balancer_imp() {
        Assertions.assertTrue(new SimpleLoadBalancer() instanceof LoadBalancer);
    }

    @Test
    void check_invalid_address() {
        LoadBalancer lb = new SimpleLoadBalancer();
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> lb.registration(null));
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> lb.registration(""));
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> lb.registration("    "));
    }

    @Test
    void check_max_registration_size() {
        LoadBalancer lb = new SimpleLoadBalancer();
        for (int i = 0; i < LoadBalancer.MAX_LOAD_BALANCER; i++)
            Assertions.assertNotNull(lb.registration(UUID.randomUUID().toString()));
        Assertions.assertThrowsExactly(LoadBalancerFullException.class,
                () -> lb.registration(UUID.randomUUID().toString()));
    }

    @Test
    void prevent_duplication_registration() {
        LoadBalancer lb = new SimpleLoadBalancer();
        String address = "127.0.0.1 ";
        Assertions.assertNotNull(lb.registration(address));
        Assertions.assertThrowsExactly(DuplicationAddressException.class,
                () -> lb.registration(address));
        Assertions.assertThrowsExactly(DuplicationAddressException.class
                , () -> lb.registration(address.concat(" ")));
    }

    @Test
    void check_randomly_load_balancer(){
        LoadBalancingStrategy lbs = new RandomlyStrategy();
        LoadBalancer lb  = new SimpleLoadBalancer(lbs);
        final String a="1",b="2",c="3";
        lb.registration(a);
        lb.registration(b);
        lb.registration(c);

        AtomicInteger counterA = new AtomicInteger(0);
        AtomicInteger counterB = new AtomicInteger(0);
        AtomicInteger counterC = new AtomicInteger(0);

        for(int i=0; i < 100; i++){
            switch (lb.getServerInstance().getAddress()){
                case a:
                    counterA.incrementAndGet();
                    break;
                case b:
                    counterB.incrementAndGet();
                    break;
                case c:
                    counterC.incrementAndGet();
                    break;
            }

        }

        Assertions.assertTrue(counterA.get() > 0);
        Assertions.assertTrue(counterB.get() > 0);
        Assertions.assertTrue(counterC.get() > 0);
    }

    @Test
    void check_round_robin_balancer() {
        LoadBalancingStrategy lbs = new RoundRobinStrategy();
        LoadBalancer lb = new SimpleLoadBalancer(lbs);
        final String a="1",b="2",c="3";
        lb.registration(a);
        lb.registration(b);
        lb.registration(c);

        Assertions.assertEquals(a, lb.getServerInstance().getAddress());
        Assertions.assertEquals(b, lb.getServerInstance().getAddress());
        Assertions.assertEquals(c, lb.getServerInstance().getAddress());

        Assertions.assertEquals(a, lb.getServerInstance().getAddress());
        Assertions.assertEquals(b, lb.getServerInstance().getAddress());
        Assertions.assertEquals(c, lb.getServerInstance().getAddress());
    }
}
