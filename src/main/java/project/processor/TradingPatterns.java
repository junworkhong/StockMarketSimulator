package project.processor;

/*
User trading pattern:
User starts with $10k
Inputs how much of the $10k they want to spend on their initial purchase (ie. $4000)
Inputs percentages of stocks/ETFs of how they want that spending money to be allocated (initial position using Day 1 price)
User inputs their own pattern: stop loss, risk, target, buy threshold price
If they run out of money, only holds and sells. Simulation continues until last day
 */

import project.common.*;
import project.data.StockETFReader;

import java.util.*;

public class TradingPatterns {
    private static final StockETFReader reader = StockETFReader.getInstance();
    private static final Map<String, StockETF> stockETFs = reader.readStockETFs();
    private static final Portfolio dummy = initializeDummyPortfolio();
    private static final DateCounter dates = new DateCounter();

    /*
     Map<String, Double> shares = new HashMap<>()
                put("AAPL", (double)(20*initial));
                put("AMZN", (double)15*initial);
                put("GOOGL", (double)5*initial);
                put("TSLA", (double)10*initial);
                put("NVDA", (double)15*initial);
                put("KO", (double)10*initial);
                put("SPYD", (double)15*initial);
                put("VOO", (double)10*initial);
                put("SPYD", (double)10*initial);
     */

    public static void UserTradingPattern(){
//        double budget = dummy.getBudget();
        double initial = dummy.getInitialInvestment(); //6000
        double stoploss = dummy.getStopLoss() * 0.01;
        double risk = dummy.getRisk() * 0.01;
        double target = dummy.getTarget() * 0.01;
        dummy.addBudget((-1)*initial); // = 4000
        risk = risk * dummy.getBudget();
        System.out.println(dummy.getBudget());

        Map<String, StockETF> UserStocks = dummy.getUserStockETFMap();

        for (MyDate date : dates.getDateList()) {
            for (Map.Entry<String, StockETF> entry : UserStocks.entrySet()) {
                int i = 0;
                double shareCounter = dummy.getShares().get(entry.getKey());
                double open = entry.getValue().getPriceMap().get(date).getOpen();
                double thresholdPrice = dummy.getThreshold().get(entry.getKey());
                String tickerName = entry.getKey();

                if (shareCounter == 0) {
                    if (open > thresholdPrice && (dummy.getBudget() > risk))
                        dummy.addShares(tickerName, risk * open);
                }
            }

        }
    }

    private static Portfolio initializeDummyPortfolio() {
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
                put("VTI", 10);
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

        Map<String, StockETF> UserStockETFMap = new HashMap<>() {
            {
                put("AAPL", stockETFs.get("AAPL"));
                put("AMZN", stockETFs.get("AMZN"));
                put("GOOGL", stockETFs.get("GOOGL"));
                put("TSLA", stockETFs.get("TSLA"));
                put("NVDA", stockETFs.get("NVDA"));
                put("KO", stockETFs.get("KO"));
                put("SPYD", stockETFs.get("SPYD"));
                put("VOO", stockETFs.get("VOO"));
                put("VTI", stockETFs.get("VTI"));
            }
        };

        Map<String, Double> shares = new HashMap<>() {
            {
                put("AAPL", 0.20*initial);
                put("AMZN", 0.15*initial);
                put("GOOGL", 0.05*initial);
                put("TSLA", 0.10*initial);
                put("NVDA", 0.15*initial);
                put("KO", 0.10*initial);
                put("SPYD", 0.15*initial);
                put("VOO", 0.10*initial);
                put("VTI", 0.10*initial);

            }
        };

        Portfolio dummyPortfolio = new Portfolio(
                initial,
                allocation,
                StopLoss,
                Risk,
                Target,
                threshold,
                UserStockETFMap,
                shares
        );

        return dummyPortfolio;
    }

    public static void main(String[] args) {
        UserTradingPattern();
    }
}
