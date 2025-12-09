package project.processor;

import project.common.DateResultMap;
import project.common.MyDate;

import java.util.HashMap;
import java.util.Map;

import static project.ui.SimulatorUI.checkIfInteger;
import static project.ui.SimulatorUI.checkIfPositiveInteger;

public class TotalValueOnDate {
    private static Map<MyDate, String> results = new HashMap<>();

    public static String calculateTotalValueOnDate(MyDate date, Map<MyDate, DateResultMap.DateResults> map){
            try {
                if (date == null)
                    throw new IllegalArgumentException("Invalid date");

                if (!date.toString().contains("-"))
                    throw new IllegalArgumentException("Invalid date");

                String[] split = date.toString().split("-");

                if (split.length != 3)
                    throw new IllegalArgumentException("Invalid date");

                int year;
                int month;
                int day;

                if (checkIfPositiveInteger(split[0], split[1], split[2])) {
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[1]);
                    day = Integer.parseInt(split[2]);
                }else
                    throw new IllegalArgumentException("Invalid date");

                if (year < 2020 || year > 2025 || month < 1 || month > 12 || day < 1 || day > 31)
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
                String result = "Invalid date! Please try again";
                return result;
            }
        }
//        if (map.containsKey(date)){
//            System.out.println("Total Value On Date: " + date + map.get(date));
//        }else
//            System.out.println("Invalid date!");
    }
}
