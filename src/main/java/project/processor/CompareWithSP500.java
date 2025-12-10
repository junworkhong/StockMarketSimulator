package project.processor;

import project.common.*;
import project.data.BenchmarkReader;
import project.data.StockETFReader;

import java.util.Map;

public class CompareWithSP500 {
    private static final BenchmarkReader reader = BenchmarkReader.getInstance();
    private static final SAndPIndex sp500 = reader.readSAndP();

    public void execute(Portfolio portfolio,
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
     * Computes the total portfolio value on a given date.
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
}
