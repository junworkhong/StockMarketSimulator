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

public class ExampleTradingPattern {
//    private static final boolean DEBUG = true;

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
        dummy.addBudget((-1)*initial); // = 4000

//        System.out.println("Initial budget: " + dummy.getBudget());
//        System.out.println("Initial investment (to be allocated): " + initial);
//        System.out.println("Budget after initial allocation: " + dummy.getBudget());
//        System.out.println("User thresholds: " + dummy.getThreshold());
//        System.out.println("Initial shares map: " + dummy.getShares());
//        System.out.println("---- START SIM ----");

        Map<String, StockETF> UserStocks = dummy.getUserStockETFMap();
        MyDate firstDay = dates.getDateList().get(0);

//        dummy.sellShares("AAPL");
//        dummy.sellShares("GOOGL");
//        dummy.sellShares("TSLA");
//        dummy.sellShares("NVDA");
//        dummy.sellShares("KO");
//        dummy.sellShares("SPYD");
//        dummy.sellShares("VOO");
//        dummy.sellShares("VTI");

//        dummy.addShares("AAPL", (0.20 * initial) / stockETFs.get("AAPL").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("AMZN", (0.15 * initial) / stockETFs.get("AMZN").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("GOOGL", (0.05 * initial) / stockETFs.get("GOOGL").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("TSLA", (0.10 * initial) / stockETFs.get("TSLA").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("NVDA", (0.15 * initial) / stockETFs.get("NVDA").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("KO", (0.10 * initial) / stockETFs.get("KO").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("SPYD", (0.15 * initial) / stockETFs.get("SPYD").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("VOO", (0.10 * initial) / stockETFs.get("VOO").getPriceMap().get(firstDay).getClose());
//        dummy.addShares("VTI", (0.10 * initial) / stockETFs.get("VTI").getPriceMap().get(firstDay).getClose());

        for (String ticker : dummy.getUserStockETFMap().keySet()) {
            StockETF stock = dummy.getUserStockETFMap().get(ticker);
            double day1Price = stock.getPriceMap().get(firstDay).getClose();

            double allocationPercent = dummy.getAllocations().get(ticker) * 0.01;
            double amountDollars = allocationPercent * initial;
            double sharesToBuy = amountDollars / day1Price;

            dummy.addShares(ticker, sharesToBuy);
            dummy.addBuyPrice(ticker, day1Price);
        }

        double targetMultiplier = dummy.getTarget() * 0.01;
        double stoplossMultiplier = dummy.getStopLoss() * 0.01;

//        int i = 0;
        for (MyDate date : dates.getDateList()) {
//            if (i == 30)
//                break;
//            if (DEBUG) {
//                System.out.println("\n===== DATE " + date + " =====");
//                System.out.println("Current budget: " + dummy.getBudget());
//            }

            for (Map.Entry<String, StockETF> entry : UserStocks.entrySet()) {
                String tickerName = entry.getKey();
                StockETF stock = entry.getValue();

                double closePrice = stock.getPriceMap().get(date).getClose();
                double shares = dummy.getShares().get(tickerName);

                if (!dummy.getBuyPrice().containsKey(tickerName)) {
                    if (date.equals(firstDay)) {
                        double firstClose = stock.getPriceMap().get(firstDay).getClose();
                        dummy.addBuyPrice(tickerName, firstClose);
                    }else
                        continue;
                }

                double buyPrice = dummy.getBuyPrice().get(tickerName);
                double target = targetMultiplier * buyPrice;
                double stoploss = stoplossMultiplier * buyPrice;
                double thresholdPrice = dummy.getThreshold().get(tickerName);
                double risk = dummy.getRisk() * 0.01 * dummy.getBudget();

//                System.out.println("===== DATE " + date + " =====");
//                System.out.println("Ticker: " + tickerName);
//                System.out.println("Close Price: " + closePrice);
//                System.out.println("Shares Before: " + shares);
//                System.out.println("Budget Before: " + dummy.getBudget());
//                System.out.println("BuyPrice: " + buyPrice);
//                System.out.println("Threshold: " + thresholdPrice);
//                System.out.println("Target Price: " + target);
//                System.out.println("Stoploss Price: " + stoploss);

                if (shares == 0) {
                    if (closePrice > thresholdPrice && (dummy.getBudget() > risk)) {
                        Double amountToBuy = risk/closePrice;
//                        Double dollarValueShares = amountToBuy * closePrice;

                        dummy.addBuyPrice(tickerName, closePrice);
                        dummy.addShares(tickerName, amountToBuy);
                        dummy.addBudget((-1)* risk);
//
//                        System.out.println("---- BUYING " + tickerName + " ----");
//                        System.out.println("Amount To Buy (shares): " + amountToBuy);
//                        System.out.println("Cost: " + risk);
//                        System.out.println("New Budget: " + (dummy.getBudget() - risk));
                    }
                }else{
                    if (closePrice < stoploss || closePrice > target) {
                        dummy.addBudget(shares * closePrice);
                        dummy.sellShares(tickerName);
//
//                        System.out.println("---- SELLING " + tickerName + " ----");
//                        System.out.println("Shares Sold: " + shares);
//                        System.out.println("Sale Value: " + (shares * closePrice));
//                        System.out.println("New Budget: " + dummy.getBudget());
                    }
                }
//                System.out.println("Shares After: " + dummy.getShares().get(tickerName));
//                System.out.println("Budget After: " + dummy.getBudget());
//                System.out.println("======================================");
            }
//            i++;
//            System.out.println("===== END OF DAY " + date + " =====");
//            System.out.println("Total Budget: " + dummy.getBudget());
//            System.out.println("--------------------------------------\n");
        }

        MyDate lastDate = dates.getDateList().get(dates.getDateList().size() - 1);
        double finalCash = dummy.getBudget();
        double finalSharesValue = 0.0;

        for (String ticker : dummy.getUserStockETFMap().keySet()) {
            StockETF stock = dummy.getUserStockETFMap().get(ticker);
            double lastClose = stock.getPriceMap().get(lastDate).getClose();
            double sh = dummy.getShares().get(ticker);
            finalSharesValue += sh * lastClose;
        }

        double finalTotal = finalCash + finalSharesValue;

        System.out.println("==== FINAL RESULTS ====");
        System.out.println("Final Cash: " + finalCash);
        System.out.println("Final Shares Value: " + finalSharesValue);
        System.out.println("Final Total Portfolio Value: " + finalTotal);
        System.out.println("Initial Investment: " + dummy.getInitialInvestment());
        System.out.println("Total Return %: " + (finalTotal / dummy.getInitialInvestment() * 100.0));

        System.out.println("Final Budget: " + dummy.getBudget());
        System.out.println("Final Shares Map: " + dummy.getShares());
    }

    public static Portfolio initializeDummyPortfolio() {
        double initial = 6000.0;
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
        int StopLoss = 85;
        int Risk = 30;
        int Target = 130;
        Map<String, Integer> threshold = new HashMap<>() {
            {
                put("AAPL", 40);
                put("AMZN", 80);
                put("GOOGL", 50);
                put("TSLA", 35);
                put("NVDA", 4);
                put("KO", 45);
                put("SPYD", 28);
                put("VOO", 260);
                put("VTI", 140);
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
                put("AAPL", 0.0);
                put("AMZN", 0.0);
                put("GOOGL", 0.0);
                put("TSLA", 0.0);
                put("NVDA", 0.0);
                put("KO", 0.0);
                put("SPYD", 0.0);
                put("VOO", 0.0);
                put("VTI", 0.0);

            }
        };

        DateResultMap dateResultMap = new DateResultMap();

        Portfolio dummyPortfolio = new Portfolio(
                initial,
                allocation,
                StopLoss,
                Risk,
                Target,
                threshold,
                UserStockETFMap,
                shares,
                dateResultMap
        );

        return dummyPortfolio;
    }

    public static void main(String[] args) {
        UserTradingPattern();
    }
}
