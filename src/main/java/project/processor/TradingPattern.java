package project.processor;

import project.common.*;
import project.data.StockETFReader;

import java.util.*;

public abstract class TradingPattern {
    private static final StockETFReader reader = StockETFReader.getInstance();
    private static final Map<String, StockETF> stockETFs = reader.readStockETFs();
    private static final DateCounter dates = new DateCounter();

    private static Double initialInvestment;
    private static Map<String, Integer> allocationMap;
    private static int stopLoss;
    private static int risk;
    private static int target;
    private static Map<String, Integer> threshold;

    private static Portfolio initializePortfolio() {
        Map<String, StockETF> UserStockETFMap = new HashMap<>() {
            {
                for (Map.Entry<String, Integer> entry : allocationMap.entrySet())
                    put(entry.getKey(), stockETFs.get(entry.getKey()));
            }
        };

        Map<String, Double> shares = new HashMap<>() {
            {
                for (Map.Entry<String, StockETF> entry : UserStockETFMap.entrySet())
                    put(entry.getKey(), 0.0);
            }
        };

        DateResultMap dateResultMap = new DateResultMap();

        Portfolio userPortfolio = new Portfolio(
                initialInvestment,
                allocationMap,
                stopLoss,
                risk,
                target,
                threshold,
                UserStockETFMap,
                shares,
                dateResultMap
        );

        return userPortfolio;
    }

    public static void RunTradingPattern() {
        Portfolio userPortfolio = initializePortfolio();
        double budget = userPortfolio.getBudget();
        double initial = userPortfolio.getInitialInvestment();

        userPortfolio.addBudget((-1)*initial);

        Map<String, StockETF> userStocks = userPortfolio.getUserStockETFMap();
        MyDate firstDay = dates.getDateList().get(0);

        for (String ticker : userPortfolio.getUserStockETFMap().keySet()) {
            StockETF stock = userPortfolio.getUserStockETFMap().get(ticker);
            double day1Price = stock.getPriceMap().get(firstDay).getClose();

            double allocationPercent = userPortfolio.getAllocations().get(ticker) * 0.01;
            double amountDollars = allocationPercent * initial;
            double sharesToBuy = amountDollars / day1Price;

            userPortfolio.addShares(ticker, sharesToBuy);
            userPortfolio.addBuyPrice(ticker, day1Price);
        }

        double targetMultiplier = userPortfolio.getTarget() * 0.01;
        double stoplossMultiplier = userPortfolio.getStopLoss() * 0.01;

        for (MyDate date : dates.getDateList()) {
            for (Map.Entry<String, StockETF> entry : userStocks.entrySet()) {
                String tickerName = entry.getKey();
                StockETF stock = entry.getValue();

                double closePrice = stock.getPriceMap().get(date).getClose();
                double shares = userPortfolio.getShares().get(tickerName);

                if (!userPortfolio.getBuyPrice().containsKey(tickerName)) {
                    if (date.equals(firstDay)) {
                        double firstClose = stock.getPriceMap().get(firstDay).getClose();
                        userPortfolio.addBuyPrice(tickerName, firstClose);
                    }else
                        continue;
                }

                double buyPrice = userPortfolio.getBuyPrice().get(tickerName);
                double target = targetMultiplier * buyPrice;
                double stoploss = stoplossMultiplier * buyPrice;
                double thresholdPrice = userPortfolio.getThreshold().get(tickerName);
                double risk = userPortfolio.getRisk() * 0.01 * userPortfolio.getBudget();

                if (shares == 0) {
                    if (closePrice > thresholdPrice && (userPortfolio.getBudget() > risk)) {
                        Double amountToBuy = risk/closePrice;

                        userPortfolio.addBuyPrice(tickerName, closePrice);
                        userPortfolio.addShares(tickerName, amountToBuy);
                        userPortfolio.addBudget((-1)* risk);
                    }
                }else{
                    if (closePrice < stoploss || closePrice > target) {
                        userPortfolio.addBudget(shares * closePrice);
                        userPortfolio.sellShares(tickerName);
                    }
                }

                double currCash = userPortfolio.getBudget();
                double currSharesValue = 0.0;
                StockETF tempStock = userPortfolio.getUserStockETFMap().get(tickerName);
                double lastClose = tempStock.getPriceMap().get(date).getClose();
                double sh = userPortfolio.getShares().get(tickerName);
                double currTotal = currCash + currSharesValue;
                double currReturnPercent = (currTotal / userPortfolio.getInitialInvestment() * 100.0);

                currSharesValue += sh * lastClose;
                DateResultMap.addDateResults(date, initial, currCash, currSharesValue, currTotal, currReturnPercent);
            }
        }

        MyDate lastDate = dates.getDateList().get(dates.getDateList().size() - 1);
        double finalCash = userPortfolio.getBudget();
        double finalSharesValue = 0.0;

        for (String ticker : userPortfolio.getUserStockETFMap().keySet()) {
            StockETF stock = userPortfolio.getUserStockETFMap().get(ticker);
            double lastClose = stock.getPriceMap().get(lastDate).getClose();
            double sh = userPortfolio.getShares().get(ticker);
            finalSharesValue += sh * lastClose;
        }

        double finalTotal = finalCash + finalSharesValue;

        System.out.println("==== FINAL RESULTS ====");
        System.out.println("Final Cash: " + finalCash);
        System.out.println("Final Shares Value: " + finalSharesValue);
        System.out.println("Final Total Portfolio Value: " + finalTotal);
        System.out.println("Initial Investment: " + userPortfolio.getInitialInvestment());
        System.out.println("Total Return %: " + (finalTotal / userPortfolio.getInitialInvestment() * 100.0));

        System.out.println("Final Budget: " + userPortfolio.getBudget());
        System.out.println("Final Shares Map: " + userPortfolio.getShares());
    }

    public static void setInitialInvestment(Double initialInvestment) {
        TradingPattern.initialInvestment = initialInvestment;
    }

    public static void setAllocationMap(Map<String, Integer> allocationMap) {
        TradingPattern.allocationMap = allocationMap;
    }

    public static void setStopLoss(int stopLoss) {
        TradingPattern.stopLoss = stopLoss;
    }

    public static void setRisk(int risk) {
        TradingPattern.risk = risk;
    }

    public static void setTarget(int target) {
        TradingPattern.target = target;
    }

    public static void setThreshold(Map<String, Integer> threshold) {
        TradingPattern.threshold = threshold;
    }
}
