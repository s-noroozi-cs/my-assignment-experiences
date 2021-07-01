package com.backbase.config.init;

import com.backbase.model.entity.ServiceKey;
import com.backbase.model.repository.ServiceKeyRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Component
public class GenerateServiceKey {
    private static final Logger logger = LoggerFactory.getLogger(GenerateServiceKey.class);

    private ServiceKeyRepo repo;

    @Autowired
    public GenerateServiceKey(ServiceKeyRepo repo) {
        this.repo = repo;
    }

    void generateServiceKey() {
        logger.info("try to generate service key for test scope");

        BiFunction<String, String, ServiceKey> makeServiceKey = (key, owner) -> {
            ServiceKey entity = new ServiceKey();
            entity.setExpirationTime(LocalDateTime.now().plusDays(3));
            entity.setKey(key);
            entity.setOwner(owner);
            return entity;
        };


        ServiceKey devKey = repo.save(makeServiceKey.apply("developer_key","Developer"));
        logger.info("Save service key for developer team with id: " + devKey.getId());

        ServiceKey testKey = repo.save(makeServiceKey.apply("tester_key","Tester"));
        logger.info("Save service key for test team with id: " + testKey.getId());

        ServiceKey leaderKey = repo.save(makeServiceKey.apply("leader_key","Leader"));
        logger.info("Save service key for leading team with id: " + leaderKey.getId());

    }


}
