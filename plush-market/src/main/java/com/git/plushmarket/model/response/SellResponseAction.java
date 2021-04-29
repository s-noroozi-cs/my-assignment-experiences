package com.git.plushmarket.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellResponseAction extends ResponseAction {
    private String plush;
    private Integer price;

    @Override
    public ActionType getAction() {
        return ActionType.SELL;
    }

    public SellResponseAction(String plush, int price) {
        this.plush = plush;
        this.price = price;
    }
}
