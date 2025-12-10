package project.data;

import project.common.MyDate;
import project.common.PriceDate;
import project.common.SAndPIndex;
import project.common.StockETF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BenchmarkReader {
    private static BenchmarkReader instance;
    private BenchmarkReader() {}

    public SAndPIndex readSAndP() {
        Path path = Paths.get(System.getProperty("user.dir"), "CSVs");
        String filepath = path.toString();
        String filename = "sap500indexbenchmark.csv";
        File directory = new File(filepath + "\\" + filename);
        String name = "S&P 500 Index Benchmark";

        SAndPIndex benchmark = new SAndPIndex(name);
        Map<String, SAndPIndex> database = new HashMap<>();

        try {
            if (directory.exists()) {
                FileReader filer = new FileReader(directory);
                BufferedReader buffer = new BufferedReader(filer);

                String line = buffer.readLine();
                while ((line = buffer.readLine()) != null) {
                    String[] split = line.split(",");

                    MyDate date = new MyDate(split[0]);
                    String regex = "[.0\\s]";
                    if (split.length != 6)
                        continue;

                    split[5] = split[5].replaceAll(regex, "");

                    if (split[1] == null || split[1].isEmpty() || split[2] == null || split[2].isEmpty()
                    || split[3] == null || split[3].isEmpty() || split[4] == null || split[4].isEmpty()
                    || split[5] == null || split[5].isEmpty()) {
                        continue;
                    }

                    PriceDate priceDate = new PriceDate(
                            Double.parseDouble(split[4]),
                            Long.parseLong(split[5]),
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[2]),
                            Double.parseDouble(split[3])
                    );
                    benchmark.addPriceMap(date, priceDate);
                }
                database.put(name, benchmark);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return benchmark;
    }

    public static BenchmarkReader getInstance() {
        if (instance == null) {
            instance = new  BenchmarkReader();
        }
        return instance;
    }

    public static void main(String[] args) {
//        File directory = new File("C:\\Users\\junyo\\OneDrive\\Documents\\School\\Third Year\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs");
        String filepath = "C:\\Users\\junyo\\OneDrive\\Documents\\School\\Third Year\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs\\";
        Path path = Paths.get(System.getProperty("user.dir"), "CSVs");
        filepath = path.toString();
        String filename = "sap500indexbenchmark.csv";
        File directory = new File(filepath + "\\" + filename);
//        File[] filesAndDirs = directory.listFiles();
        String name = "S&P 500 Index Benchmark";

        SAndPIndex benchmark = new SAndPIndex(name);
//        Map<String, SAndPIndex> database = new HashMap<>();
        try {
//            if (filesAndDirs != null) {
//                for (File file : filesAndDirs) {
            if (directory.exists()) {
                FileReader filer = new FileReader(directory);
                BufferedReader buffer = new BufferedReader(filer);

                String line = buffer.readLine();
                int i = 0;
                while ((line = buffer.readLine()) != null) {
                    String[] split = line.split(",");

                    MyDate date = new MyDate(split[0]);
                    String regex = "[.0\\s]";
                    split[5] =  split[5].replaceAll(regex, "");

                    PriceDate priceDate = new PriceDate(
//                            date,
                            Double.parseDouble(split[4]),
                            Long.parseLong(split[5]),
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[2]),
                            Double.parseDouble(split[3])
                    );
//                    benchmark.addPriceDate(priceDate);
                    benchmark.addPriceMap(date, priceDate);
                    if (i == 100)
                        break;
                    i++;
                }
            }
            System.out.println(benchmark);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
