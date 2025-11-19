package project.data;

import project.common.MyDate;
import project.common.PriceDate;
import project.common.Security;
import project.common.StockETF;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//TODO
//DONE: Make loop so all .csvs can be read and create objects
//DONE: HashMap for storing date and price
//DONE: Maybe classes for S&P 500 index and Federal Reserve? Maybe not
//Portfolio Class

public class StockETFReader {
    public static void main(String[] args) {
//        String filename = "C:\\Users\\jhong\\Documents\\Documents\\Second Degree\\Columbia\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs\\AAPL Stock (Apple).csv";
        String filename = "C:\\Users\\junyo\\OneDrive\\Documents\\School\\Third Year\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs\\AAPL Stock (Apple).csv";
        File directory = new File("C:\\Users\\junyo\\OneDrive\\Documents\\School\\Third Year\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs");

        Path path = Paths.get(System.getProperty("user.dir"), "CSVs");
        System.out.println(path);
//        Paths.get("StockMarketSimulator", "CSVs");
        directory = new File(path.toString());
        File[] filesAndDirs = directory.listFiles();
        Map<String, StockETF> database = new HashMap<>();

        try {
            if (filesAndDirs != null) {
                for (File file : filesAndDirs) {
                    if (file.getName().endsWith(".csv") && (file.getName().toLowerCase().contains("stock") || file.getName().toLowerCase().contains("etf"))) {
                        FileReader filer = new FileReader(file);
                        BufferedReader buffer = new BufferedReader(filer);

                        String line = buffer.readLine();
                        String name = file.getName().substring(0, file.getName().indexOf(" "));
//                        System.out.println(name);

                        String securityType = file.getName().substring(name.length() + 1, file.getName().indexOf(" ", name.length() + 1));
//                        System.out.println(securityType);
                        Security myType = null;

                        if (securityType.toLowerCase().equals("stock")) {
                            myType = Security.Stock;
//                            System.out.println(myType.toString());
                        }else if (securityType.toLowerCase().equals("etf")) {
                            myType = Security.ETF;
//                            System.out.println(myType.toString());
                        }

                        StockETF myStockETF = new StockETF(name, myType);
                        int i = 0;
                        while ((line = buffer.readLine()) != null) {
                            String[] split = line.split(",");

                            MyDate date = new MyDate(split[0]);
                            String regex = "[$\\s]";

                            for (int a = 1; a < split.length; a++) {
                                split[a] = split[a].replaceAll(regex, "");
                            }

                            String regex2 = "[.0\\s]";
                            split[2] = split[2].replaceAll(regex2, "");

                            PriceDate priceDate = new PriceDate(
                                    date,
                                    Double.parseDouble(split[1]),
                                    Long.parseLong(split[2]),
                                    Double.parseDouble(split[3]),
                                    Double.parseDouble(split[4]),
                                    Double.parseDouble(split[5])
                            );
                            myStockETF.addPriceDate(priceDate);
                            if (i == 100)
                                break;
                            i++;
                        }
//                        System.out.println(myStockETF);
                        database.put(name, myStockETF);
//                        System.out.println(name);
                        System.out.println(database.get(name) + "\n");
                    }
//                    System.exit(1);
                }
            }
//            FileReader fr = new FileReader(filename);
//            BufferedReader br =  new BufferedReader(fr);
//
//            String line = br.readLine();
//            String name = "AAPL";
//            StockETF myStockETF = new StockETF(name, Security.Stock);

//            List<MyDate> dateList = new ArrayList<>();
//            List<Double> closeList = new ArrayList<>();
//            List<Double> volumeList = new ArrayList<>();
//            List<Double> openList = new ArrayList<>();
//            List<Double> highList = new ArrayList<>();
//            List<Double> lowList = new ArrayList<>();
//            int i = 0;
//            while ((line = br.readLine()) != null) {
//                String[] split = line.split(",");
////                Map<MyDate, StockETF> myMap = new HashMap<>();
//
//                MyDate date = new MyDate(split[0]);
//                String regex = "[$\\s]";
//
//                for (int a = 1; a < split.length; a++) {
//                    split[a] = split[a].replaceAll(regex, "");
//                }
//
//                PriceDate priceDate = new PriceDate(
//                        date,
//                        Double.parseDouble(split[1]),
//                        Long.parseLong(split[2]),
//                        Double.parseDouble(split[3]),
//                        Double.parseDouble(split[4]),
//                        Double.parseDouble(split[5])
//                );
//                myStockETF.addPriceDate(priceDate);
//
////                dateList.add(date);
////                closeList.add(Double.parseDouble(split[1]));
////                volumeList.add(Double.parseDouble(split[2]));
////                openList.add(Double.parseDouble(split[3]));
////                highList.add(Double.parseDouble(split[4]));
////                lowList.add(Double.parseDouble(split[5]));
////                for (int j = 1; j < split.length; j++) {
////                    if (j == 1)
////                        openList.add(Double.parseDouble(split[j]));
////                    else if (j == 2)
////                        highList.add(Double.parseDouble(split[j]));
////                    else if (j == 3)
////                        lowList.add(Double.parseDouble(split[j]));
////                    else if (j == 4)
////                        closeList.add(Double.parseDouble(split[j]));
////                    else if (j == 5)
////                        volumeList.add(Double.parseDouble(split[j]));
////                }
////                StockETF myStockETF = new StockETF(date, list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
////                myMap.put(date, myStockETF);
////                System.out.println(line);
////                System.out.println(myMap);
//                if (i == 50)
//                    break;
//                i++;
////                myMap.put()
//            }
//            System.out.println(myStockETF);
//            for (int k = 0; k < dateList.size(); k++) {
//
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
