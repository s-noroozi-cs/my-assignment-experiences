package com.payconiq.stock.db;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class FlywaySchemaHistory {
    private int version;
    private String script;
    private String type;
    private boolean success;
}
