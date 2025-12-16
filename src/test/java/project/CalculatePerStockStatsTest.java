package project;

import org.junit.Test;
import project.common.*;
import project.processor.CalculatePerStockStats;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CalculatePerStockStatsTest {

    private Portfolio makePortfolio(double initialInvestment,
                                    Map<String, StockETF> userStockMap,
                                    Map<String, Double> shares,
                                    Map<String, Double> buyPrice) {

        Map<String, Integer> allocations = new HashMap<>();
        Map<String, Double> threshold = new HashMap<>();
        DateResultMap dateResults = new DateResultMap();
        int stopLoss = 0, risk = 0, target = 0;

        Portfolio p = new Portfolio(
                initialInvestment,
                allocations,
                stopLoss,
                risk,
                target,
                threshold,
                userStockMap,
                shares,
                dateResults
        );


        for (Map.Entry<String, Double> e : buyPrice.entrySet()) {
            p.addBuyPrice(e.getKey(), e.getValue());
        }

        return p;
    }

    private StockETF makeStockWithCloseOnDate(String ticker, MyDate date, double close) {
        StockETF s = new StockETF(ticker, Security.Stock);
        s.addPriceMap(date, new PriceDate(close, 0L, 0.0, 0.0, 0.0));
        return s;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputs_throwIllegalArgumentException() {
        new CalculatePerStockStats().execute(null, "AAPL", new MyDate("2020-01-02"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNullUserStockMap_throwIllegalStateException() {
        Map<String, Double> shares = new HashMap<>();
        Map<String, Double> buy = new HashMap<>();
        Portfolio p = makePortfolio(10000.0, null, shares, buy);

        new CalculatePerStockStats().execute(p, "AAPL", new MyDate("2020-01-02"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownTicker_throwIllegalArgumentException() {
        MyDate d = new MyDate("2020-01-02");

        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", makeStockWithCloseOnDate("AAPL", d, 120.0));

        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 10.0);

        Map<String, Double> buy = new HashMap<>();
        buy.put("AAPL", 100.0);

        Portfolio p = makePortfolio(10000.0, userMap, shares, buy);

        new CalculatePerStockStats().execute(p, "MSFT", d);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoPriceDataOnDate_throwIllegalStateException() {
        MyDate d1 = new MyDate("2020-01-02");
        MyDate d2 = new MyDate("2020-01-03"); // 没放到 priceMap

        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", makeStockWithCloseOnDate("AAPL", d1, 120.0));

        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 10.0);

        Map<String, Double> buy = new HashMap<>();
        buy.put("AAPL", 100.0);

        Portfolio p = makePortfolio(10000.0, userMap, shares, buy);

        new CalculatePerStockStats().execute(p, "AAPL", d2);
    }

    @Test
    public void testZeroShares_writeZerosAndReturn() {
        MyDate d = new MyDate("2020-01-02");

        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", makeStockWithCloseOnDate("AAPL", d, 120.0));

        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 0.0);

        Map<String, Double> buy = new HashMap<>();
        buy.put("AAPL", 100.0);

        Portfolio p = makePortfolio(10000.0, userMap, shares, buy);

        new CalculatePerStockStats().execute(p, "aapl", d); // 覆盖 toUpperCase

        assertNotNull(p.getPerStockETFStats());
        assertEquals(0.0, p.getPerStockETFStats().get("AAPL").get(0), 1e-9);
        assertEquals(0.0, p.getPerStockETFStats().get("AAPL").get(1), 1e-9);
        assertEquals(0.0, p.getPerStockETFStats().get("AAPL").get(2), 1e-9);
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingBuyPrice_throwIllegalStateException() {
        MyDate d = new MyDate("2020-01-02");

        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", makeStockWithCloseOnDate("AAPL", d, 120.0));

        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 10.0);

        Map<String, Double> buy = new HashMap<>();


        Portfolio p = makePortfolio(10000.0, userMap, shares, buy);

        new CalculatePerStockStats().execute(p, "AAPL", d);
    }

    @Test
    public void testNormalProfit_writesCorrectStats() {
        MyDate d = new MyDate("2020-01-02");

        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", makeStockWithCloseOnDate("AAPL", d, 120.0));

        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 10.0);

        Map<String, Double> buy = new HashMap<>();
        buy.put("AAPL", 100.0);

        Portfolio p = makePortfolio(10000.0, userMap, shares, buy);

        new CalculatePerStockStats().execute(p, "AAPL", d);

        assertNotNull(p.getPerStockETFStats());
        assertEquals(1200.0, p.getPerStockETFStats().get("AAPL").get(0), 1e-9); // currentValue
        assertEquals(200.0, p.getPerStockETFStats().get("AAPL").get(1), 1e-9);  // profitLoss
        assertEquals(20.0, p.getPerStockETFStats().get("AAPL").get(2), 1e-9);   // profitLossPercent
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDate_throwIllegalArgumentException() {
        MyDate bad = new MyDate("2020-13-40"); // 或者 "bad-date"
        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", new StockETF("AAPL", Security.Stock));

        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 1.0);

        Map<String, Double> buy = new HashMap<>();
        buy.put("AAPL", 100.0);

        Portfolio p = makePortfolio(10000.0, userMap, shares, buy);

        new CalculatePerStockStats().execute(p, "AAPL", bad);
    }

}
