package com.backbase.rest.controller;

import com.backbase.annotation.AuthorizationChecker;
import com.backbase.model.confg.ServiceAccessNames;
import com.backbase.model.entity.Movie;
import com.backbase.model.entity.MovieReview;
import com.backbase.model.service.MovieService;
import com.backbase.model.service.ReviewService;
import com.backbase.rest.dto.*;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    private ReviewService reviewService;
    private MovieService movieService;

    @Autowired
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/movies",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @AuthorizationChecker(serviceAccessName = ServiceAccessNames.SEARCH_MOVIES)
    public MovieResponse moviesSearch(@RequestParam(name = "t", defaultValue = "") String title) {
        Optional<Movie> movie = movieService.findBestPictureByMovieTitle(title).stream().findFirst();
        return new MovieResponse(movie);
    }

    @RequestMapping(value = "/movies/{movieId}/reviews",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = {"application/json", "application/xml"})
    @AuthorizationChecker(serviceAccessName = ServiceAccessNames.RATING_MOVIES)
    public MovieReviewResponse movieRating(@PathVariable("movieId") long movieId,
                                           @RequestBody MovieReviewRequest reviewRequest) {
        MovieReviewResponse response = new MovieReviewResponse();

        Optional<Movie> movie = movieService.findMovieById(movieId);

        if (movie.isPresent()) {
            MovieReview entity = new MovieReview();
            entity.setMovie(new Movie(movieId));
            entity.setRating(reviewRequest.getRating());
            reviewService.saveReview(entity);

            response.setId(entity.getId());
            response.setRating(entity.getRating());
            response.setResponse(true);
        } else {
            logger.warn("Does not find any movie with id: " + movieId);
        }
        return response;
    }

    @RequestMapping(value = "/movies/top-rated",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @AuthorizationChecker(serviceAccessName = ServiceAccessNames.VIEW_TOP_RATED_MOVIES)
    public TopRatedMovieResponse topRatedMovies(@RequestParam(name = "l", defaultValue = "10") String limit) {
        int topRatedLimit = Try.of(() -> Integer.parseInt(limit)).getOrElse(10);
        List<MovieDto> movies = movieService.TopRatedMovies(topRatedLimit);
        TopRatedMovieResponse response = new TopRatedMovieResponse();
        response.setResponse(movies != null && movies.size() > 0);
        response.setMovies(movies);
        return response;
    }
}
