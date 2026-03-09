package project.processor;

import project.common.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MomentumTradingPattern implements TradingStrategy{
    private static final DateCounter dates = new DateCounter();

    public void RunTradingPattern(Portfolio UserPortfolio) {
        if (UserPortfolio == null || dates == null || dates.getDateList() == null) {
            throw new IllegalStateException("UserPortfolio or date csv is null");
        }

        Portfolio portfolio = UserPortfolio.copy();
        if (portfolio == null)
            throw new IllegalStateException("Issue with copy method in Portfolio class");

        double initial = portfolio.getInitialInvestment();

        portfolio.addBudget((-1)*initial);

        Map<String, StockETF> userStocks = portfolio.getUserStockETFMap();
        MyDate firstDay = dates.getDateList().get(0);

        if (userStocks == null)
            throw new IllegalStateException("Stock/ETF csvs are invalid!");

        if (firstDay == null)
            throw new IllegalStateException("First day is invalid!");

        double targetMultiplier = portfolio.getTarget() * 0.01;
        double stoplossMultiplier = portfolio.getStopLoss() * 0.01;

        List<String> helperList = new ArrayList<>();
        helperList.addAll(portfolio.getUserStockETFMap().keySet());

        for (MyDate date : dates.getDateList()) {
            if (date == null)
                continue;

            boolean hi = true;
            for (String s : helperList) {
                if (s == null || userStocks.get(s) == null || !userStocks.get(s).getPriceMap().containsKey(date) || userStocks.get(s).getPriceMap().get(date) == null)
                    hi = false;
            }
            if (!hi)
                continue;

            int i = dates.getDateList().indexOf(date);

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

                double risk = portfolio.getRisk() * 0.01 * portfolio.getBudget();

                double yesterdayClose = 0.0;
                double twoDaysClose = 0.0;

                if (!date.equals(firstDay)) {
                    MyDate yesterday = dates.getDateList().get(i-1);
                    if (yesterday == null)
                        break;
                    if (stock.getPriceMap() == null)
                        throw new IllegalStateException("Stock/ETF csvs are invalid!");
                    if (stock.getPriceMap().get(yesterday) == null)
                        break;
                    yesterdayClose = stock.getPriceMap().get(yesterday).getClose();
                }

                if (i > 1) {
                    MyDate twoDaysAgo = dates.getDateList().get(i-2);
                    if (twoDaysAgo == null)
                        break;
                    if (stock.getPriceMap() == null)
                        throw new IllegalStateException("Stock/ETF csvs are invalid!");
                    if (stock.getPriceMap().get(twoDaysAgo) == null)
                        break;
                    twoDaysClose = stock.getPriceMap().get(twoDaysAgo).getClose();
                }

                boolean momentumBuy = false;
                boolean momentumSell = false;

                if (yesterdayClose > 0 && twoDaysClose > 0) {
                    momentumBuy = (closePrice > yesterdayClose && yesterdayClose > twoDaysClose);
                    momentumSell = (closePrice < yesterdayClose * 0.98);
                }


                if (shares == 0) {
                    if (portfolio.getBudget() > risk && momentumBuy) {
                        double amountToBuy = risk/closePrice;

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
            }
        }

        MyDate lastDate = dates.getDateList().get(dates.getDateList().size() - 1);
        if (lastDate == null)
            throw new IllegalStateException("Issue with last date on csv");

        double finalCash = portfolio.getBudget();
        double finalSharesValue = 0.0;

        for (String ticker : portfolio.getUserStockETFMap().keySet()) {
            if (ticker == null)
                continue;
            StockETF stock = portfolio.getUserStockETFMap().get(ticker);
            if (stock == null || stock.getPriceMap() == null)
                continue;
            double lastClose = stock.getPriceMap().get(lastDate).getClose();
            double sh = portfolio.getShares().get(ticker);
            finalSharesValue += sh * lastClose;
        }

        double finalTotal = finalCash + finalSharesValue;

        System.out.println("\n==== MOMENTUM PATTERN RESULTS ====");
        System.out.println("(Momentum Pattern buys shares when today's closing price is higher than yesterday's closing price, and yesterday's closing price is also higher than the closing price 2 days ago. It sells when today's closing price is lower than yesterday's closing price)");
        System.out.println("\nFinal Shares: " + portfolio.getShares().toString());
        System.out.println("Final Budget: $" + finalCash);
        System.out.println("Final Shares Value: $" + finalSharesValue);
        System.out.println("Final Total Portfolio Value: $" + finalTotal);
        System.out.println("Total Return Percentage: " + (finalTotal / portfolio.getInitialInvestment() * 100.0) + "%");
        System.out.println("Total Profit/Loss: $" + (finalTotal - portfolio.getInitialInvestment()));
    }
}

