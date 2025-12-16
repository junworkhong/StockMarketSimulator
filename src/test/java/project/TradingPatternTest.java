package project;

import org.junit.Test;
import project.common.*;
import project.processor.TradingPattern;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;

public class TradingPatternTest {
    TradingPattern trade = new TradingPattern();

    //for portfolio
    Map<String, Integer> allocations = new HashMap<>();
    Map<String, Double> threshold = new HashMap<>();
    Map<String, StockETF> userStockMap = new HashMap<>();
    Map <String, Double> shares = new HashMap<>();
    DateResultMap dateResults = new DateResultMap();
    Map<String, Double> dayOne = new HashMap<>();
    double initialInvestment = 0.0;
    int stopLoss = 0;
    int risk = 0;
    int target = 0;

    //for PriceDate
    double close = 0.0;
    long volume = 0;
    double open = 0.0;
    double high = 0.0;
    double low = 0.0;

    //for MyDate
    MyDate date = new MyDate("2020-01-02");
    MyDate date2 = new MyDate("2020-01-03");
    MyDate date3 = new MyDate("2025-11-14");

    //for StockETF
    String ticker = "AAPL";
    String ticker2 = "GOOGL";
    String ticker3 = "JPM";
    Security security = Security.Stock;

    PriceDate price1 = new PriceDate(close, volume, open, high, low);
    PriceDate price2 = new PriceDate(close, volume, open, high, low);
    PriceDate price3 = new PriceDate(close, volume, open, high, low);

    StockETF stock1 = new StockETF(ticker, security);
    StockETF stock2 = new StockETF(ticker2, security);
    StockETF stock3 = new StockETF(ticker3, security);

    Map<String, StockETF> stockETFMap = new HashMap<>();

    Portfolio portfolio = new Portfolio(
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

    @Test
    public void testNullPortfolio() {
        portfolio = null;
        assertThrows(IllegalStateException.class, () -> trade.RunTradingPattern(portfolio));
    }

//    @Test
//    public void testNullUserStocks() {
//        stock1 = new StockETF(ticker, security);
//        stock2 = new StockETF(ticker2, security);
//        stock3 = new StockETF(ticker3, security);
//        stockETFMap = null;
//
//        stock1.addPriceDate(price1);
//        stock1.addPriceDate(price2);
//        stock1.addPriceDate(price3);
//        stock1.addPriceMap(date, price1);
//        stock1.addPriceMap(date2, price2);
//        stock1.addPriceMap(date3, price3);
//
//        stock2.addPriceDate(price1);
//        stock2.addPriceDate(price2);
//        stock2.addPriceDate(price3);
//        stock2.addPriceMap(date, price1);
//        stock2.addPriceMap(date2, price2);
//        stock2.addPriceMap(date3, price3);
//
//        stock3.addPriceDate(price1);
//        stock3.addPriceDate(price2);
//        stock3.addPriceDate(price3);
//        stock3.addPriceMap(date, price1);
//        stock3.addPriceMap(date2, price2);
//        stock3.addPriceMap(date3, price3);
//
//        Portfolio portfolio = new Portfolio(
//                initialInvestment,
//                allocations,
//                stopLoss,
//                risk,
//                target,
//                threshold,
//                stockETFMap,
//                shares,
//                dateResults
//        );
//
//        portfolio.addTotalBuyDollars(ticker, 100.0);
//        portfolio.addTotalBuyDollars(ticker2, 200.0);
//        portfolio.addTotalBuyDollars(ticker3, 300.0);
//
//        portfolio.addTotalSellDollars(ticker, 100.0);
//        portfolio.addTotalSellDollars(ticker2, 50.0);
//        portfolio.addTotalSellDollars(ticker3, 10.0);
//
//        portfolio.addShares(ticker, 0.0);
//        portfolio.addShares(ticker2, 0.0);
//        portfolio.addShares(ticker3, 0.0);
//
//        portfolio.initializeShares(ticker, 0.0);
//        portfolio.initializeShares(ticker2, 0.0);
//        portfolio.initializeShares(ticker3, 0.0);
//
//        portfolio.addDayOnePrice(ticker, 0.0);
//        portfolio.addDayOnePrice(ticker2, 0.0);
//        portfolio.addDayOnePrice(ticker3, 0.0);
//
//        assertThrows(IllegalStateException.class, () -> trade.RunTradingPattern(portfolio));
//    }
//
//    @Test
//    public void testNullStringStockAllocations() {
//        stock1 = new StockETF(ticker, security);
//        stock2 = new StockETF(ticker2, security);
//        stock3 = new StockETF(ticker3, security);
//        stockETFMap = new HashMap<>();
//
//        stock1.addPriceDate(price1);
//        stock1.addPriceDate(price2);
//        stock1.addPriceDate(price3);
//        stock1.addPriceMap(date, price1);
//        stock1.addPriceMap(date2, price2);
//        stock1.addPriceMap(date3, price3);
//
//        stock2.addPriceDate(price1);
//        stock2.addPriceDate(price2);
//        stock2.addPriceDate(price3);
//        stock2.addPriceMap(date, price1);
//        stock2.addPriceMap(date2, price2);
//        stock2.addPriceMap(date3, price3);
//
//        stock3.addPriceDate(price1);
//        stock3.addPriceDate(price2);
//        stock3.addPriceDate(price3);
//        stock3.addPriceMap(date, price1);
//        stock3.addPriceMap(date2, price2);
//        stock3.addPriceMap(date3, price3);
//
//        stockETFMap.put(ticker, stock1);
//        stockETFMap.put(null, stock2);
//        stockETFMap.put(ticker3, null);
//
//        allocations = null;
//
//        threshold.put(ticker, 0.0);
//        threshold.put(ticker2, 0.0);
//        threshold.put(ticker3, 0.0);
//
//        Portfolio portfolio = new Portfolio(
//                initialInvestment,
//                allocations,
//                stopLoss,
//                risk,
//                target,
//                threshold,
//                stockETFMap,
//                shares,
//                dateResults
//        );
//
//        portfolio.addTotalBuyDollars(ticker, 100.0);
//        portfolio.addTotalBuyDollars(ticker2, 200.0);
//        portfolio.addTotalBuyDollars(ticker3, 300.0);
//
//        portfolio.addTotalSellDollars(ticker, 100.0);
//        portfolio.addTotalSellDollars(ticker2, 50.0);
//        portfolio.addTotalSellDollars(ticker3, 10.0);
//
//        portfolio.addShares(ticker, 0.0);
//        portfolio.addShares(null, 0.0);
//        portfolio.addShares(ticker3, 0.0);
//
//        portfolio.initializeShares(ticker, 0.0);
//        portfolio.initializeShares(ticker2, 0.0);
//        portfolio.initializeShares(ticker3, 0.0);
//
//        portfolio.addDayOnePrice(ticker, 0.0);
//        portfolio.addDayOnePrice(ticker2, 0.0);
//        portfolio.addDayOnePrice(ticker3, 0.0);
//
//        trade.RunTradingPattern(portfolio);
//    }
//
//    @Test
//    public void testDummyPortfolio() {
//        stock1 = new StockETF(ticker, security);
//        stock2 = new StockETF(ticker2, security);
//        stock3 = new StockETF(ticker3, security);
//
//        stock1.addPriceDate(price1);
//        stock1.addPriceDate(price2);
//        stock1.addPriceDate(price3);
//        stock1.addPriceMap(date, price1);
//        stock1.addPriceMap(date2, price2);
//        stock1.addPriceMap(date3, price3);
//
//        stock2.addPriceDate(price1);
//        stock2.addPriceDate(price2);
//        stock2.addPriceDate(price3);
//        stock2.addPriceMap(date, price1);
//        stock2.addPriceMap(date2, price2);
//        stock2.addPriceMap(date3, price3);
//
//        stock3.addPriceDate(price1);
//        stock3.addPriceDate(price2);
//        stock3.addPriceDate(price3);
//        stock3.addPriceMap(date, price1);
//        stock3.addPriceMap(date2, price2);
//        stock3.addPriceMap(date3, price3);
//
//        stockETFMap = new HashMap<>();
//
//        stockETFMap.put(ticker, stock1);
//        stockETFMap.put(ticker2, stock2);
//        stockETFMap.put(ticker3, stock3);
//
//        threshold.put(ticker, 0.0);
//        threshold.put(ticker2, 0.0);
//        threshold.put(ticker3, 0.0);
//
//        allocations.put(ticker, 40);
//        allocations.put(ticker2, 30);
//        allocations.put(ticker3, 30);
//
//        portfolio = new Portfolio(
//                initialInvestment,
//                allocations,
//                stopLoss,
//                risk,
//                target,
//                threshold,
//                stockETFMap,
//                shares,
//                dateResults
//        );
//
//        portfolio.addTotalBuyDollars(ticker, 100.0);
//        portfolio.addTotalBuyDollars(ticker2, 200.0);
//        portfolio.addTotalBuyDollars(ticker3, 300.0);
//
//        portfolio.addTotalSellDollars(ticker, 100.0);
//        portfolio.addTotalSellDollars(ticker2, 50.0);
//        portfolio.addTotalSellDollars(ticker3, 10.0);
//
//        portfolio.addShares(ticker, 0.0);
//        portfolio.addShares(ticker2, 0.0);
//        portfolio.addShares(ticker3, 0.0);
//
//        portfolio.initializeShares(ticker, 0.0);
//        portfolio.initializeShares(ticker2, 0.0);
//        portfolio.initializeShares(ticker3, 0.0);
//
//        portfolio.addDayOnePrice(ticker, 0.0);
//        portfolio.addDayOnePrice(ticker2, 0.0);
//        portfolio.addDayOnePrice(ticker3, 0.0);
//
//        portfolio.addBuyPrice(ticker, 0.0);
//        portfolio.addBuyPrice(ticker2, 0.0);
//        portfolio.addBuyPrice(ticker3, 0.0);
//
//        trade.RunTradingPattern(portfolio);
//    }
}
