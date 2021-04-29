package com.git.plushmarket.model;

import com.git.plushmarket.model.request.Offer;
import com.git.plushmarket.model.response.ActionType;
import com.git.plushmarket.model.response.ResponseAction;
import com.git.plushmarket.model.response.SellResponseAction;
import com.git.plushmarket.model.response.TradeResponseAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class TradeAndSellCost {
    private Offer offer;
    private List<ResponseAction> actions = new ArrayList<>();

    public TradeAndSellCost(Offer offer) {
        this.offer = offer;
    }


    public int getPrice() {
        return Optional.ofNullable(actions)
                .filter(act -> act.size() > 0)
                .map(act -> act.get(act.size() - 1))
                .filter(SellResponseAction.class::isInstance)
                .map(SellResponseAction.class::cast)
                .map(SellResponseAction::getPrice)
                .orElse(0);
    }

    public int getTradeCount() {
        return getActions().size();
    }


    public TradeAndSellCost newInstanceWithAllActions() {
        TradeAndSellCost newInstance = new TradeAndSellCost();
        newInstance.getActions().addAll(getActions());
        return newInstance;
    }

    public boolean checkTradeSeries(TradeResponseAction newTrade) {
        return getActions().stream()
                .filter(t -> ActionType.TRADE.equals(t.getAction()))
                .map(TradeResponseAction.class::cast)
                .noneMatch(t -> !Objects.equals(t.getTake(), newTrade.getGive()));
    }


}
