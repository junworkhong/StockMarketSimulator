package project.processor;

import project.common.DateResultMap;
import project.common.MyDate;

import java.util.HashMap;
import java.util.Map;

import project.ui.SimulatorUI;

public class TotalValueOnDate {
    private static Map<MyDate, String> results = new HashMap<>();

    public String calculateTotalValueOnDate(MyDate date, Map<MyDate, DateResultMap.DateResults> map){
        if (results == null || date == null || map == null)
            throw new IllegalStateException("Results or dates are invalid");

        try {
            if (!date.isValidDate(date))
                throw new IllegalArgumentException("Invalid date");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        if (results.containsKey(date)) {
            return results.get(date);
        }
        else {
            if (map.containsKey(date)) {
                String result = "Total Value On Date: " + date + map.get(date);
                results.put(date, result);
                return result;
            }else {
                String result = "No price data on that date. Try again";
                return result;
            }
        }
    }
}
