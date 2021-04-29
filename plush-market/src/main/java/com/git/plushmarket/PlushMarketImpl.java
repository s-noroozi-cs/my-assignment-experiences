package com.git.plushmarket;

import com.git.plushmarket.model.TradeAndSellCost;
import com.git.plushmarket.model.request.Market;
import com.git.plushmarket.model.request.Offer;
import com.git.plushmarket.model.request.Plush;
import com.git.plushmarket.model.request.Trade;
import com.git.plushmarket.model.response.PlushMarketResponse;
import com.git.plushmarket.model.response.ResponseAction;
import com.git.plushmarket.model.response.SellResponseAction;
import com.git.plushmarket.model.response.TradeResponseAction;
import com.git.plushmarket.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class PlushMarketImpl implements PlushMarket {
    private static final Logger logger = LoggerFactory.getLogger(PlushMarketImpl.class);

    @Override
    public String calculateStrategy(String offerJSON, String marketJSON) {
        long startTime = System.nanoTime();
        try {
            Optional<Offer> offer = JsonUtil.jsonStringToObj(offerJSON, Offer.class);
            Optional<Market> market = JsonUtil.jsonStringToObj(marketJSON, Market.class);
            Optional<String> response = Optional.empty();

            if (offer.isPresent() && market.isPresent()) {

                List<TradeAndSellCost> allTradeAndSellCost = getAllTradeAndSellCost(offer.get(), market.get());

                PlushMarketResponse plushMarketResponse = new PlushMarketResponse();
                plushMarketResponse.setActions(getOptimalStrategy(allTradeAndSellCost));

                response = JsonUtil.objToJsonString(plushMarketResponse);
            } else {
                logger.error("Input parameters is not valid json or empty. offer: {} and market: {}", offerJSON, marketJSON);
            }

            if (response.isPresent()) {
                return response.get();
            } else {
                logger.warn("There is not exist any response due to the bad parameters.");
                return "{\"actions\":[]}";
            }
        } finally {
            Duration elapseTime = Duration.of((System.nanoTime() - startTime), ChronoUnit.NANOS);
            logger.info("calculateStrategy take {} milliseconds.", elapseTime.toMillis());
        }
    }

    private List<TradeAndSellCost> getAllTradeAndSellCost(Offer offer1, Market market) {
        List<TradeAndSellCost> allCalcCost = new ArrayList<>();
        Deque<TradeAndSellCost> deque = new ArrayDeque<>();
        deque.push(new TradeAndSellCost(offer1));

        while (!deque.isEmpty()) {
            TradeAndSellCost curCalcCost = deque.pop();

            matchPlush(curCalcCost.getOffer(), market)
                    .ifPresent(i -> {
                        SellResponseAction sellResponseAction = new SellResponseAction();
                        sellResponseAction.setPrice(i.getPrice());
                        sellResponseAction.setPlush(i.getPlush());

                        TradeAndSellCost selCalcCost = curCalcCost.newInstanceWithAllActions();
                        selCalcCost.getActions().add(sellResponseAction);

                        allCalcCost.add(selCalcCost);
                    });

            matchTrade(curCalcCost.getOffer(), market)
                    .forEach(trade -> {
                        TradeAndSellCost tradeCalcCost =  curCalcCost.newInstanceWithAllActions();
                        TradeResponseAction newTrade = new TradeResponseAction(trade.getTake(), trade.getGive());
                        if (tradeCalcCost.checkTradeSeries(newTrade)) {
                            tradeCalcCost.getActions().add(newTrade);
                            tradeCalcCost.setOffer(new Offer(trade.getGive()));
                            deque.push(tradeCalcCost);
                        }
                    });
        }
        return allCalcCost;
    }


    private Optional<Plush> matchPlush(Offer offer, Market market) {
        return Optional.ofNullable(market)
                .map(Market::getPlushes)
                .orElseGet(Collections::emptyList)
                .stream().filter(i -> Objects.equals(i.getPlush(), offer.getPlush())).findFirst();
    }

    private List<Trade> matchTrade(Offer offer, Market market) {
        return Optional.ofNullable(market)
                .map(Market::getTrades)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Trade::isTakeAndGiveDifferent)//prevent trade infinite loop
                .filter(i -> Objects.equals(i.getTake(), offer.getPlush()))
                .collect(Collectors.toList());
    }

    private List<ResponseAction> getOptimalStrategy(List<TradeAndSellCost> tradeAndSellCosts) {
        return tradeAndSellCosts.stream()
                .max(getMaxPriceMinTradeCountStrategy())
                .map(TradeAndSellCost::getActions)
                .orElseGet(Collections::<ResponseAction>emptyList);
    }


    private Comparator<TradeAndSellCost> getMaxPriceMinTradeCountStrategy() {
        return (caseA, caseB) -> {
            if (caseA.getPrice() == caseB.getPrice())
                return caseB.getTradeCount() - caseA.getTradeCount();
            else
                return caseA.getPrice() - caseB.getPrice();
        };
    }


}
