package com.git.plushmarket.model.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ActionType {
    TRADE("trade"), SELL("sell");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
