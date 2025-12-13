package project.ui;

import com.bethecoder.ascii_table.ASCIITable;
import project.common.*;
import project.data.StockETFReader;
import project.processor.*;

import java.util.*;

public class SimulatorUI {
    public boolean checkIfInteger (String... input) {
        for (String s : input) {
            if (s.trim().matches("-?\\d+"))
                return true;
        }
        return false;
    }

    public boolean checkIfPositiveInteger (String... input) {
        if (checkIfInteger(input)) {
            for (String s : input) {
               if (Integer.parseInt(s) >= 0)
                   return true;
            }
        }
        return false;
    }

    public void start() {
        System.out.println("Welcome to the Stock Market Simulator!");
        Scanner sc = new Scanner(System.in);
        System.out.println("Here are the list of stocks and ETFs to choose from:");

        StockETFReader reader = StockETFReader.getInstance();
        Map<String, StockETF> stockETFs = reader.readStockETFs();

        if (reader == null || stockETFs == null)
            throw new IllegalStateException("Stock/ETFs are null!");

        List<String[]> myList = new ArrayList<>();
        List<String> helperList = new ArrayList<>();;
        for (Map.Entry<String, StockETF> entry : stockETFs.entrySet()) {
            if (helperList.size() < 5) {
                helperList.add(entry.getKey());
            }
            if (helperList.size() == 5) {
                String[] array = new String[5];
                int i = 0;
                for (String item : helperList) {
                    array[i] = item;
                    i++;
                }
                myList.add(array);
                helperList.clear();
            }
        }

        String [] header = {};
        String[][] data = new String[myList.size()][4];
        for (int i = 0; i < myList.size(); i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = myList.get(i)[j];
            }
        }

        ASCIITable.getInstance().printTable(header, data);

        System.out.println("Your starting budget is $10000.00. How much of it would you like to invest? (Enter a number between 1-10000): ");

        boolean on = true;
        double initialInvestment = 0.0;

        while (on) {
            try {
                String input = sc.nextLine().trim();
                if (input.equals(" ") || input.isEmpty() || !input.trim().matches("\\d*\\.?\\d+"))
                    throw new IllegalArgumentException("Please enter a valid amount\n");

                initialInvestment = Double.parseDouble(input.trim());
                if (initialInvestment <= 0 || initialInvestment > 10000)
                    throw new IllegalArgumentException("Please enter a valid amount\n");

                System.out.println("List the percentages of your initial $" + initialInvestment
                        + "0 that you'd like to allocate for each stock. You can enter 0, but please use whole numbers!\n");
                on = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
            int percentage = 100;

            List<Integer> count =  new ArrayList<>();
            MyDate date = new MyDate("2020-01-02");

            Map<String, Integer> allocations = new HashMap<>();
            for (Map.Entry<String, StockETF> entry : stockETFs.entrySet()) {
                on = true;
                while (on) {
                    if (entry == null)
                        continue;

                    if (percentage == 0)
                        break;
                    try {
                        System.out.println(entry.getKey() + " Stock Price on 2020-01-02: $"
                                + entry.getValue().getPriceMap().get(date).getClose() +
                                "\nHow much would you like to allocate to this stock/ETF? ");
                        String input = sc.nextLine().trim();
                        int amount = Integer.parseInt(input);

                        if (amount > 100 || amount < 0) {
                            throw new IllegalArgumentException("Amount must be between 0 and 100\n");
                        }

                        if (amount > percentage)
                            throw new IllegalArgumentException("That is greater than the remaining amount! Please try again\n");

                        if (amount == 0);
                        else
                            allocations.put(entry.getKey(), amount);

                        count.add(amount);
                        percentage = percentage - amount;
                        System.out.println("You have " + percentage + "% of your initial investment remaining.\n");
                        on = false;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }

            int sum = count.stream().reduce(0, (a, b) -> a + b);

            if (sum != 100) {
                System.out.println("You only used " + percentage + "% of your allocation. Exiting program.");
                System.exit(0);
            }

            System.out.println("All allocations complete. Now you will create your own trading pattern!\n");
            int stopLoss = 0;
            int risk = 0;
            int target = 0;
            int thresholdPercent = 0;

            on = true;
            while (on) {
                try {
                    System.out.println("Please enter your stop loss percentage as an integer " +
                            "(if current buy price falls below this, sell all shares): ");
                    String input = sc.nextLine().trim();
                    if (!checkIfPositiveInteger(input))
                        throw new IllegalArgumentException("That is not a positive integer\n");

                    stopLoss = Integer.parseInt(input);
                    if (stopLoss > 100)
                        throw new IllegalArgumentException("Please enter a valid amount\n");

                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            double remaining = 10000.0 - initialInvestment;
            on = true;

            while (on) {
                try {
                    System.out.println("Please enter your risk percentage as an integer " +
                            "(percentage of your remaining $" + remaining + " you'd like to use to buy new shares): ");
                    String input = sc.nextLine().trim();
                    if (!checkIfPositiveInteger(input))
                        throw new IllegalArgumentException("That is not a positive integer\n");

                    risk = Integer.parseInt(input);
                    if (risk > 100)
                        throw new IllegalArgumentException("Please enter a valid amount\n");

                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            on = true;
            while (on) {
                try {
                    System.out.println("Please enter your target percentage as an integer " +
                            "(if current buy price reaches this percent of your original buy price, sell all shares): ");
                    String input = sc.nextLine().trim();
                    if (!checkIfPositiveInteger(input))
                        throw new IllegalArgumentException("That is not a positive integer\n");

                    target = Integer.parseInt(input);
                    if (target > 100)
                        throw new IllegalArgumentException("Please enter a valid amount\n");
                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            on = true;
            while (on) {
                try {
                    System.out.println("Please enter your buy threshold percentage as an integer " +
                            "(if current buy price passes this percent, then you buy shares) : ");
                    String input = sc.nextLine().trim();
                    if (!checkIfInteger(input))
                        throw new IllegalArgumentException("That is not an integer\n");

                    thresholdPercent = Integer.parseInt(input);
                    if (thresholdPercent > 100)
                        throw new IllegalArgumentException("Please enter a valid amount\n");
                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            Map<String, Double> threshold = new HashMap<>();
            Map<String, StockETF> UserStockETFMap = new HashMap<>();
            Map<String, Double> shares = new HashMap<>();
            DateResultMap dateResultMap = new DateResultMap();

            if (allocations == null)
                throw new IllegalStateException("Allocations are null");

            for (Map.Entry<String, Integer> entry : allocations.entrySet()) {
                if (entry == null)
                    continue;
                threshold.put(entry.getKey(), (double) thresholdPercent);
                UserStockETFMap.put(entry.getKey(), stockETFs.get(entry.getKey()));
                shares.put(entry.getKey(), 0.0);
            }

            Portfolio userPortfolio = new Portfolio(
                    initialInvestment,
                    allocations,
                    stopLoss,
                    risk,
                    target,
                    threshold,
                    UserStockETFMap,
                    shares,
                    dateResultMap
            );

            if (userPortfolio == null
                    || userPortfolio.getAllocations() == null
                    || userPortfolio.getThreshold() == null
                    || userPortfolio.getUserStockETFMap() == null
                    || userPortfolio.getShares() == null
                    || userPortfolio.getDateResultMap() == null)
                throw new IllegalStateException("UserPortfolio is null or contains null element");

            TradingPattern userTrade = new TradingPattern();

            if (userTrade == null)
                throw new IllegalStateException("TradingPattern is null");

            userPortfolio.setStrategy(userTrade);
            userPortfolio.execute(userPortfolio);

            userPortfolio.setThresholdPercentage(thresholdPercent);
            userPortfolio.setRemainingInitial(remaining);

            viewInitialPortfolio(userPortfolio);

            String[] menuHeader = {"Menu"};
            String[][] menuData = {
                    {"Operation 0: Exit program"},
                    {"Operation 1: Calculate total value of portfolio on selected date"},
                    {"Operation 2: Show Profit/Loss Percentage for a chosen stock/ETF"},
                    {"Operation 3: Compare portfolio performance to S&P 500 performance"},
                    {"Operation 4: Compare your trading pattern and results to other trading patterns"},
                    {"Operation 5: Sort all stocks and ETFs by best and worst performing"},
                    {"Operation 6: View portfolio"}
            };

            on = true;

            while (on) {
                ASCIITable.getInstance().printTable(menuHeader, menuData, ASCIITable.ALIGN_CENTER);
                System.out.println("What would you like to view? Please enter a number from 0 to 6: ");

                int option;
                String input = "";

                boolean on2 = true;
                while (on2) {
                    input = sc.nextLine().trim();
                    if (!checkIfPositiveInteger(input)) {
                        System.out.println("Please try again!\n");
                    }else if (checkIfPositiveInteger(input) && (Integer.parseInt(input) < 0 || Integer.parseInt(input) > 6)) {
                        System.out.println("Please enter a number from 0 to 6!\n");
                    }else
                        break;
                }

                option = Integer.parseInt(input);
                switch (option) {
                    case 0:
                        System.out.println("Goodbye!");
                        on = false;
                        break;
                    case 1:
                        operationOne(userPortfolio.getDateResultMap().getDateResults());
                        break;
                    case 2:
                        operationTwo(userPortfolio);
                        break;
                    case 3:
                        operationThree(userPortfolio);
                        break;
                    case 4:
                        operationFour(userPortfolio);
                        break;
                    case 5:
                        operationFive(userPortfolio);
                        break;
                    case 6:
                        viewInitialPortfolio(userPortfolio);
                        break;
                }
            }
        }

    public void viewInitialPortfolio (Portfolio userPortfolio) {
        System.out.println("\nThis is your initial portfolio: ");
        System.out.println("Initial shares: \n" + userPortfolio.getInitialShares().toString());

        System.out.println("Initial Budget: $10000.00");
        System.out.println("Initial Investment: $" + userPortfolio.getInitialInvestment());
        System.out.println("Initial Remaining Budget: $" + userPortfolio.getRemainingInitial());
        System.out.println("Allocations %: " + userPortfolio.getAllocations().toString());
        System.out.println("Stop Loss Percentage: " + userPortfolio.getStopLoss() + "%");
        System.out.println("Risk Percentage: " + userPortfolio.getRisk() + "%");
        System.out.println("Target Percentage: " + userPortfolio.getTarget() + "%");
        System.out.println("Buy Threshold Percentage: " + userPortfolio.getThresholdPercentage() + "%");

        System.out.println("\n==== YOUR TRADING PATTERN RESULTS ====");
        System.out.println("Final Shares: \n" + userPortfolio.getShares().toString());
        System.out.println("Final Budget: $" + userPortfolio.getBudget());
        System.out.println("Final Shares Value: $" + userPortfolio.getFinalSharesValue());
        System.out.println("Final Total Portfolio Value: $" + userPortfolio.getFinalTotal());
        System.out.println("Total Return Percentage: " + userPortfolio.getTotalReturnPercentage() + "%");
        System.out.println("Total Profit/Loss: $" + userPortfolio.getTotalProfit());

        returnToMenu();
    }

    public void operationOne(Map<MyDate, DateResultMap.DateResults> map) {
        boolean on = true;
        TotalValueOnDate total =  new TotalValueOnDate();
        while (on) {
            System.out.println("\nPlease enter a date in the format yyyy-mm-dd starting from 2020-01-02 to 2025-11-14 " +
                    "or enter 0 to return to menu");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine().trim();

            if (checkIfInteger(input) && Integer.parseInt(input) == 0) {
                on = false;
                break;
            }
            MyDate myDate = new MyDate(input);

            if (!myDate.isValidDate(myDate)) {
                System.out.println("Invalid date. Try again\n");
            }
            else{
                System.out.println(total.calculateTotalValueOnDate(myDate, map));
            }
        }
    }

    public void operationTwo(Portfolio portfolio) {
        boolean on = true;
        CalculatePerStockStats calc = new CalculatePerStockStats();
        System.out.println("\nHere are the stocks/ETFs in your portfolio: " + portfolio.getStockList().toString());
        while (on) {
            System.out.println("Please enter a stock name or press 0 to return to menu: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine().trim().toLowerCase();

            if (checkIfInteger(input) && Integer.parseInt(input) == 0) {
                on = false;
                break;
            }

            boolean on2 = true;
            while (on2) {
                if (!portfolio.getStockList().contains(input.trim().toUpperCase())) {
                    System.out.println("Enter a stock/ETF you own.");
                    input = sc.nextLine().trim().toLowerCase();
                }else
                    break;
            }

            System.out.println("Please enter a date in the format yyyy-mm-dd starting from 2020-01-02 to 2025-11-14 " +
                    "or enter 0 to return to menu");
            String input2 = sc.nextLine().trim();
            MyDate date = new MyDate(input2);

            if (checkIfInteger(input2) && Integer.parseInt(input2) == 0) {
                on = false;
                break;
            }

            on2 = true;
            while (on2) {
                if (!date.isValidDate(date)) {
                    System.out.println("Please enter a valid date");
                    input2 = sc.nextLine().trim();
                }else {
                    boolean on3 = true;
                    while (on3) {
                        try{
                            calc.execute(portfolio, input, date);
                            for (Map.Entry<String, List<Double>> entry : portfolio.getPerStockETFStats().entrySet()) {
                                System.out.println(entry.getKey() + ":\nValue : $" + entry.getValue().get(0)
                                        + "\nProfit/Loss: $" + entry.getValue().get(1)
                                        + "\nProfit/Loss Percentage: " + entry.getValue().get(2) + "%");
                            }
                            break;
                        }catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    }
                    break;
                }
            }
            }
    }

    public void operationThree(Portfolio portfolio) {
        CompareWithSP500 sp500 = new CompareWithSP500();
        MyDate startDate = new MyDate("2020-01-02");
        MyDate endDate = new MyDate("2025-11-13");

        try {
            sp500.execute(portfolio, startDate, endDate);
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("S&P Return Percent over the same time period: " + portfolio.getSPReturn() + "%");
        System.out.println("Your portfolio return percent over the same time period: " + portfolio.getTotalReturnPercentage() + "%");
        System.out.println("Your portfolio return percent compared to the S&P 500: " + portfolio.getSPReturnOverUnder() + "%");

        returnToMenu();
    }

    public void operationFour(Portfolio portfolio) {
        System.out.println("\nThis is your initial portfolio: ");
        System.out.println("Initial Shares: \n" + portfolio.getInitialShares().toString());

        System.out.println("Initial Budget: $10000.00");
        System.out.println("Initial Investment: $" + portfolio.getInitialInvestment());
        System.out.println("Initial Remaining Budget: $" + portfolio.getRemainingInitial());
        System.out.println("Allocations %: " + portfolio.getAllocations().toString());
        System.out.println("Stop Loss Percentage: " + portfolio.getStopLoss() + "%");
        System.out.println("Risk Percentage: " + portfolio.getRisk() + "%");
        System.out.println("Target Percentage: " + portfolio.getTarget() + "%");
        System.out.println("Buy Threshold Percentage: " + portfolio.getThresholdPercentage() + "%");

        System.out.println("\n==== YOUR TRADING PATTERN RESULTS ====");
        System.out.println("\nFinal Shares: " + portfolio.getShares().toString());
        System.out.println("Final Budget: $" + portfolio.getBudget());
        System.out.println("Final Shares Value: $" + portfolio.getFinalSharesValue());
        System.out.println("Final Total Portfolio Value: $" + portfolio.getFinalTotal());
        System.out.println("Total Return Percentage: " + portfolio.getTotalReturnPercentage() + "%");
        System.out.println("Total Profit/Loss: $" + portfolio.getTotalProfit());

        BreakoutTradingPattern breakout = new BreakoutTradingPattern();
        portfolio.setStrategy(breakout);
        portfolio.execute(portfolio);

        MomentumTradingPattern momentum = new MomentumTradingPattern();
        portfolio.setStrategy(momentum);
        portfolio.execute(portfolio);

        returnToMenu();
    }


    public void operationFive(Portfolio portfolio) {
        SortBestWorstPerformers sort = new SortBestWorstPerformers();
        sort.sortBestToWorst(portfolio);
        returnToMenu();
    }

    public void returnToMenu() {
        boolean on = true;
        while (on) {
            System.out.println("\nEnter 0 to return: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine().trim();
            if (checkIfInteger(input) && Integer.parseInt(input) == 0) {
                on = false;
            }
        }
    }


    public static void main(String[] args) {
        SimulatorUI sim = new SimulatorUI();
//        String [] header = {};
//        String[][] data = {
//                { "Ram", "2000", "Manager", "#99, Silk board", "1111"  },
//                { "Sri", "12000", "Developer", "BTM Layout", "22222" },
//                { "Prasad", "42000", "Lead", "#66, Viaya Bank Layout", "333333" },
//                { "Anu", "132000", "QA", "#22, Vizag", "4444444" },
//                { "Sai", "62000", "Developer", "#3-3, Kakinada"  },
//                { "Venkat", "2000", "Manager"   },
//                { "Raj", "62000"},
//                { "BTC"},
//        };
//        ASCIITable.getInstance().printTable(header, data);
        sim.start();
    }
}
