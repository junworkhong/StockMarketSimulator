package project.data;

import project.common.MyDate;
import project.common.Security;
import project.common.StockETF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataReader {
    public static void main(String[] args) {
        String filename = "C:\\Users\\jhong\\Documents\\Documents\\Second Degree\\Columbia\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs\\AAPL Stock (Apple).csv";
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br =  new BufferedReader(fr);

            String line = br.readLine();
            String name = "AAPL";

            List<MyDate> dateList = new ArrayList<>();
            List<Double> closeList = new ArrayList<>();
            List<Double> volumeList = new ArrayList<>();
            List<Double> openList = new ArrayList<>();
            List<Double> highList = new ArrayList<>();
            List<Double> lowList = new ArrayList<>();

            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
//                Map<MyDate, StockETF> myMap = new HashMap<>();
                MyDate date = new MyDate(split[0]);
                String regex = "[$\\s]";

                for (int a = 1; a < split.length; a++) {
                    split[a] = split[a].replaceAll(regex, "");
                }

                dateList.add(date);
                closeList.add(Double.parseDouble(split[1]));
                volumeList.add(Double.parseDouble(split[2]));
                openList.add(Double.parseDouble(split[3]));
                highList.add(Double.parseDouble(split[4]));
                lowList.add(Double.parseDouble(split[5]));
//                for (int j = 1; j < split.length; j++) {
//                    if (j == 1)
//                        openList.add(Double.parseDouble(split[j]));
//                    else if (j == 2)
//                        highList.add(Double.parseDouble(split[j]));
//                    else if (j == 3)
//                        lowList.add(Double.parseDouble(split[j]));
//                    else if (j == 4)
//                        closeList.add(Double.parseDouble(split[j]));
//                    else if (j == 5)
//                        volumeList.add(Double.parseDouble(split[j]));
//                }
//                StockETF myStockETF = new StockETF(date, list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
//                myMap.put(date, myStockETF);
//                System.out.println(line);
//                System.out.println(myMap);
                if (i == 50)
                    break;
                i++;
//                myMap.put()
            }
            StockETF myStockETF =  new StockETF(name, Security.Stock, dateList, closeList, volumeList, openList, highList, lowList);
            myStockETF.returnStockETF();
//            for (int k = 0; k < dateList.size(); k++) {
//
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
