package com.backbase.model.service;

import com.backbase.exception.OmdbApiServiceCallException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class OmdbApiService {
    private static final Logger logger = LoggerFactory.getLogger(OmdbApiService.class);
    private static final Gson gson = new Gson();

    @Value("${omdbapi.apikey:71cd0dd0}")
    private String apiKey;

    @Value("${omdbapi.host:http://www.omdbapi.com}")
    private String host;

    private HttpConnectionManager connectionManager;

    @Autowired
    public void setConnectionManager(HttpConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private String urlEncode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }


    public String searchMovieForBoxOfficeValue(String title, String year) {
        String url = host + "/?t=" + urlEncode(title) + "&r=json";

        if (year != null && !year.isBlank())
            url += "&y=" + urlEncode(year);

        //for security issue should not log api key
        logger.info(String.format("Call Service with url: %s", host + url));

        url += "&apikey=" + apiKey;

        try (CloseableHttpResponse httpResp =
                     connectionManager
                             .getCloseableHttpClient()
                             .execute(new HttpGet(url))) {

            int statusCode = httpResp.getStatusLine().getStatusCode();

            String respContent = new String(httpResp.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

            logger.info(String.format(
                    "For parameters (title:%s, year:%s), Service response --> status code: %d, response content: %s",
                    title, year, statusCode, respContent));

            JsonObject jsonObject = gson.fromJson(respContent, JsonObject.class);
            return jsonObject.get("BoxOffice").getAsJsonPrimitive().getAsString();

        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            throw new OmdbApiServiceCallException(ex.getMessage(), ex);
        }
    }
}
