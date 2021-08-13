package com.payconiq.stock.api;

import com.payconiq.stock.api.model.GlobalErrorResponse;
import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.config.TestOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@TestOrder(4)
public class ApiValidationTest extends ApiCommon {

    @Test
    public void fetch_invalid_stock_test() throws Exception {
        URI uri = new URI(getBaseStocksApiUrl() + "/fake_id_for_bad_request");
        ResponseEntity<GlobalErrorResponse> resp = getRestTemplate().getForEntity(uri, GlobalErrorResponse.class);
        response_validation_for_user_bad_request(resp, "Expected bad request");
    }

    @Test
    public void update_specific_stock_bad_structure_test() throws Exception {
        int randomStockId = (int) (Math.random() * System.currentTimeMillis());
        URI uri = new URI(getBaseStocksApiUrl() + "/" + randomStockId);
        send_request_for_bad_request_control(uri, HttpMethod.PUT, "{", "]", "[]", "{}");
    }

    @Test
    public void create_new_stock_bad_structure_test() throws Exception {
        URI uri = new URI(getBaseStocksApiUrl());
        send_request_for_bad_request_control(uri, HttpMethod.POST, "{", "]", "[]", "{}");
    }

    private void send_request_for_bad_request_control(URI uri, HttpMethod method, String... bodies) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        for (String body : bodies) {
            ResponseEntity<GlobalErrorResponse> resp =
                    getRestTemplate().exchange(uri, method, new HttpEntity(body, headers), GlobalErrorResponse.class);
            response_validation_for_user_bad_request(resp, "expected bad request response status for body:'" + body + "'");
        }
    }

    @Test
    public void update_stock_required_fields_test() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        int randomStockId = (int) (Math.random() * System.currentTimeMillis());
        URI uri = new URI(getBaseStocksApiUrl() + "/" + randomStockId);

        send_request_for_field_validation(uri, HttpMethod.PUT, make_invalid_stock_request());
    }

    @Test
    public void create_stock_required_fields_test() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        send_request_for_field_validation(new URI(getBaseStocksApiUrl()), HttpMethod.POST, make_invalid_stock_request());
    }

    private List<StockRequest> make_invalid_stock_request() {
        List<StockRequest> list = new ArrayList<>();

        //blank name
        list.add(StockRequest.builder().name("   ").build());

        //null current price value
        list.add(StockRequest.builder().name("paging_stocks.feature").build());

        //negative current price value
        list.add(StockRequest.builder().name("paging_stocks.feature").currentPrice(BigDecimal.valueOf(-1L)).build());

        return list;
    }


    private void send_request_for_field_validation(URI uri, HttpMethod method, List<StockRequest> stockRequests) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        for (StockRequest stockRequest : stockRequests) {
            ResponseEntity<GlobalErrorResponse> resp =
                    getRestTemplate().exchange(uri, method, new HttpEntity(stockRequest, headers), GlobalErrorResponse.class);
            response_validation_for_user_bad_request(resp, "control validation does not work correctly");
        }
    }

    @Test
    public void delete_invalid_stock_test() throws Exception {
        URI uri = new URI(getBaseStocksApiUrl() + "/fake_id_for_bad_request");
        ResponseEntity<GlobalErrorResponse> resp = getRestTemplate().exchange(uri,HttpMethod.DELETE,HttpEntity.EMPTY, GlobalErrorResponse.class);
        response_validation_for_user_bad_request(resp, "Expected bad request for delete operation");
    }

    private void response_validation_for_user_bad_request(ResponseEntity<GlobalErrorResponse> resp, String message) {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode(), message + " - check http status code");
    }
}
