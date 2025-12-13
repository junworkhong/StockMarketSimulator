package project.processor;

import project.common.*;
import project.data.StockETFReader;

import java.util.*;

public class TradingPattern implements TradingStrategy{
    private static final StockETFReader reader = StockETFReader.getInstance();
    private static final Map<String, StockETF> stockETFs = reader.readStockETFs();
    private static final DateCounter dates = new DateCounter();

//    private static double initialInvestment;
//    private static Map<String, Integer> allocationMap;
//    private static int stopLoss;
//    private static int risk;
//    private static int target;
//    private static Map<String, Double> threshold;

//    public Portfolio initializePortfolio(double initialInvestment,
//                                         Map<String, Integer> allocations,
//                                         int stopLoss,
//                                         int Risk,
//                                         int Target,
//                                         Map<String, Double> Threshold) {
//
//        Map<String, StockETF> UserStockETFMap = new HashMap<>() {
//            {
//                for (Map.Entry<String, Integer> entry : allocations.entrySet()) {
//                    if (entry != null && entry.getKey() != null && entry.getValue() != null)
//                        put(entry.getKey(), stockETFs.get(entry.getKey()));
//                }
//            }
//        };
//
//        Map<String, Double> shares = new HashMap<>() {
//            {
//                for (Map.Entry<String, StockETF> entry : UserStockETFMap.entrySet()) {
//                    if (entry != null && entry.getKey() != null)
//                        put(entry.getKey(), 0.0);
//                }
//            }
//        };
//
//        DateResultMap dateResultMap = new DateResultMap();
//
//        if (dateResultMap == null)
//            throw new IllegalStateException("Problem with dateResultMap");
//
//        Portfolio userPortfolio = new Portfolio(
//                initialInvestment,
//                allocations,
//                stopLoss,
//                Risk,
//                Target,
//                Threshold,
//                UserStockETFMap,
//                shares,
//                dateResultMap
//        );
//
//        return userPortfolio;
//    }

    public void RunTradingPattern(Portfolio portfolio){
//        Portfolio portfolio = initializePortfolio();

        if (portfolio == null)
            throw new IllegalStateException("Invalid portfolio");

        double initial = portfolio.getInitialInvestment();

        portfolio.addBudget((-1)*initial);

        Map<String, StockETF> userStocks = portfolio.getUserStockETFMap();

        if (userStocks == null)
            throw new IllegalStateException("Stock/ETF csvs are invalid!");

        if (dates == null || dates.getDateList() == null)
            throw new IllegalStateException("Dates are invalid!");

        MyDate firstDay = dates.getDateList().get(0);

        if (firstDay == null)
            throw new IllegalStateException("First day is invalid!");

        for (String ticker : portfolio.getUserStockETFMap().keySet()) {
            if (ticker == null)
                continue;

            StockETF stock = portfolio.getUserStockETFMap().get(ticker);
            if (stock == null)
                continue;

            double day1Price = stock.getPriceMap().get(firstDay).getClose();

            if (portfolio.getAllocations() == null)
                continue;

            double allocationPercent = portfolio.getAllocations().get(ticker) * 0.01;
            double amountDollars = allocationPercent * initial;
            double sharesToBuy = amountDollars / day1Price;

            portfolio.addShares(ticker, sharesToBuy);
            portfolio.initializeShares(ticker, sharesToBuy);
            portfolio.addBuyPrice(ticker, day1Price);
            portfolio.addDayOnePrice(ticker, day1Price);

            portfolio.addTotalBuyDollars(ticker, 0.0);
            portfolio.addTotalSellDollars(ticker, 0.0);

            portfolio.getThreshold().put(ticker, day1Price * portfolio.getThreshold().get(ticker) * 0.01);
        }

        double targetMultiplier = portfolio.getTarget() * 0.01;
        double stoplossMultiplier = portfolio.getStopLoss() * 0.01;

        for (MyDate date : dates.getDateList()) {
            if (date == null)
                continue;

            for (Map.Entry<String, StockETF> entry : userStocks.entrySet()) {
                if (entry == null || entry.getKey() == null || entry.getValue() == null)
                    continue;

                String tickerName = entry.getKey();
                StockETF stock = entry.getValue();

                double closePrice = stock.getPriceMap().get(date).getClose();
                double shares = portfolio.getShares().get(tickerName);

                if (!portfolio.getBuyPrice().containsKey(tickerName)) {
                    if (date.equals(firstDay)) {
                        double firstClose = stock.getPriceMap().get(firstDay).getClose();
                        portfolio.addBuyPrice(tickerName, firstClose);
                    }else
                        continue;
                }

                double buyPrice = portfolio.getBuyPrice().get(tickerName);
                double target = targetMultiplier * buyPrice;
                double stoploss = stoplossMultiplier * buyPrice;
                double thresholdPrice = portfolio.getThreshold().get(tickerName);
                double risk = portfolio.getRisk() * 0.01 * portfolio.getBudget();

                if (shares < 10.0) {
                    if (closePrice > thresholdPrice && portfolio.getBudget() >= risk) {
                        double amountToBuy = risk / closePrice;

                        portfolio.addBuyPrice(tickerName, closePrice);
                        portfolio.addEndPrice(tickerName, closePrice);
                        portfolio.addShares(tickerName, amountToBuy);
                        portfolio.addBudget(-risk);

                        portfolio.addTotalBuyDollars(
                                tickerName,
                                portfolio.getTotalBuyDollars().get(tickerName) + risk
                        );
                    }
                }
                else {
                    if (closePrice < stoploss || closePrice > target) {
//                        portfolio.addBudget(shares * closePrice);
//                        portfolio.sellShares(tickerName);
                        portfolio.addBudget(shares * closePrice * 0.25);
                        portfolio.addShares(tickerName, -shares * 0.25);

                        portfolio.addTotalSellDollars(
                                tickerName,
                                portfolio.getTotalSellDollars().get(tickerName) + (shares * closePrice * 0.25)
                        );
                    }
                }

                double currCash = portfolio.getBudget();
                double currSharesValue = 0.0;

                for (String ticker : portfolio.getUserStockETFMap().keySet()) {
                    if (ticker == null)
                        continue;
                    StockETF tempStock = portfolio.getUserStockETFMap().get(ticker);
                    if (tempStock == null || tempStock.getPriceMap() == null)
                        continue;
                    double lastClose = tempStock.getPriceMap().get(date).getClose();
                    double sh = portfolio.getShares().get(ticker);
                    currSharesValue += sh * lastClose;
                }
                double currTotal = currCash + currSharesValue;
                double currReturnPercent = (currTotal / portfolio.getInitialInvestment() * 100.0);
                DateResultMap.addDateResults(date, initial, currCash, currSharesValue, currTotal, currReturnPercent, portfolio.getShares());
            }
        }

        MyDate lastDate = dates.getDateList().get(dates.getDateList().size() - 1);
        if (lastDate == null)
            throw new IllegalStateException("Issue with last date on csv");

        double finalCash = portfolio.getBudget();
        double finalSharesValue = 0.0;

        for (String ticker : portfolio.getUserStockETFMap().keySet()) {
            StockETF stock = portfolio.getUserStockETFMap().get(ticker);
            double lastClose = stock.getPriceMap().get(lastDate).getClose();
            double sh = portfolio.getShares().get(ticker);
            finalSharesValue += sh * lastClose;
        }

        double finalTotal = finalCash + finalSharesValue;

        portfolio.setFinalSharesValue(finalSharesValue);
        portfolio.setFinalTotal(finalTotal);
        portfolio.setTotalReturnPercentage((finalTotal) / portfolio.getInitialInvestment() * 100.0);
        portfolio.setTotalProfit(finalTotal - portfolio.getInitialInvestment());

//        System.out.println("==== FINAL RESULTS ====");
////        System.out.println("Final Cash: " + finalCash);
//        System.out.println("Final Shares Value: " + finalSharesValue);
//        System.out.println("Final Total Portfolio Value: " + finalTotal);
//        System.out.println("Initial Investment: " + userPortfolio.getInitialInvestment());
//        System.out.println("Total Return %: " + (finalTotal / userPortfolio.getInitialInvestment() * 100.0));
//
//        System.out.println("Final Budget: " + userPortfolio.getBudget());
//        System.out.println("Final Shares Map: " + userPortfolio.getShares());
    }

//    public void setAllocationMap(Map<String, Integer> allocationMap) {
//        TradingPattern.allocationMap = allocationMap;
//    }
//
//    public void setStopLoss(int stopLoss) {
//        TradingPattern.stopLoss = stopLoss;
//    }
//
//    public void setRisk(int risk) {
//        TradingPattern.risk = risk;
//    }
//
//    public void setTarget(int target) {
//        TradingPattern.target = target;
//    }
//
//    public void setThreshold(Map<String, Double> threshold) {
//        TradingPattern.threshold = threshold;
//    }
}
