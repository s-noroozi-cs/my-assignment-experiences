package com.git.plushmarket.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlushMarketResponse {
    private List<ResponseAction> actions = new LinkedList<>();
}
