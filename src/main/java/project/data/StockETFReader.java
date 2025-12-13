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

public class StockETFReader {
    private static StockETFReader instance;

    public Map<String, StockETF> readStockETFs(){
        Path path = Paths.get(System.getProperty("user.dir"), "CSVs");
        File directory = new File(path.toString());
        File[] filesAndDirs = directory.listFiles();
        Map<String, StockETF> database = new HashMap<>();

        try {
            if (filesAndDirs != null) {
                for (File file : filesAndDirs) {
                    if (file.getName().endsWith(".csv") && (file.getName().toLowerCase().contains("stock")
                            || file.getName().toLowerCase().contains("etf"))) {
                        FileReader filer = new FileReader(file);
                        BufferedReader buffer = new BufferedReader(filer);

                        String line = buffer.readLine();
                        String name = file.getName().substring(0, file.getName().indexOf(" "));

                        String securityType = file.getName().substring(name.length() + 1, file.getName().indexOf(" ", name.length() + 1));
                        Security myType = null;

                        if (securityType.toLowerCase().equals("stock")) {
                            myType = Security.Stock;
                        }else if (securityType.toLowerCase().equals("etf")) {
                            myType = Security.ETF;
                        }else
                            throw new IllegalArgumentException("Invalid csv names");

                        StockETF myStockETF = new StockETF(name, myType);

                        int i = 0;
                        while ((line = buffer.readLine()) != null) {
                            String[] split = line.split(",");

                            if (split.length == 0)
                                throw new IllegalStateException("Issue with .csv");

                            MyDate date = new MyDate(split[0]);
                            String regex = "[$\\s]";

                            for (int a = 1; a < split.length; a++) {
                                if (split[a] == null)
                                    throw new IllegalStateException("Issue with .csv");
                                split[a] = split[a].replaceAll(regex, "");
                            }

                            String regex2 = "[.0\\s]";
                            split[2] = split[2].replaceAll(regex2, "");

                            PriceDate priceDate = new PriceDate(
                                    Double.parseDouble(split[1]),
                                    Long.parseLong(split[2]),
                                    Double.parseDouble(split[3]),
                                    Double.parseDouble(split[4]),
                                    Double.parseDouble(split[5])
                            );
                            myStockETF.addPriceMap(date, priceDate);
                        }
                        database.put(name, myStockETF);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        return database;
    }

    public static StockETFReader getInstance() {
        if (instance == null) {
            instance = new StockETFReader();
        }
        return instance;
    }
}
