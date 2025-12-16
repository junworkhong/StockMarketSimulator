package project.common;

import java.util.Map;
import java.util.TreeMap;

public class DateResultMap {
    public static Map<MyDate, DateResults> map = new TreeMap<>();

    public class DateResults {
        private double initialInvestment;
        private double currentCash;
        private double currentSharesValue;
        private double currentTotalValue;
        private double currentReturnPercent;
        private Map<String, Double> shareMap;

        public DateResults(double initInvestment,
                              double currCash,
                              double currSharesValue,
                              double currTotalValue,
                              double currReturnPercent,
                              Map<String, Double> shareMap) {
            this.initialInvestment = initInvestment;
            this.currentCash = currCash;
            this.currentSharesValue = currSharesValue;
            this.currentTotalValue = currTotalValue;
            this.currentReturnPercent = currReturnPercent;
            this.shareMap = shareMap;
        }

        @Override
        public String toString() {
            return "\nInitial Investment: " + this.initialInvestment +
                    "\nCurrent Cash: " + this.currentCash +
                    "\nCurrent Shares Value: " + this.currentSharesValue +
                    "\nCurrent Total Value: " + this.currentTotalValue +
                    "\nCurrent Return Percent: " + this.currentReturnPercent +
                    "\nCurrent Shares: " + this.shareMap;
        }
    }

    public static void addDateResults(MyDate todayDate,
                                                    double initInvestment,
                                                    double currCash,
                                                    double currSharesValue,
                                                    double currTotalValue,
                                                    double currReturnPercent,
                                                    Map<String, Double> shareMap) {
        DateResults myResults = new DateResults(initInvestment,
                currCash,
                currSharesValue,
                currTotalValue,
                currReturnPercent,
                shareMap);
        map.put(todayDate, myResults);
    }

    public Map<MyDate, DateResults> getDateResults() {
        return map;
    }

    public void dateResultOutput() {
        for (Map.Entry<MyDate, DateResults> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
