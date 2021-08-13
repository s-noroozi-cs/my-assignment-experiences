package com.payconiq.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stock.api.ApiCommon;
import com.payconiq.stock.entity.StockEntity;
import com.payconiq.stock.repository.StockRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Saeid Noroozi
 * This class help cucumber stepdefinition execute in spring context environment
 * and able to use spring beans
 * Also define some useful method that used in multiple cucumber scenarios (step definiton class)
 */

@CucumberContextConfiguration
@SpringBootTest(classes = StockApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringIntegrationTest extends ApiCommon {
    @LocalServerPort
    private int randomServerPort;

    @Autowired
    protected StockRepository stockRepository;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected final ObjectMapper objectMapper = new ObjectMapper()
            //to fix java 8 local date time
            .findAndRegisterModules();

    @PostConstruct
    private void initWebContainer() {
        ApiCommon.SERVER_PORT = randomServerPort;
    }

    ResponseEntity<String> executeGet(String apiUrl) {
        return getRestTemplate().getForEntity(getHostUrl() + apiUrl, String.class);
    }

    ResponseEntity<String> execute(String method, String apiUrl, String contentType, String body) {
        HttpHeaders headers = new HttpHeaders();
        Try.run(() -> headers.setContentType(MediaType.valueOf(contentType)));
        return getRestTemplate()
                .exchange(getHostUrl() + apiUrl, HttpMethod.resolve(method), new HttpEntity(String.valueOf(body), headers), String.class);
    }

    ResponseEntity<String> execute(String method, String apiUrl) {
        return getRestTemplate()
                .exchange(getHostUrl() + apiUrl, HttpMethod.resolve(method), HttpEntity.EMPTY, String.class);
    }

    protected void batch_insert_wit_id(List<StockEntity> entities){
        jdbcTemplate.batchUpdate(
                "INSERT INTO TB_STOCK(ID,NAME,CURRENT_PRICE,CREATE_TIME) VALUES(?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        StockEntity entity = entities.get(i);
                        ps.setLong(1, entity.getId());
                        ps.setString(2, entity.getName());
                        ps.setBigDecimal(3, entity.getCurrentPrice());
                        ps.setTimestamp(4, Timestamp.valueOf(entity.getCreateTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return entities.size();
                    }
                }
        );
    }

}
