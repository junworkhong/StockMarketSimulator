package project.processor;

import project.common.*;

import java.util.Map;

public class MomentumTradingPattern implements TradingStrategy{
    private static final DateCounter dates = new DateCounter();

    public static void RunTradingPattern(Portfolio portfolio) {
        double initial = portfolio.getInitialInvestment();

        portfolio.addBudget((-1)*initial);

        Map<String, StockETF> userStocks = portfolio.getUserStockETFMap();
        MyDate firstDay = dates.getDateList().get(0);

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

        double targetMultiplier = portfolio.getTarget() * 0.01;
        double stoplossMultiplier = portfolio.getStopLoss() * 0.01;

        for (MyDate date : dates.getDateList()) {
            int i = dates.getDateList().indexOf(date);

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

                double yesterdayClose = 0.0;
                double twoDaysClose = 0.0;

                if (!date.equals(firstDay)) {
                    MyDate yesterday = dates.getDateList().get(i-1);
                    yesterdayClose = stock.getPriceMap().get(yesterday).getClose();
                }

                if (i > 1) {
                    MyDate twoDaysAgo = dates.getDateList().get(i-2);
                    twoDaysClose = stock.getPriceMap().get(twoDaysAgo).getClose();
                }

                boolean momentumBuy = false;
                boolean momentumSell = false;

                double calculation1 = (closePrice - yesterdayClose) / yesterdayClose;
                double calculation2 = (yesterdayClose - twoDaysClose)  / twoDaysClose;

//                && (i > 1 && calculation2 >= 0.01)
                if (yesterdayClose > 0 && twoDaysClose > 0) {
                    momentumBuy = (closePrice > yesterdayClose && yesterdayClose > twoDaysClose);
                    momentumSell = (closePrice < yesterdayClose * 0.98);
                }


                if (shares == 0) {
                    if (portfolio.getBudget() > risk && momentumBuy) {
                        Double amountToBuy = risk/closePrice;

                        portfolio.addBuyPrice(tickerName, closePrice);
                        portfolio.addShares(tickerName, amountToBuy);
                        portfolio.addBudget((-1)* risk);
                    }
                }else{
                    if (i < dates.getDateList().size() - 1 && momentumSell) {
                        portfolio.addBudget(shares * closePrice);
                        portfolio.sellShares(tickerName);
                    }
                }

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

        System.out.println("==== MOMENTUM PATTERN RESULTS ====");
//        System.out.println("Final Cash: " + finalCash);
        System.out.println("Final Shares Value: " + finalSharesValue);
        System.out.println("Final Total Portfolio Value: " + finalTotal);
        System.out.println("Initial Investment: " + portfolio.getInitialInvestment());
        System.out.println("Total Return %: " + (finalTotal / portfolio.getInitialInvestment() * 100.0));

        System.out.println("Final Budget: " + portfolio.getBudget());
        System.out.println("Final Shares Map: " + portfolio.getShares());
    }
    }

//    public static void RunMomentumTradingPattern(Portfolio portfolio) {
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
//        int i = 1;
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
//                double yesterdayClose = 0.0;
//
//                if (!date.equals(firstDay)) {
//                    MyDate yesterday = dates.getDateList().get(i-1);
//                    yesterdayClose = stock.getPriceMap().get(yesterday).getClose();
//                }else {
//                    yesterdayClose = -1.0;
//                }
//
//                boolean momentum;
//
//                if (yesterdayClose > 0 && closePrice > yesterdayClose)
//                    momentum = true;
//                else
//                    momentum = false;
//
//                if (shares == 0) {
//                    if (closePrice > thresholdPrice && (portfolio.getBudget() > risk) && momentum) {
//                        Double amountToBuy = risk/closePrice;
//
//                        portfolio.addBuyPrice(tickerName, closePrice);
//                        portfolio.addShares(tickerName, amountToBuy);
//                        portfolio.addBudget((-1)* risk);
//                    }
//                }else{
//                    if (closePrice < stoploss || closePrice > target) {
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
//            i++;
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
//        System.out.println("==== MOMENTUM PATTERN RESULTS ====");
//        System.out.println("Final Cash: " + finalCash);
//        System.out.println("Final Shares Value: " + finalSharesValue);
//        System.out.println("Final Total Portfolio Value: " + finalTotal);
//        System.out.println("Initial Investment: " + portfolio.getInitialInvestment());
//        System.out.println("Total Return %: " + (finalTotal / portfolio.getInitialInvestment() * 100.0));
//
//        System.out.println("Final Budget: " + portfolio.getBudget());
//        System.out.println("Final Shares Map: " + portfolio.getShares());
//    }
//}
