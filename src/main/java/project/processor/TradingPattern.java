package project.processor;

import project.common.*;
import project.data.StockETFReader;

import java.util.*;

public class TradingPattern implements TradingStrategy{
    private static final StockETFReader reader = StockETFReader.getInstance();
    private static final Map<String, StockETF> stockETFs = reader.readStockETFs();
    private static final DateCounter dates = new DateCounter();

    private static Double initialInvestment;
    private static Map<String, Integer> allocationMap;
    private static int stopLoss;
    private static int risk;
    private static int target;
    private static Map<String, Double> threshold;

    public Portfolio initializePortfolio() {
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

    public void RunTradingPattern(Portfolio portfolio){
//        Portfolio portfolio = initializePortfolio();

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
            portfolio.initializeShares(ticker, sharesToBuy);
            portfolio.addBuyPrice(ticker, day1Price);

            portfolio.getThreshold().put(ticker, day1Price * portfolio.getThreshold().get(ticker) * 0.01);
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

                if (shares == 0.0) {
                    if (closePrice > thresholdPrice && (portfolio.getBudget() > risk)) {
                        Double amountToBuy = risk/closePrice;

                        portfolio.addBuyPrice(tickerName, closePrice);
                        portfolio.addEndPrice(tickerName, closePrice);
                        portfolio.addShares(tickerName, amountToBuy);
                        portfolio.addBudget((-1)* risk);
                    }
                }else{
                    if (closePrice < stoploss || closePrice > target) {
                        portfolio.addBudget(shares * closePrice);
                        portfolio.sellShares(tickerName);
                    }
                }

                double currCash = portfolio.getBudget();
                double currSharesValue = 0.0;

                for (String ticker : portfolio.getUserStockETFMap().keySet()) {
                    StockETF tempStock = portfolio.getUserStockETFMap().get(ticker);
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
        portfolio.setTotalProfit(portfolio.getBudget() - portfolio.getInitialInvestment());

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

    public void setAllocationMap(Map<String, Integer> allocationMap) {
        TradingPattern.allocationMap = allocationMap;
    }

    public void setStopLoss(int stopLoss) {
        TradingPattern.stopLoss = stopLoss;
    }

    public void setRisk(int risk) {
        TradingPattern.risk = risk;
    }

    public void setTarget(int target) {
        TradingPattern.target = target;
    }

    public void setThreshold(Map<String, Double> threshold) {
        TradingPattern.threshold = threshold;
    }
}
