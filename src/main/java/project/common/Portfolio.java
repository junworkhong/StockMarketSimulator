package project.common;

import java.util.*;

public class Portfolio {
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
    private Map<String, Double> endPrice = new HashMap<>();
    private DateResultMap dateResultMap;
    private final Set<String> stockList = new TreeSet<>();

//    Operation 1 variables
    private double portfolioValue;
    private double totalProfitLoss;
    private double totalProfitLossPercent;

//    Operation 2 variables
    private Map<String, List<Double>> perStockETFStats;

//    Operation 3 variables
    private double SPReturn;
    private double SPReturnOverUnder;

//    Operation 4 variables
    private int totalTrades;
    private int winningTrades;
    private int losingTrades;
    private double winRate;
    private String biggestWinner;
    private String biggestLoser;
    private double strategyReturn;

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

        for (Map.Entry<String, StockETF> entry : UserStockETFMap.entrySet()) {
            stockList.add(entry.getKey());
        }
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
        this.shares.put(ticker, this.shares.get(ticker) + shares);
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

    public DateResultMap getDateResultMap() {
        return this.dateResultMap;
    }

    public Set<String> getStockList() {
        return this.stockList;
    }

    public Map<String, Double> getInitialShares() {
        return this.initialShares;
    }

//    Getters and setters for results

    public double getStrategyReturn() {
        return strategyReturn;
    }

    public void setStrategyReturn(double strategyReturn) {
        this.strategyReturn = strategyReturn;
    }

    public String getBiggestLoser() {
        return biggestLoser;
    }

    public void setBiggestLoser(String biggestLoser) {
        this.biggestLoser = biggestLoser;
    }

    public String getBiggestWinner() {
        return biggestWinner;
    }

    public void setBiggestWinner(String biggestWinner) {
        this.biggestWinner = biggestWinner;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public int getLosingTrades() {
        return losingTrades;
    }

    public void setLosingTrades(int losingTrades) {
        this.losingTrades = losingTrades;
    }

    public int getWinningTrades() {
        return winningTrades;
    }

    public void setWinningTrades(int winningTrades) {
        this.winningTrades = winningTrades;
    }

    public int getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(int totalTrades) {
        this.totalTrades = totalTrades;
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

    public double getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        this.portfolioValue = portfolioValue;
    }
}
