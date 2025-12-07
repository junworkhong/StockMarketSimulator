package project;

import project.common.MyDate;
import project.common.Portfolio;
import project.common.DateCounter;
import project.common.SAndPIndex;
import project.common.PriceDate;
import project.common.StockETF;
import project.data.StockETFReader;
import project.processor.PortfolioAnalyzer;
import project.processor.TradingPatterns;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // ensure dot decimal output

        // 1. Initialize portfolio and data
        Portfolio portfolio = TradingPatterns.initializeDummyPortfolio();
        DateCounter dates = new DateCounter();
        List<MyDate> dateList = dates.getDateList();

        if (dateList.isEmpty()) {
            System.out.println("No dates available. Please check DateCounter / data loading.");
            return;
        }

        MyDate firstDate = dateList.get(0);
        MyDate lastDate = dateList.get(dateList.size() - 1);

        PortfolioAnalyzer analyzer = new PortfolioAnalyzer();

        // 2. Run user trading pattern ONCE (Operation 4 logic)
        try {
            analyzer.runUserTradingPattern(portfolio);
        } catch (Exception e) {
            System.out.println("Error running trading pattern: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 3. Load S&P 500 benchmark (VOO proxy)
        SAndPIndex sp500;
        try {
            sp500 = loadSP500Index();
        } catch (Exception e) {
            System.out.println("Error loading S&P500 benchmark: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 4. Simple console UI loop
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readInt(scanner);

                switch (choice) {
                    case 1 -> handleOperation1NotImplemented();
                    case 2 -> handleOperation2(scanner, analyzer, portfolio, lastDate);
                    case 3 -> handleOperation3(analyzer, portfolio, sp500, firstDate, lastDate);
                    case 4 -> handleOperation4(portfolio);
                    case 5 -> handleOperation5NotImplemented();
                    case 0 -> {
                        System.out.println("Exiting. Goodbye.");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter 0–5.");
                }
            }
        }
    }

    // ========================= UI helpers =========================

    private static void printMenu() {
        System.out.println();
        System.out.println("========== Stock Portfolio Simulator ==========");
        System.out.println("1. Operation 1: Total portfolio value on selected date (not implemented)");
        System.out.println("2. Operation 2: Profit/Loss % for a chosen stock/ETF");
        System.out.println("3. Operation 3: Compare portfolio performance to S&P 500");
        System.out.println("4. Operation 4: Show total strategy return");
        System.out.println("5. Operation 5: Sort stocks/ETFs by performance (not implemented)");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int readInt(Scanner scanner) {
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Op1 stub (your teammate will implement later)
    private static void handleOperation1NotImplemented() {
        System.out.println("Operation 1 is not implemented yet. (Teammate's task)");
    }

    // Op5 stub (your teammate will implement later)
    private static void handleOperation5NotImplemented() {
        System.out.println("Operation 5 is not implemented yet. (Teammate's task)");
    }

    // ========================= Operation 2 UI =========================

    private static void handleOperation2(Scanner scanner,
                                         PortfolioAnalyzer analyzer,
                                         Portfolio portfolio,
                                         MyDate date) {
        System.out.print("Enter ticker symbol (e.g. AAPL, VOO): ");
        String ticker = scanner.nextLine().trim().toUpperCase();

        Map<String, StockETF> userMap = portfolio.getUserStockETFMap();
        if (!userMap.containsKey(ticker)) {
            System.out.println("Unknown ticker: " + ticker);
            System.out.println("Available tickers: " + userMap.keySet());
            return;
        }

        try {
            analyzer.calculatePerStockStats(portfolio, ticker, date);

            if (portfolio.getPerStockETFStats() == null ||
                    !portfolio.getPerStockETFStats().containsKey(ticker)) {
                System.out.println("No stats available for ticker: " + ticker);
                return;
            }

            var stats = portfolio.getPerStockETFStats().get(ticker);
            double currentValue = stats.get(0);
            double profitLoss = stats.get(1);
            double profitLossPercent = stats.get(2);

            System.out.println("\n=== Operation 2 Result ===");
            System.out.println("Date           : " + date);
            System.out.println("Ticker         : " + ticker);
            System.out.println("Current value  : $" + String.format("%.2f", currentValue));
            System.out.println("Profit / Loss  : $" + String.format("%.2f", profitLoss));
            System.out.println("P/L Percentage : " + String.format("%.2f", profitLossPercent) + " %");

        } catch (Exception e) {
            System.out.println("Error during Operation 2: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================= Operation 3 UI =========================

    private static void handleOperation3(PortfolioAnalyzer analyzer,
                                         Portfolio portfolio,
                                         SAndPIndex sp500,
                                         MyDate startDate,
                                         MyDate endDate) {
        try {
            analyzer.compareWithSP500(portfolio, sp500, startDate, endDate);

            System.out.println("\n=== Operation 3 Result ===");
            System.out.println("Start date              : " + startDate);
            System.out.println("End date                : " + endDate);
            System.out.println("Portfolio total P/L     : $" +
                    String.format("%.2f", portfolio.getTotalProfitLoss()));
            System.out.println("Portfolio total P/L %   : " +
                    String.format("%.2f", portfolio.getTotalProfitLossPercent()) + " %");
            System.out.println("S&P 500 return %        : " +
                    String.format("%.2f", portfolio.getSPReturn()) + " %");
            System.out.println("Over/Under vs S&P 500   : " +
                    String.format("%.2f", portfolio.getSPReturnOverUnder()) + " %");
        } catch (Exception e) {
            System.out.println("Error during Operation 3: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================= Operation 4 UI =========================

    private static void handleOperation4(Portfolio portfolio) {
        System.out.println("\n=== Operation 4 Result ===");
        System.out.println("Strategy return % (total) : " +
                String.format("%.2f", portfolio.getStrategyReturn()) + " %");
    }

    // ========================= S&P 500 loader =========================

    /**
     * Temporary implementation of S&P 500 benchmark:
     * we use VOO as a proxy for the S&P 500.
     *
     * Later, if you have a real S&P 500 reader, you can replace this method.
     */
    private static SAndPIndex loadSP500Index() {
        StockETFReader reader = StockETFReader.getInstance();
        var stockMap = reader.readStockETFs();

        StockETF voo = stockMap.get("VOO");
        if (voo == null) {
            throw new IllegalStateException("VOO data not found. Cannot build S&P 500 proxy.");
        }

        SAndPIndex sp500 = new SAndPIndex("S&P500 (VOO proxy)");

        // Copy all price data from VOO into the SAndPIndex structure
        for (var entry : voo.getPriceMap().entrySet()) {
            MyDate date = entry.getKey();
            PriceDate priceDate = entry.getValue();

            sp500.addPriceDate(priceDate);
            sp500.addPriceMap(date, priceDate);
        }

        return sp500;
    }
}
