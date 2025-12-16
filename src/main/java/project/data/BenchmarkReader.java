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

    public SAndPIndex readSAndP() {
        Path path = Paths.get(System.getProperty("user.dir"), "CSVs");
        String filename = "sap500indexbenchmark.csv";
        String filepath = path.toString();
        File directory = new File(filepath + "\\" + filename);

        String name = "S&P 500 Index Benchmark";
        SAndPIndex benchmark = new SAndPIndex(name);

        try {
            if (directory.exists()) {
                FileReader filer = new FileReader(directory);
                BufferedReader buffer = new BufferedReader(filer);

                String line = buffer.readLine();
                while ((line = buffer.readLine()) != null) {
                    String[] split = line.split(",");

                    if (split.length != 6)
                        continue;

                    MyDate date = new MyDate(split[0].replace("\uFEFF", "").trim());
//                    String regex = "[.0\\s]";

                    if (split[1] == null || split[1].isEmpty()
                            || split[2] == null || split[2].isEmpty()
                            || split[3] == null || split[3].isEmpty()
                            || split[4] == null || split[4].isEmpty()
                            || split[5] == null || split[5].isEmpty())
                        continue;

                    split[5] = split[5].replaceAll("\\.0$", "");

                    PriceDate priceDate = new PriceDate(
                            Double.parseDouble(split[4]),
                            Long.parseLong(split[5]),
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[2]),
                            Double.parseDouble(split[3])
                    );
                    benchmark.addPriceMap(date, priceDate);
                }
            }
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return benchmark;
    }

    public static BenchmarkReader getInstance() {
        if (instance == null) {
            instance = new  BenchmarkReader();
        }
        return instance;
    }
}
