package com.backbase.model.confg;

public enum ServiceAccessNames {
    SEARCH_MOVIES("search_movies"),
    RATING_MOVIES("rating_movies"),
    VIEW_TOP_RATED_MOVIES("view_top_rated_movies");

    private String value;

    ServiceAccessNames(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
