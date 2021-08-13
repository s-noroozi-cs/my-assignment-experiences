package com.payconiq.stock.api;

import com.payconiq.stock.api.controller.StockController;
import com.payconiq.stock.api.handler.RestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

public abstract class ApiCommon {
    public static int SERVER_PORT;
    public static StockController stockController;
    public static JdbcTemplate jdbcTemplate;


    private final RestTemplate restTemplate;

    public ApiCommon() {
        restTemplate = new RestTemplateBuilder().errorHandler(new RestTemplateResponseErrorHandler()).build();
    }


    protected String getBaseStocksApiUrl() {
        return "http://localhost:" + SERVER_PORT + "/api/stocks";
    }

    protected String getHostUrl() {
        return "http://localhost:" + SERVER_PORT;
    }


    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }


}
