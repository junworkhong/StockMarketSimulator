package project.processor;

import project.common.*;
import project.data.StockETFReader;

import java.util.Map;

public class BreakoutTradingPattern implements TradingStrategy {
    private static final DateCounter dates = new DateCounter();

//    private static Double initialInvestment;
//    private static Map<String, Integer> allocationMap;
//    private static int stopLoss;
//    private static int risk;
//    private static int target;
//    private static Map<String, Integer> threshold;

    public static void RunTradingPattern(Portfolio UserPortfolio) {
        Portfolio portfolio = UserPortfolio.copy();

        double initial = portfolio.getInitialInvestment();

        portfolio.addBudget((-1)*initial);

        Map<String, StockETF> userStocks = portfolio.getUserStockETFMap();
        MyDate firstDay = dates.getDateList().get(0);

        for (String ticker : portfolio.getUserStockETFMap().keySet()) {
            StockETF stock = portfolio.getUserStockETFMap().get(ticker);
            double day1Price = stock.getPriceMap().get(firstDay).getClose();

            double allocationPercent = portfolio.getAllocations().get(ticker) * 0.01;
            double amountDollars = allocationPercent * initial;
            double sharesToBuy = amountDollars / day1Price;

            portfolio.addShares(ticker, sharesToBuy);
            portfolio.addBuyPrice(ticker, day1Price);
        }

        double targetMultiplier = portfolio.getTarget() * 0.01;
        double stoplossMultiplier = portfolio.getStopLoss() * 0.01;

        for (MyDate date : dates.getDateList()) {
            for (Map.Entry<String, StockETF> entry : userStocks.entrySet()) {
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

                if (shares == 0) {
                    if (closePrice > thresholdPrice && (portfolio.getBudget() > risk)) {
                        Double amountToBuy = risk/closePrice;

                        portfolio.addBuyPrice(tickerName, closePrice);
                        portfolio.addShares(tickerName, amountToBuy);
                        portfolio.addBudget((-1)* risk);
                    }
                }else{
                    if (closePrice < buyPrice || closePrice < stoploss || closePrice > target) {
                        portfolio.addBudget(shares * closePrice);
                        portfolio.sellShares(tickerName);
                    }
                }

//                double currCash = portfolio.getBudget();
//                double currSharesValue = 0.0;
//
//                for (String ticker : portfolio.getUserStockETFMap().keySet()) {
//                    StockETF tempStock = portfolio.getUserStockETFMap().get(ticker);
//                    double lastClose = tempStock.getPriceMap().get(date).getClose();
//                    double sh = portfolio.getShares().get(ticker);
//                    currSharesValue += sh * lastClose;
//                }
//                double currTotal = currCash + currSharesValue;
//                double currReturnPercent = (currTotal / portfolio.getInitialInvestment() * 100.0);
//                DateResultMap.addDateResults(date, initial, currCash, currSharesValue, currTotal, currReturnPercent);
            }
        }

        MyDate lastDate = dates.getDateList().get(dates.getDateList().size() - 1);
        double finalCash = portfolio.getBudget();
        double finalSharesValue = 0.0;

        for (String ticker : portfolio.getUserStockETFMap().keySet()) {
            StockETF stock = portfolio.getUserStockETFMap().get(ticker);
            double lastClose = stock.getPriceMap().get(lastDate).getClose();
            double sh = portfolio.getShares().get(ticker);
            finalSharesValue += sh * lastClose;
        }

        double finalTotal = finalCash + finalSharesValue;

        System.out.println("\n==== BREAKOUT PATTERN RESULTS ====");
        System.out.println("(Breakout Pattern takes your trading pattern but also sells when closing price is lower than the buy price)");
        System.out.println("Final Budget: $" + finalCash);
        System.out.println("Final Shares Value: $" + finalSharesValue);
        System.out.println("Final Total Portfolio Value: $" + finalTotal);
//        System.out.println("Initial Investment: " + portfolio.getInitialInvestment());
        System.out.println("Total Return Percentage: " + (finalTotal / portfolio.getInitialInvestment() * 100.0) + "%");
        System.out.println("Total Profit/Loss: $" + (finalCash - portfolio.getInitialInvestment()));
        System.out.println("Final Shares: " + portfolio.getShares().toString());
//        System.out.println("\nFinal Shares: ");
//
//        for (Map.Entry<String, Double> entry : portfolio.getShares().entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
    }

//    public static void RunBreakoutTradingPattern(Portfolio portfolio) {
//        double initial = portfolio.getInitialInvestment();
//
//        portfolio.addBudget((-1)*initial);
//
//        Map<String, StockETF> userStocks = portfolio.getUserStockETFMap();
//        MyDate firstDay = dates.getDateList().get(0);
//
//        for (String ticker : portfolio.getUserStockETFMap().keySet()) {
//            StockETF stock = portfolio.getUserStockETFMap().get(ticker);
//            double day1Price = stock.getPriceMap().get(firstDay).getClose();
//
//            double allocationPercent = portfolio.getAllocations().get(ticker) * 0.01;
//            double amountDollars = allocationPercent * initial;
//            double sharesToBuy = amountDollars / day1Price;
//
//            portfolio.addShares(ticker, sharesToBuy);
//            portfolio.addBuyPrice(ticker, day1Price);
//        }
//
//        double targetMultiplier = portfolio.getTarget() * 0.01;
//        double stoplossMultiplier = portfolio.getStopLoss() * 0.01;
//
//        for (MyDate date : dates.getDateList()) {
//            for (Map.Entry<String, StockETF> entry : userStocks.entrySet()) {
//                String tickerName = entry.getKey();
//                StockETF stock = entry.getValue();
//
//                double closePrice = stock.getPriceMap().get(date).getClose();
//                double shares = portfolio.getShares().get(tickerName);
//
//                if (!portfolio.getBuyPrice().containsKey(tickerName)) {
//                    if (date.equals(firstDay)) {
//                        double firstClose = stock.getPriceMap().get(firstDay).getClose();
//                        portfolio.addBuyPrice(tickerName, firstClose);
//                    }else
//                        continue;
//                }
//
//                double buyPrice = portfolio.getBuyPrice().get(tickerName);
//                double target = targetMultiplier * buyPrice;
//                double stoploss = stoplossMultiplier * buyPrice;
//                double thresholdPrice = portfolio.getThreshold().get(tickerName);
//                double risk = portfolio.getRisk() * 0.01 * portfolio.getBudget();
//
//                if (shares == 0) {
//                    if (closePrice > thresholdPrice && (portfolio.getBudget() > risk)) {
//                        Double amountToBuy = risk/closePrice;
//
//                        portfolio.addBuyPrice(tickerName, closePrice);
//                        portfolio.addShares(tickerName, amountToBuy);
//                        portfolio.addBudget((-1)* risk);
//                    }
//                }else{
//                    if (closePrice < buyPrice || closePrice < stoploss || closePrice > target) {
//                        portfolio.addBudget(shares * closePrice);
//                        portfolio.sellShares(tickerName);
//                    }
//                }
//
//                double currCash = portfolio.getBudget();
//                double currSharesValue = 0.0;
//                StockETF tempStock = portfolio.getUserStockETFMap().get(tickerName);
//                double lastClose = tempStock.getPriceMap().get(date).getClose();
//                double sh = portfolio.getShares().get(tickerName);
//                double currTotal = currCash + currSharesValue;
//                double currReturnPercent = (currTotal / portfolio.getInitialInvestment() * 100.0);
//
//                currSharesValue += sh * lastClose;
//                DateResultMap.addDateResults(date, initial, currCash, currSharesValue, currTotal, currReturnPercent);
//            }
//        }
//
//        MyDate lastDate = dates.getDateList().get(dates.getDateList().size() - 1);
//        double finalCash = portfolio.getBudget();
//        double finalSharesValue = 0.0;
//
//        for (String ticker : portfolio.getUserStockETFMap().keySet()) {
//            StockETF stock = portfolio.getUserStockETFMap().get(ticker);
//            double lastClose = stock.getPriceMap().get(lastDate).getClose();
//            double sh = portfolio.getShares().get(ticker);
//            finalSharesValue += sh * lastClose;
//        }
//
//        double finalTotal = finalCash + finalSharesValue;
//
//        System.out.println("==== BREAKOUT PATTERN RESULTS ====");
//        System.out.println("Final Cash: " + finalCash);
//        System.out.println("Final Shares Value: " + finalSharesValue);
//        System.out.println("Final Total Portfolio Value: " + finalTotal);
//        System.out.println("Initial Investment: " + portfolio.getInitialInvestment());
//        System.out.println("Total Return %: " + (finalTotal / portfolio.getInitialInvestment() * 100.0));
//
//        System.out.println("Final Budget: " + portfolio.getBudget());
//        System.out.println("Final Shares Map: " + portfolio.getShares());
//    }
}
