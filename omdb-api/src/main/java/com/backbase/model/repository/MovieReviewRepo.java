package com.backbase.model.repository;

import com.backbase.model.entity.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieReviewRepo extends JpaRepository<MovieReview, Long> {
}
