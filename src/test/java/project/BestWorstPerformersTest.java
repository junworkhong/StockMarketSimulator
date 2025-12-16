package project;

import org.junit.Test;
import project.common.*;
import project.processor.SortBestWorstPerformers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class BestWorstPerformersTest {
    SortBestWorstPerformers sort =  new SortBestWorstPerformers();

    //for portfolio
    Map<String, Integer> allocations = new HashMap<>();
    Map<String, Double> threshold = new HashMap<>();
    Map<String, StockETF> userStockMap = new HashMap<>();
    Map <String, Double> shares = new HashMap<>();
    DateResultMap dateResultMap = new DateResultMap();
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
            dateResultMap
    );

    @Test
    public void nullPortfolio() {
        Portfolio portfolio = null;
        assertThrows(IllegalStateException.class, () -> sort.sortBestToWorst(portfolio));
    }

    @Test
    public void portfolioElementsNull() {
        //when userStockMap = null
        userStockMap = null;

        Portfolio portfolio = new Portfolio(
                initialInvestment,
                allocations,
                stopLoss,
                risk,
                target,
                threshold,
                userStockMap,
                shares,
                dateResultMap
        );

        assertThrows(IllegalStateException.class, () -> sort.sortBestToWorst(portfolio));
    }

    @Test
    public void skipsNullStock() {
        stock1 = new StockETF(ticker, security);
        stock2 = new StockETF(ticker2, security);
        stock3 = new StockETF(ticker3, security);
        stockETFMap = new HashMap<>();

        stock1.addPriceDate(price1);
        stock1.addPriceDate(price2);
        stock1.addPriceDate(price3);
        stock1.addPriceMap(date, price1);
        stock1.addPriceMap(date2, price2);
        stock1.addPriceMap(date3, price3);

        stock2.addPriceDate(price1);
        stock2.addPriceDate(price2);
        stock2.addPriceDate(price3);
        stock2.addPriceMap(date, price1);
        stock2.addPriceMap(date2, price2);
        stock2.addPriceMap(date3, price3);

        stock3.addPriceDate(price1);
        stock3.addPriceDate(price2);
        stock3.addPriceDate(price3);
        stock3.addPriceMap(date, price1);
        stock3.addPriceMap(date2, price2);
        stock3.addPriceMap(date3, price3);

        stockETFMap.put(ticker, stock1);
        stockETFMap.put(ticker2, null);
        stockETFMap.put(ticker3, stock3);

        Portfolio portfolio = new Portfolio(
                initialInvestment,
                allocations,
                stopLoss,
                risk,
                target,
                threshold,
                stockETFMap,
                shares,
                dateResultMap
        );

        portfolio.addTotalBuyDollars(ticker, 100.0);
        portfolio.addTotalBuyDollars(ticker2, 200.0);
        portfolio.addTotalBuyDollars(ticker3, 300.0);

        portfolio.addTotalSellDollars(ticker, 100.0);
        portfolio.addTotalSellDollars(ticker2, 50.0);
        portfolio.addTotalSellDollars(ticker3, 10.0);

        portfolio.addShares(ticker, 0.0);
        portfolio.addShares(ticker2, 0.0);
        portfolio.addShares(ticker3, 0.0);

        portfolio.initializeShares(ticker, 0.0);
        portfolio.initializeShares(ticker2, 0.0);
        portfolio.initializeShares(ticker3, 0.0);

        portfolio.addDayOnePrice(ticker, 0.0);
        portfolio.addDayOnePrice(ticker2, 0.0);
        portfolio.addDayOnePrice(ticker3, 0.0);

        sort.sortBestToWorst(portfolio);
    }

    @Test
    public void portfolioLoopNull() {
        ByteArrayOutputStream bucket = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bucket));

        stock1.addPriceDate(price1);
        stock1.addPriceDate(price2);
        stock1.addPriceDate(price3);
        stock1.addPriceMap(date, price1);
        stock1.addPriceMap(date2, price2);
        stock1.addPriceMap(date3, price3);

        stock2.addPriceDate(price1);
        stock2.addPriceDate(price2);
        stock2.addPriceDate(price3);
        stock2.addPriceMap(date, price1);
        stock2.addPriceMap(date2, price2);
        stock2.addPriceMap(date3, price3);

        stock3.addPriceDate(price1);
        stock3.addPriceDate(price2);
        stock3.addPriceDate(price3);
        stock3.addPriceMap(date, price1);
        stock3.addPriceMap(date2, price2);
        stock3.addPriceMap(date3, price3);

        stockETFMap.put(ticker, stock1);
        stockETFMap.put(ticker2, stock2);
        stockETFMap.put(ticker3, stock3);

        Portfolio portfolio = new Portfolio(
                initialInvestment,
                allocations,
                stopLoss,
                risk,
                target,
                threshold,
                stockETFMap,
                shares,
                dateResultMap
        );

        portfolio.addTotalBuyDollars(ticker, 100.0);
        portfolio.addTotalBuyDollars(ticker2, 200.0);
        portfolio.addTotalBuyDollars(ticker3, 300.0);

        portfolio.addTotalSellDollars(ticker, 100.0);
        portfolio.addTotalSellDollars(ticker2, 50.0);
        portfolio.addTotalSellDollars(ticker3, 10.0);

        portfolio.addShares(ticker, 0.0);
        portfolio.addShares(ticker2, 0.0);
        portfolio.addShares(ticker3, 0.0);

        portfolio.initializeShares(ticker, 0.0);
        portfolio.initializeShares(ticker2, 0.0);
        portfolio.initializeShares(ticker3, 0.0);

        portfolio.addDayOnePrice(ticker, 0.0);
        portfolio.addDayOnePrice(ticker2, 0.0);
        portfolio.addDayOnePrice(ticker3, 0.0);

        sort.sortBestToWorst(portfolio);
        assertFalse(bucket.toString().isEmpty());
    }
}
