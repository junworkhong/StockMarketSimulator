package project.processor;

import project.common.DateResultMap;
import project.common.MyDate;

import java.util.HashMap;
import java.util.Map;

public class TotalValueOnDate {
    private static Map<MyDate, String> results = new HashMap<>();

    public static String calculateTotalValueOnDate(MyDate date, Map<MyDate, DateResultMap.DateResults> map){
        if (results.containsKey(date)) {
            return results.get(date);
        }
        else {
            if (map.containsKey(date)) {
                String result = "Total Value On Date: " + date + map.get(date);
                results.put(date, result);
                return result;
            }else {
                String result = "Invalid date!";
                return result;
            }
        }
//        if (map.containsKey(date)){
//            System.out.println("Total Value On Date: " + date + map.get(date));
//        }else
//            System.out.println("Invalid date!");
    }
}
