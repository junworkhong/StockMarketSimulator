package project.processor;

import project.common.MyDate;
import project.common.Portfolio;
import project.common.StockETF;

import java.text.DecimalFormat;
import java.util.*;

import static java.util.Arrays.stream;

public class SortBestWorstPerformers {
    public void sortBestToWorst(Portfolio portfolio) {
        Map<String, Double> helperValueMap = new TreeMap<>();

        if (portfolio == null || portfolio.getStockList() == null)
            throw new IllegalStateException("Portfolio has a bug");

        for (String ticker : portfolio.getStockList()) {
            if (ticker == null)
                continue;

            double profit = (portfolio.getTotalSellDollars().get(ticker) - portfolio.getTotalBuyDollars().get(ticker));
            MyDate date = new MyDate("2025-11-14");

            StockETF tempStock = portfolio.getUserStockETFMap().get(ticker);

            if (tempStock == null)
                continue;

            double lastClose = tempStock.getPriceMap().get(date).getClose();
            double unrealized = portfolio.getShares().get(ticker) * lastClose;
            double total = profit + unrealized;

            double profitPercent = 0.0;
            if (portfolio.getTotalBuyDollars().get(ticker) > 0) {
                double initialAllocation = portfolio.getInitialShares().get(ticker) * portfolio.getDayOnePrice().get(ticker);
                profitPercent = (total / initialAllocation) * 100.0;
            }

            helperValueMap.put(ticker, profitPercent);
        }

        List<Map.Entry<String, Double>> entryList = new ArrayList<>(helperValueMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        entryList = entryList.reversed();

        Map<String, Double> stockList = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : entryList) {
            if (entry == null)
                continue;
            stockList.put(entry.getKey(), entry.getValue());
        }

        DecimalFormat numberFormat = new DecimalFormat("#.00");

        for (Map.Entry<String, Double> entry : stockList.entrySet()) {
            if (entry == null)
                continue;
            System.out.println(entry.getKey() + " : " + numberFormat.format(entry.getValue()) + "%");
        }
    }
}
