package com.backbase.config.init;

import com.backbase.model.confg.ServiceAccessNames;
import com.backbase.model.entity.ServiceAccess;
import com.backbase.model.entity.ServiceKey;
import com.backbase.model.repository.ServiceAccessRepo;
import com.backbase.model.repository.ServiceKeyRepo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AssignAccessToServiceKey {
    private static final Logger logger = LoggerFactory.getLogger(AssignAccessToServiceKey.class);
    private static final Gson gson = new Gson();

    private ServiceKeyRepo serviceKeyRepo;
    private ServiceAccessRepo serviceAccessRepo;

    @Autowired
    public AssignAccessToServiceKey(ServiceKeyRepo serviceKeyRepo,
                                    ServiceAccessRepo serviceAccessRepo
    ) {
        this.serviceKeyRepo = serviceKeyRepo;
        this.serviceAccessRepo = serviceAccessRepo;
    }

    void assignAccessToKey() {
        logger.info("Try to assign access to service key");

        try {

            ServiceKey developer = null, tester = null, leader = null;
            for (ServiceKey key : serviceKeyRepo.findAll()) {
                switch (key.getOwner()) {
                    case "Developer":
                        developer = key;
                        break;
                    case "Tester":
                        tester = key;
                        break;
                    case "Leader":
                        leader = key;
                        break;
                }
            }

            ServiceAccess searchMovie = null, rateMovie = null, viewTopRate = null;
            for (ServiceAccess access : serviceAccessRepo.findAll()) {
                if (ServiceAccessNames.SEARCH_MOVIES.getValue().equalsIgnoreCase(access.getAccessName())) {
                    searchMovie = access;
                } else if (ServiceAccessNames.RATING_MOVIES.getValue().equalsIgnoreCase(access.getAccessName())) {
                    rateMovie = access;
                } else if (ServiceAccessNames.VIEW_TOP_RATED_MOVIES.getValue().equalsIgnoreCase(access.getAccessName())) {
                    viewTopRate = access;
                }
            }

            developer.setServiceAccesses(List.of(searchMovie));
            serviceKeyRepo.save(developer);
            logger.info("Developer " + gson.toJson(developer));

            tester.setServiceAccesses(List.of(searchMovie,rateMovie));
            serviceKeyRepo.save(tester);
            logger.info("Tester" + gson.toJson(tester));

            leader.setServiceAccesses(List.of(searchMovie,rateMovie,viewTopRate));
            serviceKeyRepo.save(leader);
            logger.info("Leader" + gson.toJson(leader));

        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
