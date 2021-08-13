package com.payconiq.stock.api;

import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.api.model.StockResponse;
import com.payconiq.stock.config.TestOrder;
import com.payconiq.stock.exception.BadRequestException;
import com.payconiq.stock.exception.StockNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TestOrder(3)
public class ApiOperationTest extends ApiCommon {


    @BeforeEach
    public void clearAndSetTestData() throws Exception {
        jdbcTemplate.execute("delete from TB_STOCK");
        InputStream is = new ClassPathResource("db/migration/V2__IMPORT_TEST_DATA.sql").getInputStream();
        new BufferedReader(new InputStreamReader(is))
                .lines()
                .forEach(jdbcTemplate::execute);
    }

    @Test
    public void all_stock_paging_size_count_test() {
        int assumeTotalRecordThatImported = 9;
        int pageSize = 2;
        int pageNumber = 0;
        int totalPageCount = assumeTotalRecordThatImported / pageSize + assumeTotalRecordThatImported % pageSize;

        Page<StockResponse> result = stockController.getAllStocks(PageRequest.of(pageNumber, pageSize));
        Assertions.assertEquals(assumeTotalRecordThatImported, result.getTotalElements(),
                "assume data imported successfully");
        Assertions.assertEquals(totalPageCount, result.getTotalPages(),
                "assume page size: " + pageSize +
                        ", total records: " + assumeTotalRecordThatImported +
                        ", total page count: " + totalPageCount);
    }

    @Test
    public void all_stock_paging_sort_test() {
        String lastStockNameAlphabetically = "PYPL";
        PageRequest page = PageRequest.of(0, 1, Sort.Direction.DESC, "name");
        Page<StockResponse> result = stockController.getAllStocks(page);
        Assertions.assertEquals(lastStockNameAlphabetically, result.get().findFirst().get().getName(),
                "order stock name alphabetically and check fetch last ones fetch correctly");
    }

    @Test
    public void get_specific_stock_test() {
        StockResponse expectedStock = stockController.getAllStocks(PageRequest.of(0, 9)).get()
                .filter(i -> i.getName().equals("GOOG")).findFirst().get();

        StockResponse foundStock = stockController.getSpecificStock(expectedStock.getId());
        //ignore update time in equation process
        foundStock.setLastUpdate(null);
        Assertions.assertEquals(expectedStock, foundStock, "Expected stock does not match with found item");
    }

    @Test
    public void not_found_stock_test() {
        Assertions.assertThrows(StockNotFoundException.class, () -> stockController.getSpecificStock(0L),
                "Expected throw stock not found exception");
    }

    @Test
    public void update_existing_stock_test() {
        Long stockId = stockController.getAllStocks(PageRequest.of(0, 1)).get().findFirst().get().getId();

        StockRequest stockRequest = StockRequest.builder()
                .name("paging_stocks.feature")
                .currentPrice(BigDecimal.ZERO)
                .build();

        StockResponse stockBeforeUpdate = stockController.getSpecificStock(stockId);
        StockResponse stockAfterUpdate = stockController.updateSpecificStock(stockId, stockRequest);

        Assertions.assertEquals(stockBeforeUpdate.getId(), stockAfterUpdate.getId(),
                "Expected same identifier");

        Assertions.assertTrue(stockAfterUpdate.getLastUpdate().isBefore(LocalDateTime.now()),
                "Expected update time changed and after original stock update time ");

        Assertions.assertEquals(stockRequest.getName(), stockAfterUpdate.getName(),
                "Expected name attribute update successfully");

        Assertions.assertEquals(stockRequest.getCurrentPrice().toPlainString(),
                stockAfterUpdate.getCurrentPrice().toPlainString(),
                "Expected current price attribute update successfully");
    }

    @Test
    public void create_new_stock() {
        StockRequest stockRequest = StockRequest.builder()
                .name("IBM")
                .currentPrice(BigDecimal.valueOf(333.75))
                .build();
        StockResponse newStock = stockController.creatNewStock(stockRequest);
        Assertions.assertTrue(newStock.getId() > 0,
                "Expected positive identifier");
        Assertions.assertTrue(newStock.getCreateTime().isBefore(LocalDateTime.now()),
                "Expected creation time before current time");
        Assertions.assertNull(newStock.getLastUpdate(),
                "Last update value should be null");
        Assertions.assertTrue(stockRequest.getName().equals(newStock.getName())
                        && stockRequest.getCurrentPrice().toPlainString()
                        .equals(
                                newStock.getCurrentPrice().toPlainString()),
                "Name and current price Last should be had same value");
    }

    @Test
    public void delete_existing_stock() {
        Page<StockResponse> result = stockController.getAllStocks(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id")));
        StockResponse stock = result.getContent().get(0);
        Assertions.assertNotNull(stock);
        Assertions.assertTrue(stock.getId() > 0);
        stockController.deleteSpecificStock(stock.getId());
        Assertions.assertThrows(StockNotFoundException.class, () -> stockController.getSpecificStock(0L),
                "Expected throw stock not found exception");
    }

    @Test
    public void prevent_duplicate_stock_name_when_create_or_update() {
        final StockRequest stockRequest = StockRequest.builder()
                .name("IBM")
                .currentPrice(BigDecimal.valueOf(333.75))
                .build();
        StockResponse newStock = stockController.creatNewStock(stockRequest);
        Assertions.assertNotNull(newStock);
        Assertions.assertTrue(newStock.getId() > 0);

        //prevent in create scenario
        Assertions.assertThrows(BadRequestException.class, () -> stockController.creatNewStock(stockRequest));

        //prevent in update existing stock scenario
        StockResponse existStockResponse = stockController.getAllStocks(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"))).getContent().get(0);
        Assertions.assertNotEquals(newStock.getName(), existStockResponse.getName());
        Assertions.assertThrows(BadRequestException.class, () ->
                stockController.updateSpecificStock(existStockResponse.getId(), stockRequest));
    }
}
