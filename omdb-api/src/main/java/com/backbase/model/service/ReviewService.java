package com.backbase.model.service;

import com.backbase.model.entity.MovieReview;
import com.backbase.model.repository.MovieReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private MovieReviewRepo reviewRepo;

    @Autowired
    public void setReviewRepo(MovieReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public void saveReview(MovieReview entity) {
        reviewRepo.save(entity);
    }
}
