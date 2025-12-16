package project;

import org.junit.Test;
import project.common.DateResultMap;
import project.common.MyDate;
import project.processor.TotalValueOnDate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TotalValueOnDateTest {
    TotalValueOnDate total = new TotalValueOnDate();

//    @Test
//    public void nullCalculateTest() {
//        MyDate date = null;
//        Map<MyDate, DateResultMap.DateResults> map = new HashMap<>();
//
//        assertThrows(IllegalStateException.class, () -> total.calculateTotalValueOnDate(date, map));
//
//        MyDate date1 = new MyDate("2020-01-02");
//        Map<MyDate, DateResultMap.DateResults> map2 = null;
//        assertThrows(IllegalStateException.class, () -> total.calculateTotalValueOnDate(date1, map2));
//    }
//
//    @Test
//    public void validDateTest() {
//        MyDate date = new MyDate("1999-20-30");
//        Map<MyDate, DateResultMap.DateResults> map = new HashMap<>();
//
//        total.calculateTotalValueOnDate(date, map);
//    }
//
//    @Test
//    public void containsKeyTest() {
//        MyDate date = new MyDate("2025-11-14");
//        Map<MyDate, DateResultMap.DateResults> map = new HashMap<>();
//        Map<String, Double> shareMap = new HashMap<>();
//
//        DateResultMap.DateResults dateResults = new DateResultMap.DateResults(
//                0.0,
//                0.0,
//                0.0,
//                0.0,
//                0.0,
//                shareMap
//        );
//
//        map.put(date, dateResults);
//
//        Map<MyDate, String> testResults = new HashMap<>();
//        String res = "Total Value On Date: " + date + map.get(date);
//        testResults.put(date, res);
//
//        assertEquals(testResults.get(date), total.calculateTotalValueOnDate(date, map));
//    }
//
//    @Test
//    public void memoizeTest(){
//        MyDate date = new MyDate("2025-11-14");
//        Map<MyDate, DateResultMap.DateResults> map = new HashMap<>();
//        Map<String, Double> shareMap = new HashMap<>();
//
//        DateResultMap.DateResults dateResults = new DateResultMap.DateResults(
//                0.0,
//                0.0,
//                0.0,
//                0.0,
//                0.0,
//                shareMap
//        );
//
//        map.put(date, dateResults);
//
//        Map<MyDate, String> testResults = new HashMap<>();
//        String res = "Total Value On Date: " + date + map.get(date);
//        testResults.put(date, res);
//
//        System.out.println(total.calculateTotalValueOnDate(date, map));
//
//        MyDate date2 = new MyDate("2025-11-14");
//        assertEquals(testResults.get(date), total.calculateTotalValueOnDate(date2, map));
//    }
}
