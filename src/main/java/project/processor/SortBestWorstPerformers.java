package project.processor;

import project.common.Portfolio;
import project.common.StockETF;

import java.text.DecimalFormat;
import java.util.*;

import static java.util.Arrays.stream;

public class SortBestWorstPerformers {
    public static void sortBestToWorst(Portfolio portfolio) {
//        Map<String, StockETF> tempStockETFMap = portfolio.getUserStockETFMap();
        Map<String, Double> helperValueMap = new TreeMap<>();

        for (String ticker : portfolio.getStockList()) {
            List<Double> values = new ArrayList<>();
            values.add(portfolio.getBuyPrice().get(ticker));
            values.add(portfolio.getEndPrice().get(ticker));
            values.add(portfolio.getShares().get(ticker));
            double profit = (values.get(1) - values.get(0)) * values.get(2);
            double profitPercent = ((values.get(1) / values.get(0)) - 1) * 100;
            values.add(profit);
            values.add(profitPercent);
            helperValueMap.put(ticker, values.get(4));
        }

        List<Map.Entry<String, Double>> entryList = new ArrayList<>(helperValueMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        entryList = entryList.reversed();

        Map<String, Double> stockList = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : entryList) {
            stockList.put(entry.getKey(), entry.getValue());
        }

        DecimalFormat numberFormat = new DecimalFormat("#.00");

        for (Map.Entry<String, Double> entry : stockList.entrySet()) {
            System.out.println(entry.getKey() + " : " + numberFormat.format(entry.getValue()) + "%");
        }
    }
}
