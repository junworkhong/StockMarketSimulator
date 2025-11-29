package project.processor;

/*
User trading pattern:
User starts with $10k
Inputs how much of the $10k they want to spend on their initial purchase (ie. $4000)
Inputs percentages of stocks/ETFs of how they want that spending money to be allocated (initial position using Day 1 price)
User inputs their own pattern: stop loss, risk, target, buy threshold price
If they run out of money, only holds and sells. Simulation continues until last day
 */

import project.common.Portfolio;
import project.common.StockETF;
import project.data.StockETFReader;

import java.util.*;

public class TradingPatterns {
    private final StockETFReader reader = StockETFReader.getInstance();
    private final Map<String, StockETF> stockETFs = reader.readStockETFs();
    private final Portfolio dummy = initializeDummyPortfolio();

    public void UserTradingPattern(){
        for(StockETF stockETF : dummy.getStockETFSet()){
            int initial = dummy.getInitialInvestment();
        }
    }

    private Portfolio initializeDummyPortfolio() {
        int initial = 6000;
        Map<String, Integer> allocation = new HashMap<>() {
            {
                put("AAPL", 20);
                put("AMZN", 15);
                put("GOOGL", 5);
                put("TSLA", 10);
                put("NVDA", 15);
                put("KO", 10);
                put("SPYD", 15);
                put("VOO", 10);
                put("SPYD", 10);
            }
        };
        int StopLoss = 80;
        int Risk = 10;
        int Target = 150;
        Map<String, Integer> threshold = new HashMap<>() {
            {
                put("AAPL", 100);
                put("AMZN", 150);
                put("GOOGL", 90);
                put("TSLA", 50);
                put("NVDA", 10);
                put("KO", 60);
                put("SPYD", 50);
                put("VOO", 310);
                put("VTI", 180);
            }
        };

        Set<StockETF> StockETFSet = new HashSet<>() {
            {
                add(stockETFs.get("AAPL"));
                add(stockETFs.get("AMZN"));
                add(stockETFs.get("GOOGL"));
                add(stockETFs.get("TSLA"));
                add(stockETFs.get("NVDA"));
                add(stockETFs.get("KO"));
                add(stockETFs.get("SPYD"));
                add(stockETFs.get("VOO"));
                add(stockETFs.get("VTI"));
            }
        };

        Portfolio dummyPortfolio = new Portfolio(
                initial,
                allocation,
                StopLoss,
                Risk,
                Target,
                threshold,
                StockETFSet
        );

        return dummyPortfolio;
    }

    public static void main(String[] args) {

    }
}
