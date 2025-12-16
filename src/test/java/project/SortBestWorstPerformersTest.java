package project;

import org.junit.Test;
import project.common.*;
import project.processor.SortBestWorstPerformers;

import java.util.*;

public class SortBestWorstPerformersTest {

    private Portfolio makePortfolio(double initialInvestment,
                                    Map<String, StockETF> userStockMap,
                                    Map<String, Double> shares) {

        Map<String, Integer> allocations = new HashMap<>();
        Map<String, Double> threshold = new HashMap<>();
        DateResultMap dateResults = new DateResultMap();
        int stopLoss = 0, risk = 0, target = 0;

        return new Portfolio(
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
    }

    private StockETF makeStockWithCloseOnDate(String ticker, MyDate date, double close) {
        StockETF s = new StockETF(ticker, Security.Stock);
        s.addPriceMap(date, new PriceDate(close, 0L, 0.0, 0.0, 0.0));
        return s;
    }

    @Test(expected = IllegalStateException.class)
    public void testNullPortfolio_throwIllegalStateException() {
        new SortBestWorstPerformers().sortBestToWorst(null);
    }

    @Test
    public void testStockListNotNull_defaultBehavior() {
        Map<String, StockETF> userMap = new HashMap<>();
        Map<String, Double> shares = new HashMap<>();
        Portfolio p = makePortfolio(10000.0, userMap, shares);

    }


    @Test
    public void testNormalCase_runsThrough() {

        MyDate date = new MyDate("2025-11-14");


        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", makeStockWithCloseOnDate("AAPL", date, 200.0));
        userMap.put("GOOGL", makeStockWithCloseOnDate("GOOGL", date, 100.0));


        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 10.0);
        shares.put("GOOGL", 5.0);

        Portfolio p = makePortfolio(10000.0, userMap, shares);


        p.addTotalBuyDollars("AAPL", 1000.0);
        p.addTotalSellDollars("AAPL", 1200.0);

        p.addTotalBuyDollars("GOOGL", 500.0);
        p.addTotalSellDollars("GOOGL", 400.0);

        p.initializeShares("AAPL", 10.0);
        p.initializeShares("GOOGL", 5.0);

        p.addDayOnePrice("AAPL", 100.0);
        p.addDayOnePrice("GOOGL", 100.0);


        List<String> stockList = new ArrayList<>();
        stockList.add("AAPL");
        stockList.add("GOOGL");



        new SortBestWorstPerformers().sortBestToWorst(p);
    }

    @Test
    public void testNullTickerInStockList_skips() {
        MyDate date = new MyDate("2025-11-14");

        Map<String, StockETF> userMap = new HashMap<>();
        userMap.put("AAPL", makeStockWithCloseOnDate("AAPL", date, 200.0));

        Map<String, Double> shares = new HashMap<>();
        shares.put("AAPL", 10.0);

        Portfolio p = makePortfolio(10000.0, userMap, shares);

        p.addTotalBuyDollars("AAPL", 1000.0);
        p.addTotalSellDollars("AAPL", 1200.0);
        p.initializeShares("AAPL", 10.0);
        p.addDayOnePrice("AAPL", 100.0);

        List<String> stockList = new ArrayList<>();
        stockList.add(null);     // 覆盖 if (ticker == null) continue;
        stockList.add("AAPL");


        new SortBestWorstPerformers().sortBestToWorst(p);
    }
}
