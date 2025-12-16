package project.common;

import project.processor.TradingPattern;
import project.processor.TradingStrategy;

import java.util.*;

public class Portfolio {
    private TradingStrategy strategy;

    private double Budget = 10000.00;
    private final double InitialInvestment;
    private final Map<String, Integer> Allocations;
    private final int StopLossPercentage;
    private final int RiskPercentage;
    private final int TargetPercentage;
    private final Map<String, Double> ThresholdPrice;
    private final Map<String, StockETF> UserStockETFMap;
    private Map<String, Double> shares;
    private Map<String, Double> initialShares = new HashMap<>();
    private Map<String, Double> buyPrice = new HashMap<>();
    private Map<String, Double> dayOnePrice = new HashMap<>();
    private Map<String, Double> endPrice = new HashMap<>();
    private DateResultMap dateResultMap;
    private Set<String> stockList = new TreeSet<>();
    private Map<String, Double> totalBuyDollars = new HashMap<>();
    private Map<String, Double> totalSellDollars = new HashMap<>();

//    Operation 1 variables
    private double totalProfitLoss;
    private double totalProfitLossPercent;

//    Operation 2 variables
    private Map<String, List<Double>> perStockETFStats;

//    Operation 3 variables
    private double SPReturn;
    private double SPReturnOverUnder;

//    Operation 4 variables
    private double thresholdPercentage;
    private double remainingInitial;

    private double finalSharesValue;
    private double finalTotal;
    private double totalReturnPercentage;
    private double totalProfit;

    public Portfolio(double initialInvestment,
                     Map<String, Integer> Allocations,
                     int StopLoss,
                     int Risk,
                     int Target,
                     Map<String, Double> Threshold,
                     Map<String, StockETF> UserStockETFMap,
                     Map <String, Double> shares,
                     DateResultMap dateMap) {
        this.InitialInvestment = initialInvestment;
        this.Allocations = Allocations;
        this.StopLossPercentage = StopLoss;
        this.RiskPercentage = Risk;
        this.TargetPercentage = Target;
        this.ThresholdPrice = Threshold;
        this.UserStockETFMap = UserStockETFMap;
        this.shares = shares;
        this.dateResultMap = dateMap;

        if (this.UserStockETFMap != null) {
            for (Map.Entry<String, StockETF> entry : UserStockETFMap.entrySet())
                if (entry == null || entry.getKey() == null || entry.getValue() == null)
                    continue;
                else
                    stockList.add(entry.getKey());
        }else
            stockList = null;
    }

    public Portfolio copy() {
        return new Portfolio(this.InitialInvestment,
                this.Allocations,
                this.StopLossPercentage,
                this.RiskPercentage,
                this.TargetPercentage,
                this.ThresholdPrice,
                this.UserStockETFMap,
                this.shares,
                this.dateResultMap);
    }

    public void setStrategy(TradingStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute(Portfolio portfolio) {
        if (this.strategy == null)
            this.strategy = new TradingPattern();

        strategy.RunTradingPattern(portfolio);
    }

//    Getters and setters for calculations

    public double getBudget() {
        return this.Budget;
    }

    public void addBudget(double money) {
        this.Budget = this.Budget + money;
    }

    public double getInitialInvestment() {
        return this.InitialInvestment;
    }

    public Map<String, Integer> getAllocations() {
        return this.Allocations;
    }

    public int getStopLoss() {
        return this.StopLossPercentage;
    }

    public int getRisk() {
        return this.RiskPercentage;
    }

    public int getTarget() {
        return this.TargetPercentage;
    }

    public Map<String, Double> getThreshold() {
        return this.ThresholdPrice;
    }

    public Map<String, StockETF> getUserStockETFMap() {
        return this.UserStockETFMap;
    }

    public Map<String, Double> getShares() {
        return this.shares;
    }

    public void addShares(String ticker, Double shares) {
        this.shares.put(ticker, this.shares.getOrDefault((ticker) + shares, 0.0));
    }

    public void initializeShares(String ticker, Double shares) {
        this.initialShares.put(ticker, shares);
    }

    public void sellShares(String ticker) {
        this.shares.put(ticker, 0.0);
    }

    public void resetShares(String... tickers) {
        for (String t : tickers) {
            this.shares.put(t, 0.0);
        }
    }

    public Map<String, Double> getBuyPrice() {
        return buyPrice;
    }

    public void addBuyPrice(String ticker, double newPrice) {
        buyPrice.put(ticker, newPrice);
    }

    public Map<String, Double> getEndPrice() {
        return endPrice;
    }

    public void addEndPrice(String ticker, double endPrice) {
        this.endPrice.put(ticker, endPrice);
    }

    public Map<String, Double> getDayOnePrice() {
        return this.dayOnePrice;
    }

    public void addDayOnePrice(String ticker, double dayOnePrice) {
        this.dayOnePrice.put(ticker, dayOnePrice);
    }

    public DateResultMap getDateResultMap() {
        return this.dateResultMap;
    }

    public Set<String> getStockList() {
        return this.stockList;
    }

    public Map<String, Double> getInitialShares() {
        return this.initialShares;
    }

    public Map<String, Double> getTotalBuyDollars() {
        return totalBuyDollars;
    }

    public void addTotalBuyDollars(String ticker, double value) {
        this.totalBuyDollars.put(ticker, value);
    }

    public Map<String, Double> getTotalSellDollars() {
        return totalSellDollars;
    }

    public void addTotalSellDollars(String ticker, double value) {
        this.totalSellDollars.put(ticker, value);
    }

    //    Getters and setters for results

    public double getRemainingInitial() {
        return remainingInitial;
    }

    public void setRemainingInitial(double remainingBudget) {
        this.remainingInitial = remainingBudget;
    }

    public double getThresholdPercentage() {
        return thresholdPercentage;
    }

    public void setThresholdPercentage(double thresholdPercentage) {
        this.thresholdPercentage = thresholdPercentage;
    }

    public double getFinalSharesValue() {
        return finalSharesValue;
    }

    public void setFinalSharesValue(double finalSharesValue) {
        this.finalSharesValue = finalSharesValue;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }

    public double getTotalReturnPercentage() {
        return totalReturnPercentage;
    }

    public void setTotalReturnPercentage(double totalReturnPercentage) {
        this.totalReturnPercentage = totalReturnPercentage;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }


    public double getSPReturnOverUnder() {
        return SPReturnOverUnder;
    }

    public void setSPReturnOverUnder(double SPReturnOverUnder) {
        this.SPReturnOverUnder = SPReturnOverUnder;
    }

    public double getSPReturn() {
        return SPReturn;
    }

    public void setSPReturn(double SPReturn) {
        this.SPReturn = SPReturn;
    }

    public Map<String, List<Double>> getPerStockETFStats() {
        return perStockETFStats;
    }

    public void setPerStockETFStats(Map<String, List<Double>> perStockETFStats) {
        this.perStockETFStats = perStockETFStats;
    }

    public double getTotalProfitLossPercent() {
        return totalProfitLossPercent;
    }

    public void setTotalProfitLossPercent(double totalProfitLossPercent) {
        this.totalProfitLossPercent = totalProfitLossPercent;
    }

    public double getTotalProfitLoss() {
        return totalProfitLoss;
    }

    public void setTotalProfitLoss(double totalProfitLoss) {
        this.totalProfitLoss = totalProfitLoss;
    }

}
