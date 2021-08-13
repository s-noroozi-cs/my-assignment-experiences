package com.payconiq.stock;

import com.payconiq.stock.api.ApiCommon;
import com.payconiq.stock.api.controller.StockController;
import com.payconiq.stock.config.TestOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;

/**
 * @author Saeid Noroozi
 * This class load spring context and preparing environment to execute junit tests.
 * To prevent multiple spring context load, define test order and
 * , set some common value that be used in other nit tests
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestOrder(1)
public class StockApiApplicationTest {
    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private StockController stockController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initWebContainer() {
        ApiCommon.SERVER_PORT = randomServerPort;
        ApiCommon.stockController = stockController;
        ApiCommon.jdbcTemplate = jdbcTemplate;
    }

    @Test
    public void load_context_without_exception() {
    }


}
