package com.hobby.crawler.search;

import com.hobby.crawler.util.StreamUtil;
import com.hobby.crawler.util.TestDataUtil;

import java.net.URL;
import java.net.URLConnection;

public class GoogleSearchEngine implements SearchEngine {

    private String getRandomTestData() {
        try {
            return TestDataUtil.getSampleGoogleSearchResultPage();
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private String realGoogleSearch(String searchTerm) {
        try {
            URLConnection connection = new URL("https://www.google.com/search?q=" + searchTerm + "&num=20").openConnection();

            //set user agent header to simulate browser request
            connection.addRequestProperty("User-Agent","Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

            //set time out for connection and reading data,5 seconds
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            return StreamUtil.readAsString(connection.getInputStream());

        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    @Override
    public String search(String searchTerm) {
        /*
        unfortunately google blocked direct query and redirect request to "sorry page" to get captcha security.
        There are some solutions:
        1. Using google student custom api for search
            This apis are limited to only 10 search query per day and based on rest api
            I think in this test one part is extract link tags from main html result page, and this
            rest api return json that provide link in json field.

            I write simple google search to explain simple http request and response.

        return realGoogleSearch(searchTerm);

        */


        return getRandomTestData();
    }


}
