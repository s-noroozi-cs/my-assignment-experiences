package com.payconiq.stock;

import com.payconiq.stock.api.model.StockResponse;
import com.payconiq.stock.entity.StockEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeid Noroozi
 * Define cucumber step definition for updating existing stock api feature
 */
public class UpdateExistingStockFeatureStepDefTest extends SpringIntegrationTest {

    private ResponseEntity<String> responseEntity;

    @Given("^I have the following stocks in system$")
    public void init(DataTable table) {
        List<List<String>> rows = table.asLists(String.class);
        List<StockEntity> entities = new ArrayList<>();

        for (List<String> columns : rows) {
            entities.add(StockEntity.builder()
                    .id(Long.parseLong(columns.get(0)))
                    .name(columns.get(1))
                    .currentPrice(BigDecimal.valueOf(Double.valueOf(columns.get(2))))
                    .createTime(LocalDateTime.now())
                    .build());
        }
        stockRepository.deleteAll();
        //to save stocks with specific identifier , jpa repository generate id based on annotation of id field
        batch_insert_wit_id(entities);
    }

    @When("^to update existing stock call service with method: (\\w+) url: (.+) content-type: (.*) and body: (.*)$")
    public void call_service(String method, String url, String contentType, String body) {
        responseEntity = execute(method, url, contentType, body);
    }

    @Then("^status code of response is (\\d+)$")
    public void validate_response_status(int responseStatusCode) {
        Assert.assertEquals(responseStatusCode, responseEntity.getStatusCodeValue());
    }

    @And("^if (\\d+) equal 200 then validate response last updating time$")
    public void validate_response(int statusCode) throws Exception {
        if (statusCode == 200) {
            StockResponse stockResponse = objectMapper.readValue(responseEntity.getBody(), StockResponse.class);

            Assert.assertNotNull(stockResponse);
            Assert.assertTrue(stockResponse.getId() > 0);

            Assert.assertNotNull(stockResponse.getCreateTime());
            Assert.assertTrue(stockResponse.getCreateTime().isBefore(LocalDateTime.now()));

            Assert.assertNotNull(stockResponse.getLastUpdate());
            Assert.assertTrue(stockResponse.getLastUpdate().isBefore(LocalDateTime.now()));
        }
    }
}
