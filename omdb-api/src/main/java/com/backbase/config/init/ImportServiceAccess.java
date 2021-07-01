package com.backbase.config.init;

import com.backbase.model.confg.ServiceAccessNames;
import com.backbase.model.entity.ServiceAccess;
import com.backbase.model.repository.ServiceAccessRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportServiceAccess {
    private static final Logger logger = LoggerFactory.getLogger(GenerateServiceKey.class);


    private ServiceAccessRepo repository;

    @Autowired
    public void setRepository(ServiceAccessRepo repository) {
        this.repository = repository;
    }

    void importServiceAccess() {
        logger.info("try to import defined service access name.");
        try {
            for (ServiceAccessNames accessNames : ServiceAccessNames.values()) {
                ServiceAccess entity = new ServiceAccess();
                entity.setAccessName(accessNames.getValue());
                repository.save(entity);
                logger.info("Persist access name:" + entity.getAccessName() + ",with id: " + entity.getId());
            }
        } catch (Throwable ex) {
            logger.error("Error in importing defined access names: " + ex.getMessage(), ex);
        }
    }
}
