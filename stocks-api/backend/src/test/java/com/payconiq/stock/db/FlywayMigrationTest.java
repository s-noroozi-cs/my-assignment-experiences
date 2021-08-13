package com.payconiq.stock.db;

import com.payconiq.stock.api.ApiCommon;
import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.config.TestOrder;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Saeid Noroozi
 * I disabled automatically jpa-hibernate DDL, and using flyway tomanage db migration
 * In this test scenario, chack and validate flyway result
 */
@TestOrder(5)
public class FlywayMigrationTest extends ApiCommon {


    private Optional<FlywaySchemaHistory> getFlywaySchemaHistory(int version) {
        return
                jdbcTemplate.queryForObject("select * from \"flyway_schema_history\" where \"version\"=" + version,
                        (rs, rowNum) ->
                                Optional.of(FlywaySchemaHistory.builder()
                                        .version(rs.getInt("version"))
                                        .script(rs.getString("script"))
                                        .type(rs.getString("type"))
                                        .success("true".equalsIgnoreCase(rs.getString("success")))
                                        .build()));
    }

    @Test
    public void check_flyway_table_creation_ver_1() {
        Optional<FlywaySchemaHistory> createStock = getFlywaySchemaHistory(1);
        Assertions.assertTrue(createStock.isPresent(), "Flyway did not apply version 1, creat stock table");

        FlywaySchemaHistory expectedCreateStock = FlywaySchemaHistory.builder()
                .version(1)
                .script("V1__CREATE_STOCK_DB_OBJ.sql")
                .type("SQL")
                .success(true)
                .build();
        Assertions.assertEquals(expectedCreateStock, createStock.get(),
                "Flyway stock table did not apply successfully.");
    }

    @Test
    public void check_flyway_data_import_ver_2() {
        Optional<FlywaySchemaHistory> createStock = getFlywaySchemaHistory(2);
        Assertions.assertTrue(createStock.isPresent(), "Flyway did not apply version 2, import data - stocks price");

        FlywaySchemaHistory expectedCreateStock = FlywaySchemaHistory.builder()
                .version(2)
                .script("V2__IMPORT_TEST_DATA.sql")
                .type("SQL")
                .success(true)
                .build();
        Assertions.assertEquals(expectedCreateStock, createStock.get(),
                "Flyway import data - stocks prices did not apply successfully.");
    }

    @Test
    public void validate_data_import() {
        int recordCount = jdbcTemplate.queryForObject("select count(id) from TB_STOCK",
                (rs, rowNum) -> rs.getInt(1));
        Assertions.assertEquals(9, recordCount, "all record did not import completely.");

        StockRequest payconiqStock = jdbcTemplate.queryForObject("select * from TB_STOCK where name='PAYCONIQ'",
                (rs, rowNum) ->
                        StockRequest.builder()
                                .name(rs.getString("NAME"))
                                .currentPrice(rs.getBigDecimal("CURRENT_PRICE"))
                                .build()
        );
        StockRequest expectedStock = StockRequest.builder()
                .name("PAYCONIQ")
                .currentPrice(BigDecimal.valueOf(100.25))
                .build();
        Assertions.assertEquals(expectedStock, payconiqStock, "the payconiz stock did not import correctly");
    }

}
