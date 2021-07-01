package com.backbase.config.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Initializer {
    private CSVImporter csvImporter;
    private GenerateRandomRatingReview randomRatingReview;
    private ImportServiceAccess serviceAccess;
    private GenerateServiceKey serviceKey;
    private AssignAccessToServiceKey accessToServiceKey;

    @Autowired
    public Initializer(CSVImporter csvImporter,
                       GenerateRandomRatingReview randomRatingReview,
                       ImportServiceAccess serviceAccess,
                       GenerateServiceKey serviceKey,
                       AssignAccessToServiceKey accessToServiceKey) {
        this.csvImporter = csvImporter;
        this.randomRatingReview = randomRatingReview;
        this.serviceAccess = serviceAccess;
        this.serviceKey = serviceKey;
        this.accessToServiceKey = accessToServiceKey;
    }

    @PostConstruct
    public void init() {
        csvImporter.importData();
        randomRatingReview.generateRandomReviewRate();
        serviceAccess.importServiceAccess();
        serviceKey.generateServiceKey();
        accessToServiceKey.assignAccessToKey();
    }


}
