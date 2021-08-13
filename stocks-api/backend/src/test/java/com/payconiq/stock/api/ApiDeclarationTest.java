package com.payconiq.stock.api;

import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.config.TestOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;


@TestOrder(2)
public class ApiDeclarationTest extends ApiCommon {
    @BeforeEach
    public void init() {
        jdbcTemplate.execute("delete from tb_stock");
        jdbcTemplate.execute("INSERT INTO TB_STOCK(ID,NAME,CURRENT_PRICE,CREATE_TIME) " +
                "VALUES(1,'PAYCONIQ',100.25,NOW());");
    }

    @Test
    public void define_api_signature_to_get_list_of_all_stocks_test() throws Exception {
        ResponseEntity<String> result = getRestTemplate().getForEntity(new URI(getBaseStocksApiUrl()), String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue(), "check http status code");
        Assertions.assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType(), "check response content type");
    }

    @Test
    public void get_list_of_all_stocks_paging_test() throws Exception {
        URI serviceUri = new URI(getBaseStocksApiUrl() + "?page=0&size=10");
        PageRequest pageRequest = PageRequest.of(1, 10);
        ResponseEntity<String> result = getRestTemplate().getForEntity(serviceUri, String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue(), "check http status code");
        Assertions.assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType(), "check response content type");
    }

    @Test
    public void define_api_signature_to_get_specific_stock() throws Exception {
        URI uri = new URI(getBaseStocksApiUrl() + "/" + 1);
        ResponseEntity<String> result = getRestTemplate().getForEntity(uri, String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue(), "check http status code");
        Assertions.assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType(), "check response content type");
    }

    @Test
    public void define_api_signature_to_update_specific_stock() throws Exception {
        URI uri = new URI(getBaseStocksApiUrl() + "/" + 1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> body = new HttpEntity(StockRequest.builder()
                .name("name")
                .currentPrice(BigDecimal.ZERO)
                .build(), headers);
        ResponseEntity<String> result = getRestTemplate().exchange(uri, HttpMethod.PUT, body, String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue(), "check http status code");
        Assertions.assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType(), "check response content type");
    }


    @Test
    public void define_api_signature_to_creat_new_stock() throws Exception {
        URI uri = new URI(getBaseStocksApiUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> body = new HttpEntity(StockRequest.builder()
                .name("name")
                .currentPrice(BigDecimal.ZERO)
                .build(), headers);
        ResponseEntity<String> result = getRestTemplate().exchange(uri, HttpMethod.POST, body, String.class);
        Assertions.assertEquals(201, result.getStatusCodeValue(), "check http status code");
        Assertions.assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType(), "check response content type");
    }

    @Test
    public void define_api_to_delete_specific_stock() throws Exception {
        URI uri = new URI(getBaseStocksApiUrl() + "/1");
        ResponseEntity<String> result = getRestTemplate().exchange(uri, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        Assertions.assertEquals(204, result.getStatusCodeValue(), "check http status code");
        Assertions.assertNull(result.getBody());
    }


}
