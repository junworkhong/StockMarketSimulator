package project.processor;

import project.common.*;
import java.util.Map;

/**
 * Operation 4 implementation:
 * Runs the user trading pattern on a given portfolio and
 * returns the final total portfolio value (cash + shares).
 */
public class RunUserTradingPattern {

    private static final DateCounter dates = new DateCounter();

    public static double execute(Portfolio portfolio) {
        double initial = portfolio.getInitialInvestment();
        portfolio.addBudget(-initial); // deduct initial investment

        Map<String, StockETF> userStocks = portfolio.getUserStockETFMap();
        MyDate firstDay = dates.getDateList().get(0);

        // initial allocation using first day price
        for (String ticker : userStocks.keySet()) {
            StockETF stock = userStocks.get(ticker);
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
                String ticker = entry.getKey();
                StockETF stock = entry.getValue();

                double closePrice = stock.getPriceMap().get(date).getClose();
                double shares = portfolio.getShares().get(ticker);

                if (!portfolio.getBuyPrice().containsKey(ticker)) {
                    if (date.equals(firstDay)) {
                        double firstClose = stock.getPriceMap().get(firstDay).getClose();
                        portfolio.addBuyPrice(ticker, firstClose);
                    } else {
                        continue;
                    }
                }

                double buyPrice = portfolio.getBuyPrice().get(ticker);
                double target = targetMultiplier * buyPrice;
                double stoploss = stoplossMultiplier * buyPrice;
                double thresholdPrice = portfolio.getThreshold().get(ticker);
                double risk = portfolio.getRisk() * 0.01 * portfolio.getBudget();

                if (shares == 0.0) {
                    if (closePrice > thresholdPrice && portfolio.getBudget() > risk) {
                        double amountToBuy = risk / closePrice;
                        portfolio.addBuyPrice(ticker, closePrice);
                        portfolio.addShares(ticker, amountToBuy);
                        portfolio.addBudget(-risk);
                    }
                } else {
                    if (closePrice < stoploss || closePrice > target) {
                        portfolio.addBudget(shares * closePrice);
                        portfolio.sellShares(ticker);
                    }
                }
            }
        }

        // Final total = cash + value of remaining shares
        MyDate lastDate = dates.getDateList().get(dates.getDateList().size() - 1);
        double finalCash = portfolio.getBudget();
        double finalSharesValue = 0.0;

        for (String ticker : userStocks.keySet()) {
            StockETF stock = userStocks.get(ticker);
            double lastClose = stock.getPriceMap().get(lastDate).getClose();
            double sh = portfolio.getShares().get(ticker);
            finalSharesValue += sh * lastClose;
        }

        return finalCash + finalSharesValue;
    }
}
