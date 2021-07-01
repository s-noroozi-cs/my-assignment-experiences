package com.backbase.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@XmlRootElement(name = "movies")
public class TopRatedMovieResponse {
    private boolean response;

    @JsonInclude(NON_NULL)
    private List<MovieDto> movies;

    public TopRatedMovieResponse() {
    }

    public TopRatedMovieResponse(List<MovieDto> movies) {
        this.movies = movies;
    }

    @XmlAttribute(name = "response")
    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    @XmlElement(name = "movie")
    public List<MovieDto> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDto> movies) {
        this.movies = movies;
    }
}
