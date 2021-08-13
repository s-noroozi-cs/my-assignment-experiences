package com.payconiq.stock;

import com.payconiq.stock.entity.StockEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeid Noroozi
 * Define cucumber step definition for stock list api feature
 * this api is pageable ap, because list of stock growth quickly and
 * fetch all of them have performance issue
 */
public class PagingStocksFeatureStepDefTest extends SpringIntegrationTest {
    private ResponseEntity<String> responseEntity;

    @Given("^We have following stocks$")
    public void init(DataTable table) {
        List<List<String>> rows = table.asLists(String.class);
        List<StockEntity> entities = new ArrayList<>();

        for (List<String> columns : rows) {
            entities.add(StockEntity.builder()
                    .name(columns.get(0))
                    .currentPrice(BigDecimal.valueOf(Double.valueOf(columns.get(1))))
                    .build());
        }
        stockRepository.deleteAll();
        stockRepository.saveAll(entities);
    }

    @When("^the client calls GET (.+)$")
    public void send_request_to_fetch_all_stocks(String url) {
        responseEntity = executeGet(url);
    }

    @Then("^the client receives status code of (.+)$")
    public void control_response_status_code(int statusCode) {
        Assert.assertEquals(statusCode, responseEntity.getStatusCodeValue());
    }

    @And("^the client receives (\\d+) stocks$")
    public void control_response_stock_size(int stockCount) throws Exception {
        JSONObject js = new JSONObject(responseEntity.getBody());
        Assert.assertEquals(js.getJSONArray("content").length(), stockCount);
    }

    @When("^then client call with page size and sort by GET (.+)$")
    public void send_request_with_page_size_and_sort(String url) {
        responseEntity = executeGet(url);
    }


    @And("^the client receives \"(\\w+)\" and \"(\\w+)\" and \"(\\w+)\"$")
    public void control_response_stock_list_size_for_three_highest_price_stocks(String stock1, String stock2, String stock3) throws Exception {
        JSONObject js = new JSONObject(responseEntity.getBody());
        JSONArray jsArr = js.getJSONArray("content");

        Assert.assertEquals(stock1, jsArr.getJSONObject(0).getString("name"));
        Assert.assertEquals(stock2, jsArr.getJSONObject(1).getString("name"));
        Assert.assertEquals(stock3, jsArr.getJSONObject(2).getString("name"));
    }

    @When("^then client call only one page with size 1 that sort price trough GET (.+)$")
    public void send_request_for_lowest_price_stock(String url) {
        responseEntity = executeGet(url);
    }

    @And("^the client receives \"(\\w+)\"$")
    public void control_response_stock_for_lowest_price_stocks(String stock) throws Exception {
        JSONObject js = new JSONObject(responseEntity.getBody());
        JSONArray jsArr = js.getJSONArray("content");
        Assert.assertEquals(1, jsArr.length());
        Assert.assertEquals(stock, jsArr.getJSONObject(0).getString("name"));
    }
}
