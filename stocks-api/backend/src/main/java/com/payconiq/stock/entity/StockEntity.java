package com.payconiq.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_STOCK")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {

    @Id
    @SequenceGenerator(name = "seq_stock", sequenceName = "SEQ_STOCK_ID", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock")
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "CURRENT_PRICE")
    private BigDecimal currentPrice;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "LAST_UPDATE")
    private LocalDateTime lastUpdate;

}
