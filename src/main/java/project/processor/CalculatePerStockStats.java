package project.processor;

import project.common.MyDate;
import project.common.Portfolio;
import project.common.PriceDate;
import project.common.StockETF;
import project.ui.SimulatorUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CalculatePerStockStats {
    public void execute(Portfolio portfolio, String ticker, MyDate date) {

        if (portfolio == null || ticker == null || date == null) {
            throw new IllegalArgumentException("Portfolio, ticker, and date must not be null");
        }

        ticker = ticker.toUpperCase();

        Map<String, StockETF> userMap = portfolio.getUserStockETFMap();

        if (portfolio.getUserStockETFMap() == null)
            throw new IllegalStateException("Try a new date");

        if (!userMap.containsKey(ticker)) {
            throw new IllegalArgumentException("Unknown ticker: " + ticker);
        }

        if (!date.isValidDate(date))
            throw new IllegalArgumentException("Invalid date: " + date);

        StockETF stock = userMap.get(ticker);
        PriceDate priceDate = stock.getPriceMap().get(date);
        if (priceDate == null) {
            throw new IllegalStateException("No price data for " + ticker + " on date " + date);
        }

        double currentPrice = priceDate.getClose();
        double shares = portfolio.getShares().getOrDefault(ticker, 0.0);

        // If no position, everything is zero
        if (shares <= 0.0) {
            ensurePerStockStatsMap(portfolio);
            portfolio.getPerStockETFStats().put(
                    ticker,
                    Arrays.asList(0.0, 0.0, 0.0)
            );
            return;
        }

        // Buy price must exist for this ticker in the portfolio
        Double buyPrice = portfolio.getBuyPrice().get(ticker);
        if (buyPrice == null || buyPrice <= 0) {
            throw new IllegalStateException("Buy price not recorded for ticker: " + ticker);
        }

        double cost = shares * buyPrice;
        double currentValue = shares * currentPrice;
        double profitLoss = currentValue - cost;
        double profitLossPercent = (cost == 0.0) ? 0.0 : (profitLoss / cost * 100.0);

        ensurePerStockStatsMap(portfolio);
        portfolio.getPerStockETFStats().put(
                ticker,
                Arrays.asList(currentValue, profitLoss, profitLossPercent)
        );
    }

    private void ensurePerStockStatsMap(Portfolio portfolio) {
        if (portfolio.getPerStockETFStats() == null) {
            portfolio.setPerStockETFStats(new HashMap<>());
        }
    }
}
