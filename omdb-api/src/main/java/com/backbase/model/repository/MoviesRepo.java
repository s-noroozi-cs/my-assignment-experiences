package com.backbase.model.repository;

import com.backbase.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepo extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCaseOrderByYearDesc(String title);

    @Query(value = "select * from tb_movies  where id in " +
            "           ( select movie_id from tb_movie_review " +
            "               group by movie_id " +
            "               order by avg(rating) " +
            "               limit ?1)",nativeQuery = true)
    List<Movie> findTopRatedMovies(int top);
}
