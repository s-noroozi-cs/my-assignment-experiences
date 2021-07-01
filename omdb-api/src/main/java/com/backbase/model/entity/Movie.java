package com.backbase.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "tb_movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "ADDITIONAL_INFO")
    private String additionalInfo;

    @Column(name = "WON")
    private String won;

    public Movie() {
    }

    public Movie(Long id) {
        this.id = id;
    }

    public Movie(String year, String title, String additionalInfo, String won) {
        this.year = year;
        this.title = title;
        this.additionalInfo = additionalInfo;
        this.won = won;
    }

    public Movie(Long id, String year, String title, String additionalInfo, String won) {
        this.id = id;
        this.year = year;
        this.title = title;
        this.additionalInfo = additionalInfo;
        this.won = won;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
