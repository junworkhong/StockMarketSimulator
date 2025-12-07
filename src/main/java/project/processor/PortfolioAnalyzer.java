package project.processor;

import project.common.*;

/**
 * Utility class for portfolio-related calculations.
 *
 * It delegates actual logic to:
 *   - CalculatePerStockStats  (Operation 2)
 *   - CompareWithSP500        (Operation 3)
 *   - RunUserTradingPattern   (Operation 4)
 */
public class PortfolioAnalyzer {

    /**
     * Operation 2:
     * Calculate profit/loss information for a single stock/ETF on a given date.
     */
    public void calculatePerStockStats(Portfolio portfolio, String ticker, MyDate date) {
        CalculatePerStockStats.execute(portfolio, ticker, date);
    }

    /**
     * Operation 3:
     * Compare the portfolio performance to the S&P 500 over a given period.
     */
    public void compareWithSP500(Portfolio portfolio,
                                 SAndPIndex sp500,
                                 MyDate startDate,
                                 MyDate endDate) {
        CompareWithSP500.execute(portfolio, sp500, startDate, endDate);
    }

    /**
     * Operation 4:
     * Run the user trading pattern simulation on the given portfolio
     * and store the total strategy return percentage in the portfolio.
     */
    public void runUserTradingPattern(Portfolio portfolio) {
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio must not be null");
        }

        double finalTotal = RunUserTradingPattern.execute(portfolio);
        double initial = portfolio.getInitialInvestment();

        double strategyReturnPercent =
                (initial == 0.0) ? 0.0 : ((finalTotal - initial) / initial * 100.0);

        portfolio.setStrategyReturn(strategyReturnPercent);
    }
}
