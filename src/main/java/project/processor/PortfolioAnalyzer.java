package project.processor;

import project.common.*;

import java.util.*;

/**
 * Utility class for portfolio-related calculations.
 *
 * In this step we only implement:
 *   Operation 2: profit/loss percentage for a chosen stock/ETF.
 *
 * Result will be stored in Portfolio.perStockETFStats:
 *   key   = ticker symbol (e.g. "AAPL")
 *   value = List<Double> with:
 *           index 0 -> current market value
 *           index 1 -> profit/loss amount
 *           index 2 -> profit/loss percentage
 */
public class PortfolioAnalyzer {

    /**
     * Operation 2:
     * Calculate profit/loss information for a single stock/ETF on a given date.
     *
     * @param portfolio the user's portfolio
     * @param ticker    the stock/ETF symbol (e.g. "AAPL")
     * @param date      the date for which we want P/L (must exist in the StockETF price map)
     */
    public void calculatePerStockStats(Portfolio portfolio, String ticker, MyDate date) {
        if (portfolio == null || ticker == null || date == null) {
            throw new IllegalArgumentException("Portfolio, ticker, and date must not be null");
        }

        Map<String, StockETF> userMap = portfolio.getUserStockETFMap();
        if (!userMap.containsKey(ticker)) {
            throw new IllegalArgumentException("Unknown ticker: " + ticker);
        }

        StockETF stock = userMap.get(ticker);
        PriceDate priceDate = stock.getPriceMap().get(date);
        if (priceDate == null) {
            throw new IllegalStateException("No price data for " + ticker + " on date " + date);
        }

        double currentPrice = priceDate.getClose();
        double shares = portfolio.getShares().getOrDefault(ticker, 0.0);

        // If no position, everything is zero
        if (shares <= 0) {
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

    /**
     * Operation 3:
     * Compare the portfolio performance to the S&P 500 over a given period.
     *
     * It will:
     *  - compute portfolio profit/loss and profit/loss % based on InitialInvestment
     *  - compute S&P 500 return % between startDate and endDate
     *  - store both and the over/under difference inside Portfolio:
     *      totalProfitLoss
     *      totalProfitLossPercent
     *      SPReturn
     *      SPReturnOverUnder
     *
     * @param portfolio the user's portfolio
     * @param sp500     the S&P 500 index data
     * @param startDate start date of the comparison period
     * @param endDate   end date of the comparison period
     */
    public void compareWithSP500(Portfolio portfolio,
                                 SAndPIndex sp500,
                                 MyDate startDate,
                                 MyDate endDate) {

        if (portfolio == null || sp500 == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Portfolio, S&P index, and dates must not be null");
        }

        double initialInvestment = portfolio.getInitialInvestment();

        // Final portfolio value on endDate = cash + value of all holdings
        double finalPortfolioValue = calculatePortfolioValueOnDate(portfolio, endDate);

        double profitLoss = finalPortfolioValue - initialInvestment;
        double profitLossPercent =
                (initialInvestment == 0.0) ? 0.0 : (profitLoss / initialInvestment * 100.0);

        portfolio.setTotalProfitLoss(profitLoss);
        portfolio.setTotalProfitLossPercent(profitLossPercent);

        // S&P 500 return over the same period
        PriceDate spStart = sp500.getPriceMap().get(startDate);
        PriceDate spEnd = sp500.getPriceMap().get(endDate);

        if (spStart == null || spEnd == null) {
            throw new IllegalStateException("Missing S&P 500 data for the given dates");
        }

        double spStartClose = spStart.getClose();
        double spEndClose = spEnd.getClose();

        if (spStartClose <= 0.0) {
            throw new IllegalStateException("Invalid S&P 500 start close price");
        }

        double spReturnPercent = (spEndClose - spStartClose) / spStartClose * 100.0;
        portfolio.setSPReturn(spReturnPercent);

        // Over/Under = portfolio return % - S&P return %
        portfolio.setSPReturnOverUnder(profitLossPercent - spReturnPercent);
    }

    /**
     * Helper method used by Operation 3 (and can be reused by others):
     * Computes the total portfolio value on a given date.
     *
     * total value = cash (Budget) + sum over all stocks/ETFs (shares * close price on that date)
     */
    private double calculatePortfolioValueOnDate(Portfolio portfolio, MyDate date) {
        if (portfolio == null || date == null) {
            throw new IllegalArgumentException("Portfolio and date must not be null");
        }

        double total = portfolio.getBudget(); // current cash

        for (Map.Entry<String, StockETF> entry : portfolio.getUserStockETFMap().entrySet()) {
            String ticker = entry.getKey();
            StockETF stock = entry.getValue();

            double shares = portfolio.getShares().getOrDefault(ticker, 0.0);
            if (shares <= 0.0) {
                continue;
            }

            PriceDate priceDate = stock.getPriceMap().get(date);
            if (priceDate == null) {
                // no price for this date -> skip
                continue;
            }

            double close = priceDate.getClose();
            total += shares * close;
        }

        return total;
    }


    /**
     * Operation 4:
     * Run the user trading pattern simulation on the given portfolio
     * and store the total strategy return percentage in the portfolio.
     *
     * Result is written into:
     *   portfolio.strategyReturn
     */
    public void runUserTradingPattern(Portfolio portfolio) {
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio must not be null");
        }

        double finalTotal = TradingPatterns.runUserTradingPattern(portfolio);
        double initial = portfolio.getInitialInvestment();

        double strategyReturnPercent =
                (initial == 0.0) ? 0.0 : ((finalTotal - initial) / initial * 100.0);

        portfolio.setStrategyReturn(strategyReturnPercent);
    }

}
