package com.backbase.rest.dto;

import com.backbase.model.entity.Movie;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Optional;

@XmlRootElement(name = "movie")
public class MovieResponse extends MovieDto {


    private boolean response;

    public MovieResponse() {
    }


    public MovieResponse(Optional<Movie> movie) {
        super(movie);
        this.response = movie.isPresent();
    }

    @XmlAttribute(name = "response")
    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
