package project.ui;

import com.bethecoder.ascii_table.ASCIITable;
import project.common.DateResultMap;
import project.common.MyDate;
import project.common.Portfolio;
import project.common.StockETF;
import project.data.StockETFReader;
import project.processor.TradingPattern;
import project.processor.TradingStrategy;

import java.util.*;

public class SimulatorUI {
    TradingStrategy tradingStrategy;

    public static boolean checkIfInteger (String input) {
        return (input.trim().matches("-?\\d+"));
    }

    public static boolean checkIfPositiveInteger (String input) {
        return checkIfInteger(input) && (Integer.parseInt(input) >= 0);
    }

    public static void start() {
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

        System.out.println("Your starting budget is $10,000. How much of it would you like to invest? ");

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

                System.out.println("List the percentages of your initial $" + initialInvestment + " that you'd like to allocate for each stock. You can enter 0, but please use whole numbers!");
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

            TradingPattern.RunTradingPattern(userPortfolio);

            System.out.println("\nThis is your initial portfolio: ");
            System.out.println(userPortfolio.getInitialShares().toString());
            System.out.println("Initial Budget: $10000.00");
            System.out.println("Initial Investment: $" + initialInvestment);
            System.out.println("Current Budget: $" + remaining);
            System.out.println("Allocations: " + userPortfolio.getAllocations().toString());
            System.out.println("Stop Loss Percentage: " + stopLoss + "%");
            System.out.println("Risk Percentage: " + risk + "%");
            System.out.println("Target Percentage: " + target + "%");
            System.out.println("Buy Threshold Percentage: " + thresholdPercent + "%");

            System.out.println("What would you like to view?");
        }

    public static void main(String[] args) {
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
        start();
    }
}
