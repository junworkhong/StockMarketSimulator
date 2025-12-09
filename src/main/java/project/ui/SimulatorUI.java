package project.ui;

import com.bethecoder.ascii_table.ASCIITable;
import project.common.DateResultMap;
import project.common.MyDate;
import project.common.Portfolio;
import project.common.StockETF;
import project.data.StockETFReader;
import project.processor.*;

import java.util.*;

public class SimulatorUI {
    TradingStrategy tradingStrategy;

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
//        return checkIfInteger(input) && (Integer.parseInt(input) >= 0);
    }

    public void start() {
        System.out.println("Welcome to the Stock Market Simulator!");
        Scanner sc = new Scanner(System.in);
        System.out.println("Here are the list of stocks and ETFs to choose from:");

        StockETFReader reader = StockETFReader.getInstance();
        Map<String, StockETF> stockETFs = reader.readStockETFs();

        List<String[]> myList = new ArrayList<>();
        List<String> helperList = new ArrayList<>();;
        for (Map.Entry<String, StockETF> entry : stockETFs.entrySet()) {
//            System.out.println(entry.getKey());
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
                String input = sc.nextLine();
                if (input.equals(" ") || input.isEmpty() || !input.trim().matches("\\d*\\.?\\d+"))
                    throw new IllegalArgumentException("Please enter a valid amount");

                initialInvestment = Double.parseDouble(input.trim());
                if (initialInvestment <= 0 || initialInvestment > 10000)
                    throw new IllegalArgumentException("Please enter a valid amount");

                System.out.println("List the percentages of your initial $" + initialInvestment + "0 that you'd like to allocate for each stock. You can enter 0, but please use whole numbers!");
//                break;
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
                    if (percentage == 0)
                        break;
                    try {
                        System.out.println(entry.getKey() + " Stock Price on 2020-01-02: $" + entry.getValue().getPriceMap().get(date).getClose() +
                                "\nHow much would you like to allocate to this stock/ETF? ");
                        String input = sc.nextLine();
                        int amount = Integer.parseInt(input);

                        if (amount > 100 || amount < 0) {
                            throw new IllegalArgumentException("Amount must be between 0 and 100");
                        }

                        if (amount > percentage)
                            throw new IllegalArgumentException("That is greater than the remaining amount! Please try again");

                        allocations.put(entry.getKey(), amount);
                        count.add(amount);
                        percentage = percentage - amount;
                        System.out.println("You have " + percentage + "% of your initial investment remaining.\n");
                        on = false;
//                        break;
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

            System.out.println("All allocations complete. Now you will create your own trading pattern!");
            int stopLoss = 0;
            int risk = 0;
            int target = 0;
            int thresholdPercent = 0;

            on = true;
            while (on) {
                try {
                    System.out.println("Please enter your stop loss percentage as an integer (if current buy price falls below this, sell all shares): ");
                    String input = sc.nextLine();
                    if (!checkIfPositiveInteger(input))
                        throw new IllegalArgumentException("That is not a positive integer");

                    stopLoss = Integer.parseInt(input);
                    if (stopLoss > 100)
                        throw new IllegalArgumentException("Please enter a valid amount");

                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            double remaining = 10000.0 - initialInvestment;
            on = true;

            while (on) {
                try {
                    System.out.println("Please enter your risk percentage as an integer (percentage of your remaining $" + remaining + " you'd like to use to buy new shares): ");
                    String input = sc.nextLine();
                    if (!checkIfPositiveInteger(input))
                        throw new IllegalArgumentException("That is not a positive integer");

                    risk = Integer.parseInt(input);
                    if (risk > 100)
                        throw new IllegalArgumentException("Please enter a valid amount");

                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            on = true;
            while (on) {
                try {
                    System.out.println("Please enter your target percentage as an integer (if current buy price reaches this percent of your original buy price, sell all shares): ");
                    String input = sc.nextLine();
                    if (!checkIfPositiveInteger(input))
                        throw new IllegalArgumentException("That is not a positive integer");

                    target = Integer.parseInt(input);
                    if (target > 100)
                        throw new IllegalArgumentException("Please enter a valid amount");
                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            on = true;
            while (on) {
                try {
                    System.out.println("Please enter your buy threshold percentage as an integer (if current buy price passes this percent, then you buy shares) : ");
                    String input = sc.nextLine();
                    if (!checkIfInteger(input))
                        throw new IllegalArgumentException("That is not an integer");

                    thresholdPercent = Integer.parseInt(input);
                    if (thresholdPercent > 100)
                        throw new IllegalArgumentException("Please enter a valid amount");
                    on = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            Map<String, Double> threshold = new HashMap<>();
            Map<String, StockETF> UserStockETFMap = new HashMap<>();
            Map<String, Double> shares = new HashMap<>();
            DateResultMap dateResultMap = new DateResultMap();

            for (Map.Entry<String, Integer> entry : allocations.entrySet()) {
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

            TradingPattern userTrade = new TradingPattern();
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

            String input;

            while (on) {
                ASCIITable.getInstance().printTable(menuHeader, menuData, ASCIITable.ALIGN_CENTER);
                System.out.println("What would you like to view? Please enter a number from 0 to 6: ");

                input = sc.nextLine();
                boolean on2 = true;
                while (on2) {
                    try {
                        if (!checkIfPositiveInteger(input))
                            throw new IllegalArgumentException("Please try again");
                        int option = Integer.parseInt(input);
                        if (option < 0 || option > 6)
                            throw new IllegalArgumentException("Please enter a number from 0 to 6");
                        on2 = false;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

            /*
            Operation 1: Calculate total value of portfolio on selected date (Jun)
Operation 2: Showing profit/loss percentage for a chosen stock/ETF (Eric)
Operation 3: Comparing portfolio performance to S&P 500 Benchmark (Eric)
Operation 4: Different trading patterns and reporting total return (Eric + a little by Jun)
Operation 5: Sorting all stocks/ETFs by best and worst performing (Jun)

             */
                int option = Integer.parseInt(input);
                switch (option) {
                    case 0:
                        System.out.println("Goodbye!");
                        on = false;
                    case 1:
                        operationOne(userPortfolio.getDateResultMap().getDateResults());
                        break;
                    case 2:
                        operationTwo();
                        break;
                    case 3:
                        operationThree();
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
//        System.out.println("\nInitial Shares: ");
//        for (Map.Entry<String, Double> entry :  userPortfolio.getInitialShares().entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }

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
            System.out.println("\nPlease enter a date in the format yyyy-mm-dd starting from 2020-01-03 to 2025-11-14 or enter 0 to return to menu");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            if (checkIfInteger(input) && Integer.parseInt(input) == 0)
                on = false;
            else {
                MyDate myDate = new MyDate(input);
                System.out.println(total.calculateTotalValueOnDate(myDate, map));
            }
        }
    }

    public void operationTwo() {
        returnToMenu();
    }

    public void operationThree() {
        returnToMenu();
    }

    public void operationFour(Portfolio portfolio) {
        System.out.println("\nThis is your initial portfolio: ");
        System.out.println("Initial Shares: \n" + portfolio.getInitialShares().toString());
//        for (Map.Entry<String, Double> entry :  portfolio.getInitialShares().entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
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
//        System.out.println("\nFinal Shares: ");
//
//        for (Map.Entry<String, Double> entry : portfolio.getShares().entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }

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
            System.out.println("Enter 0 to return: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
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
