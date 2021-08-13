package com.payconiq.stock;

import com.payconiq.stock.api.model.StockResponse;
import com.payconiq.stock.entity.StockEntity;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Saeid Noroozi
 * Define cucumber step definition for create new stock feature
 */
public class CreateStockFeatureStepDefTest extends SpringIntegrationTest {

    private ResponseEntity<String> response;

    private String contentType;

    @Given("^there are exist only \"(\\w+)\" and \"(\\w+)\" stocks and call service to create,api with content type is (.+)$")
    public void init(String stockA, String stockB, String contentType) {
        //remove all existing data
        jdbcTemplate.execute("delete from tb_stock");

        //persis stock A
        stockRepository.saveAndFlush(StockEntity.builder()
                .name(stockA)
                .currentPrice(BigDecimal.ONE)
                .createTime(LocalDateTime.now())
                .build());

        //persis stock B
        stockRepository.saveAndFlush(StockEntity.builder()
                .name(stockB)
                .currentPrice(BigDecimal.ONE)
                .createTime(LocalDateTime.now())
                .build());
        this.contentType = contentType;
    }

    @When("^to create stock call service with method: (\\w.+) url: (.+) and body: (.*)$")
    public void call_service(String method, String url, String body) {
        response = execute(method, url, contentType, body);
    }

    @Then("^header status code is (\\d.+)$")
    public void check_response_status_code(int statusCode) throws Exception {
        Assert.assertEquals(statusCode, response.getStatusCodeValue());
    }

    @And("^control and validate response with (\\w+) and (.+)$")
    public void validate_response_body(boolean success, String statusMsg) throws Exception {
        if (success) {
            StockResponse stockResponse = objectMapper.readValue(response.getBody(), StockResponse.class);
            Assert.assertTrue(stockResponse.getId() > 0);
            Assert.assertNotNull(stockResponse.getCreateTime());
            Assert.assertTrue(stockResponse.getCreateTime().isBefore(LocalDateTime.now()));
        } else {
            Assert.assertEquals(statusMsg, getResponseField("status"));
        }
    }

    private String getResponseField(String fieldName) throws Exception {
        JSONObject js = new JSONObject(response.getBody());
        return js.getString(fieldName);
    }
}
