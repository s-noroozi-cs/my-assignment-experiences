package com.backbase.model.service;

import com.backbase.model.entity.Movie;
import com.backbase.model.repository.MoviesRepo;
import com.backbase.rest.dto.MovieDto;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private MoviesRepo moviesRepo;
    private ResilienceWrapperService resilienceWrapperService;
    private ExecutorService omdbApiExecService;
    private OmdbApiService omdbApiService;

    @Autowired
    public void setOmdbApiExecService(@Value("${omdbapi.search.waitTime:250}") int waitTime,
                                      @Value("${omdbapi.search.serviceTime:50}") int serviceTime) {
        /*
        IO Bound Task:
            threads = number of cores * (1 + wait time / service time)
                service time: 50 ms
                wait time: 250 ms
            threads = number of cores * (1 + 5)
    */
        this.omdbApiExecService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * (1 + waitTime / serviceTime));
    }

    @Autowired
    public void setResilienceWrapperService(ResilienceWrapperService resilienceWrapperService) {
        this.resilienceWrapperService = resilienceWrapperService;
    }

    @Autowired
    public void setOmdbApiService(OmdbApiService omdbApiService) {
        this.omdbApiService = omdbApiService;
    }

    @Autowired
    public void setMoviesRepo(MoviesRepo moviesRepo) {
        this.moviesRepo = moviesRepo;
    }


    public List<Movie> findBestPictureByMovieTitle(String title) {
        return moviesRepo.findByTitleContainingIgnoreCaseOrderByYearDesc(title);
    }

    @Transactional
    public void saveMovie(List<Movie> movies) {
        moviesRepo.saveAll(movies);
    }

    public Optional<Movie> findMovieById(long id) {
        return moviesRepo.findById(id);
    }

    public List<MovieDto> TopRatedMovies(int top) {
        List<Movie> movies = moviesRepo.findTopRatedMovies(top);
        List<MovieDto> movieDtos = movies.stream().map(MovieDto::new).collect(Collectors.toList());
        movieDtos.stream()
                .map(this::makeResilientTask)
                .map(omdbApiExecService::submit)
                .forEach(this::waitUntilDown);
        movieDtos.sort(sortByBoxOfficeValueDesc());
        return movieDtos;
    }

    private Comparator<MovieDto> sortByBoxOfficeValueDesc() {
        return (a, b) -> Long.compare(getBoxOfficeValueAsLong(b), getBoxOfficeValueAsLong(a));
    }

    private long getBoxOfficeValueAsLong(MovieDto dto) {
        return Try.of(() ->
                Optional.ofNullable(dto)
                        .map(MovieDto::getBoxOffice)
                        .filter(Objects::nonNull)
                        .filter(Predicate.not(String::isBlank))
                        .map(i -> i.substring(1))
                        .map(i -> i.replaceAll(",", ""))
                        .map(Long::parseLong)
                        .orElse(0L)
        ).getOrElse(0L);
    }

    private void waitUntilDown(Future future) {
        try {
            while (!future.isDone())
                Try.run(() -> Thread.sleep(10));
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private Runnable makeResilientTask(MovieDto dto) {
        String title = dto.getTitle();
        String year = Try.of(() -> dto.getYear().substring(0, 4)).getOrElse("");
        return () -> resilienceWrapperService.decorateTaskAction(
                "OMDB-API-CALL",
                () -> dto.setBoxOffice(omdbApiService.searchMovieForBoxOfficeValue(title, year)));
    }


    public List<Movie> ListAllMovies() {
        return moviesRepo.findAll();
    }


}
