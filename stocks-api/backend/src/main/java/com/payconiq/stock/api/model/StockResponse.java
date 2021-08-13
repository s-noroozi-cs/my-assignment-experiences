package com.payconiq.stock.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponse {
    private Long id;
    private String name;
    private BigDecimal currentPrice;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdate;
}
