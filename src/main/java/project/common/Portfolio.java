package project.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Portfolio {
    private static double Budget = 10000.00;
    private final double InitialInvestment;
    private final Map<String, Integer> Allocations;
    private final int StopLossPercentage;
    private final int RiskPercentage;
    private final int TargetPercentage;
    private final Map<String, Integer> ThresholdPrice;
    private final Map<String, StockETF> UserStockETFMap;
    private Map<String, Double> shares;

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

    public Portfolio(double initialInvestment, Map<String, Integer> Allocations, int StopLoss, int Risk, int Target, Map<String, Integer> Threshold, Map<String, StockETF> UserStockETFMap, Map <String, Double> shares) {
        this.InitialInvestment = initialInvestment;
        this.Allocations = Allocations;
        this.StopLossPercentage = StopLoss;
        this.RiskPercentage = Risk;
        this.TargetPercentage = Target;
        this.ThresholdPrice = Threshold;
        this.UserStockETFMap = UserStockETFMap;
        this.shares = shares;
    }

    public static double getBudget() {
        return Budget;
    }

    public static void addBudget(double money) {
        Budget = Budget + money;
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

    public Map<String, Integer> getThreshold() {
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
