package com.git.plushmarket.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public abstract class ResponseAction {
    public abstract ActionType getAction();
}
