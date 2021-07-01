package com.backbase.config.init;

import com.backbase.model.entity.Movie;
import com.backbase.model.entity.MovieReview;
import com.backbase.model.service.MovieService;
import com.backbase.model.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerateRandomRatingReview {
    private static final Logger logger = LoggerFactory.getLogger(GenerateRandomRatingReview.class);

    @Value("${generate-random-review-count:0}")
    private int generateRandomReviewCount;

    private ReviewService reviewService;
    private MovieService movieService;

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }

    void generateRandomReviewRate() {
        logger.info("Prepare random rating reviews data, current state:" +
                (generateRandomReviewCount > 0 ? "enabled" : "disabled"));

        if (generateRandomReviewCount > 0) {
            List<Movie> movies = movieService.ListAllMovies();

            for (int i = 0; i < generateRandomReviewCount; i++) {
                int rating = (int) (Math.random() * 10 + 1);
                int movieIndex = (int) (Math.random() * movies.size());
                MovieReview movieReview = new MovieReview();
                movieReview.setMovie(movies.get(movieIndex));
                movieReview.setRating(rating);
                reviewService.saveReview(movieReview);
            }
        }
    }
}