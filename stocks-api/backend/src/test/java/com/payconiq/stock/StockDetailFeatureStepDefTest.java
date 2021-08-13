package com.payconiq.stock;

import com.payconiq.stock.entity.StockEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeid Noroozi
 * Define cucumber step definition for stock list api feature
 * this api is pageable ap, because list of stock growth quickly and
 * fetch all of them have performance issue
 */
public class StockDetailFeatureStepDefTest extends SpringIntegrationTest {
    private ResponseEntity<String> responseEntity;

    @Given("^assume following stocks$")
    public void assume_stocks(DataTable table) {
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

    @When("^call service with (.+)$")
    public void call_service(String url) {
        responseEntity = executeGet(url);
    }

    @Then("^response status code is (.+)$")
    public void check_response_status_code(int statusCode) {
        Assert.assertEquals(statusCode, responseEntity.getStatusCodeValue());
    }

    @And("^response stock's name is (.+)$")
    public void check_response_stock_name(String stockName) throws Exception {
        if (!"---".equals(stockName)) {
            JSONObject js = new JSONObject(responseEntity.getBody());
            Assert.assertEquals(stockName, js.getString("name"));
        }
    }

}
