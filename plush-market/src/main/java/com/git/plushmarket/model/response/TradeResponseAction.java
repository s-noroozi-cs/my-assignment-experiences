package com.git.plushmarket.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TradeResponseAction extends ResponseAction {
    private String give;
    private String take;

    @Override
    public ActionType getAction() {
        return ActionType.TRADE;
    }

    public TradeResponseAction(String give, String take) {
        this.give = give;
        this.take = take;
    }
}
