package com.hobby.crawler.search;

public interface SearchEngine {
    String search(String searchTerm);

    static SearchEngine getGoogleSearchEngine() {
        return new GoogleSearchEngine();
    }
}
