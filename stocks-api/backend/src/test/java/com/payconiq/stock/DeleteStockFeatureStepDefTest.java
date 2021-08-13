package com.payconiq.stock;

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
 * Define cucumber step definition for delete existng stock feature
 */
public class DeleteStockFeatureStepDefTest extends SpringIntegrationTest {
    private ResponseEntity<String> response;

    @Given("^I have the following stocks in system for deletion$")
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
        batch_insert_wit_id(entities);
    }

    @When("^to delete existing stock call service with method: (\\w+) url: (.+)$")
    public void call_service(String method, String url) {
        response = execute(method, url);
    }

    @Then("^after delete, status code of response is (\\d+)$")
    public void check_response_status_code(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCodeValue());
    }

    @And("^stock with (\\d+) should not exist any more$")
    public void check_stock_does_not_exist(long stockId) {
        Assert.assertTrue(stockRepository.findById(stockId).isEmpty());
    }
}