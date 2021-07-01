package com.backbase.rest.dto;

import com.backbase.model.entity.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import io.vavr.control.Try;

import javax.xml.bind.annotation.XmlType;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@XmlType(propOrder = {"id", "year", "title", "additionalInfo", "won", "boxOffice"})
public class MovieDto {
    @JsonInclude(NON_NULL)
    private Long id;

    @JsonInclude(NON_NULL)
    private String year;

    @JsonInclude(NON_NULL)
    private String title;

    @JsonInclude(NON_NULL)
    private String additionalInfo;

    @JsonInclude(NON_NULL)
    private String won;

    @JsonInclude(NON_NULL)
    private String boxOffice = "N/A";

    public MovieDto() {
    }

    public MovieDto(Optional<Movie> movie) {
        if (movie.isPresent()) {
            this.title = movie.get().getTitle();
            this.year = movie.get().getYear();
            this.additionalInfo = movie.get().getAdditionalInfo();
            this.won = movie.get().getWon();
            this.id = movie.get().getId();
        }
    }

    public MovieDto(Movie movie) {
        this(Optional.ofNullable(movie));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }

    public Long getId() {
        return id;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
