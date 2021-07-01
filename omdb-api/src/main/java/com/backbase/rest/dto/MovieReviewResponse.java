package com.backbase.rest.dto;

import com.backbase.model.entity.MovieReview;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@XmlRootElement(name = "root")
public class MovieReviewResponse {
    @JsonInclude(NON_NULL)
    private Long id;

    @JsonInclude(NON_NULL)
    private Integer rating;

    private boolean response;

    public MovieReviewResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @XmlAttribute(name = "response")
    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
