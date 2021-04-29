package com.git.plushmarket;

import com.git.plushmarket.model.response.PlushMarketResponse;
import com.git.plushmarket.model.response.SellResponseAction;
import com.git.plushmarket.model.response.TradeResponseAction;
import com.git.plushmarket.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PlushMarketTests {
    private static final Logger logger = LoggerFactory.getLogger(PlushMarketTests.class);
    private PlushMarket plushMarket = new PlushMarketImpl();
    private final String EMPTY_RESULT = "{\"actions\":[]}";

    @BeforeEach
    public void setup(TestInfo testInfo) {
        logger.info("Enter test: {}", testInfo.getDisplayName());
    }

    @Test
    void testNullRequestEmptyResponse() {
        String result = plushMarket.calculateStrategy(null, null);
        String msg = "Expected '" + EMPTY_RESULT + "' but got '" + result + "'";
        Assertions.assertTrue(EMPTY_RESULT.equals(result), msg);
    }

    @Test
    void testInvalidRequestEmptyResponse() {
        String result = plushMarket.calculateStrategy("{", "[");
        String msg = "Expected '" + EMPTY_RESULT + "' but got '" + result + "'";
        Assertions.assertTrue(EMPTY_RESULT.equals(result), msg);
    }

    @Test
    void testEmptyMarketRequest() {
        String result = plushMarket.calculateStrategy("{\"plush\": \"BlueBird\"}", "{}");
        String msg = "Expected '" + EMPTY_RESULT + "' but got '" + result + "'";
        Assertions.assertTrue(EMPTY_RESULT.equals(result), msg);
    }


    @Test
    void testNotExistAnyAction() {
        String offerJson = "{\"plush\": \"BlueBird\"}";

        String marketJson = "{\"plushes\": [" +
                "    {\"plush\": \"RedBird\", \"price\": 80}," +
                "    {\"plush\": \"Stella\", \"price\": 90}," +
                "    {\"plush\": \"Pig\", \"price\": 75}" +
                "]," +
                "\"trades\": [" +
                "    {\"take\": \"RedBird\", \"give\": \"Pig\"}," +
                "    {\"take\": \"Pig\", \"give\": \"Stella\"}" +
                "]}";

        String result = plushMarket.calculateStrategy(offerJson, marketJson);
        String msg = "Expected '" + EMPTY_RESULT + "' but got '" + result + "'";
        Assertions.assertTrue(EMPTY_RESULT.equals(result), msg);
    }

    @Test
    void testDirectSellStella() {
        String offerJson = "{\"plush\": \"Stella\"}";

        String marketJson = "{\"plushes\": [" +
                "    {\"plush\": \"RedBird\", \"price\": 80}," +
                "    {\"plush\": \"Stella\", \"price\": 90}," +
                "    {\"plush\": \"Pig\", \"price\": 75}" +
                "]," +
                "\"trades\": [" +
                "    {\"take\": \"RedBird\", \"give\": \"Pig\"}," +
                "    {\"take\": \"Pig\", \"give\": \"Stella\"}" +
                "]}";

        String result = plushMarket.calculateStrategy(offerJson, marketJson);

        SellResponseAction sellRespAct = new SellResponseAction();
        sellRespAct.setPlush("Stella");
        sellRespAct.setPrice(90);

        PlushMarketResponse resp = new PlushMarketResponse();
        resp.getActions().add(sellRespAct);

        String expectedResult = JsonUtil.objToJsonString(resp).orElseThrow(
                () -> new RuntimeException("Response is not valid. check the application log"));


        Assertions.assertTrue(expectedResult.equals(result),
                "Expected '" + expectedResult + "' but got '" + result + "'");
    }

    @Test
    void testDepthGraph() {
        String offerJson = "{\"plush\": \"A\"}";

        String marketJson = "{\"plushes\": [" +
                "    {\"plush\": \"A\", \"price\": 1}," +
                "    {\"plush\": \"B\", \"price\": 2}," +
                "    {\"plush\": \"C\", \"price\": 3}," +
                "    {\"plush\": \"D\", \"price\": 4}" +
                "]," +

                "\"trades\": [" +
                "    {\"take\": \"A\", \"give\": \"B\"}," +
                "    {\"take\": \"A\", \"give\": \"C\"}," +
                "    {\"take\": \"A\", \"give\": \"D\"}," +

                "    {\"take\": \"B\", \"give\": \"A\"}," +
                "    {\"take\": \"B\", \"give\": \"C\"}," +
                "    {\"take\": \"B\", \"give\": \"D\"}," +

                "    {\"take\": \"C\", \"give\": \"A\"}," +
                "    {\"take\": \"C\", \"give\": \"B\"}," +
                "    {\"take\": \"C\", \"give\": \"D\"}," +

                "    {\"take\": \"D\", \"give\": \"A\"}," +
                "    {\"take\": \"D\", \"give\": \"B\"}," +
                "    {\"take\": \"D\", \"give\": \"C\"}" +

                "]}";

        String result = plushMarket.calculateStrategy(offerJson, marketJson);

        TradeResponseAction trade= new TradeResponseAction();
        trade.setGive("A");
        trade.setTake("D");


        SellResponseAction sellRespAct = new SellResponseAction();
        sellRespAct.setPlush("D");
        sellRespAct.setPrice(4);

        PlushMarketResponse resp = new PlushMarketResponse();
        resp.getActions().add(trade);
        resp.getActions().add(sellRespAct);

        String expectedResult = JsonUtil.objToJsonString(resp).orElseThrow(
                () -> new RuntimeException("Response is not valid. check the application log"));


        Assertions.assertTrue(expectedResult.equals(result),
                "Expected '" + expectedResult + "' but got '" + result + "'");
    }

    @Test
    void testSellWithTrading() {
        String offerJson = "{\"plush\": \"RedBird\"}";

        String marketJson = "{\"plushes\": [" +
                "    {\"plush\": \"RedBird\", \"price\": 80}," +
                "    {\"plush\": \"Stella\", \"price\": 90}," +
                "    {\"plush\": \"Pig\", \"price\": 75}" +
                "]," +
                "\"trades\": [" +
                "    {\"take\": \"RedBird\", \"give\": \"Pig\"}," +
                "    {\"take\": \"Pig\", \"give\": \"Stella\"}" +
                "]}";

        String result = plushMarket.calculateStrategy(offerJson, marketJson);


        PlushMarketResponse resp = new PlushMarketResponse();
        resp.getActions().add(new TradeResponseAction("RedBird", "Pig"));
        resp.getActions().add(new TradeResponseAction("Pig", "Stella"));
        resp.getActions().add(new SellResponseAction("Stella", 90));

        String expectedResult = JsonUtil.objToJsonString(resp).orElseThrow(
                () -> new RuntimeException("Response is not valid. check the application log"));


        Assertions.assertTrue(expectedResult.equals(result),
                "Expected '" + expectedResult + "' but got '" + result + "'");
    }

    @Test
    void testMaxSellCostWithMinTradingNumber() {
        String offerJson = "{\"plush\": \"BlueBird\"}";

        String marketJson =
                "{\"plushes\": [" +
                        "    {\"plush\": \"RedBird\", \"price\": 90}," +
                        "    {\"plush\": \"Stella\", \"price\": 90}," +
                        "    {\"plush\": \"Pig\", \"price\": 80}," +
                        "    {\"plush\": \"BlueBird\", \"price\": 70}" +
                        "]," +

                        "\"trades\": [" +
                        "    {\"take\": \"BlueBird\", \"give\": \"RedBird\"}," +
                        "    {\"take\": \"Stella\", \"give\": \"RedBird\"}," +
                        "    {\"take\": \"Pig\", \"give\": \"Stella\"}," +
                        "    {\"take\": \"BlueBird\", \"give\": \"Pig\"}" +
                        "]}";

        String result = plushMarket.calculateStrategy(offerJson, marketJson);


        PlushMarketResponse resp = new PlushMarketResponse();
        resp.getActions().add(new TradeResponseAction("BlueBird", "RedBird"));
        resp.getActions().add(new SellResponseAction("RedBird", 90));

        String expectedResult = JsonUtil.objToJsonString(resp).orElseThrow(
                () -> new RuntimeException("Response is not valid. check the application log"));


        Assertions.assertTrue(expectedResult.equals(result),
                "Expected '" + expectedResult + "' but got '" + result + "'");
    }


    @Test
    void testPreventTradeLoop() {
        String offerJson = "{\"plush\": \"BlueBird\"}";

        String marketJson =
                "{\"plushes\": [" +
                        "    {\"plush\": \"WhiteBird\", \"price\": 70}" +
                        "]," +

                        "\"trades\": [" +
                        "    {\"take\": \"BlueBird\", \"give\": \"BlueBird\"}" +
                        "]}";

        String result = plushMarket.calculateStrategy(offerJson, marketJson);
        PlushMarketResponse resp = new PlushMarketResponse();

        String expectedResult = JsonUtil.objToJsonString(resp).orElseThrow(
                () -> new RuntimeException("Response is not valid. check the application log"));


        Assertions.assertTrue(expectedResult.equals(result),
                "Expected '" + expectedResult + "' but got '" + result + "'");
    }

    @Test
    void testPreventCircularTrading() {
        String offerJson = "{\"plush\": \"BlueBird\"}";

        String marketJson =
                "{\"plushes\": [" +
                        "    {\"plush\": \"BlueBird\", \"price\": 70}" +
                        "]," +

                        "\"trades\": [" +
                        "    {\"take\": \"BlueBird\", \"give\": \"Pig\"}," +
                        "    {\"take\": \"Pig\", \"give\": \"Stella\"}," +
                        "    {\"take\": \"Stella\", \"give\": \"BlueBird\"}" +
                        "]}";

        String result = plushMarket.calculateStrategy(offerJson, marketJson);
        PlushMarketResponse resp = new PlushMarketResponse();
        resp.getActions().add(new SellResponseAction("BlueBird",70));

        String expectedResult = JsonUtil.objToJsonString(resp).orElseThrow(
                () -> new RuntimeException("Response is not valid. check the application log"));


        Assertions.assertTrue(expectedResult.equals(result),
                "Expected '" + expectedResult + "' but got '" + result + "'");
    }

}
