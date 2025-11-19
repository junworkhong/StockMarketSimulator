package project.data;

import project.common.FedDate;
import project.common.FedReserve;
import project.common.MyDate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FedReserveReader {
    public static void main(String[] args) {
        String filepath = "C:\\Users\\junyo\\OneDrive\\Documents\\School\\Third Year\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs\\";
        String filename = "Federal Reserve TBSM3.csv";
        File directory = new File(filepath + filename);
//        File[] filesAndDirs = directory.listFiles();

        FedReserve fedReserve = new FedReserve();

        try {
            if (directory.exists() && !directory.isDirectory()) {
                FileReader filer = new FileReader(directory);
                BufferedReader buffer = new BufferedReader(filer);

                String line = buffer.readLine();
                int i = 0;
                while ((line = buffer.readLine()) != null) {
                    String[] split = line.split(",");
                    MyDate date = new MyDate(split[0]);
                    FedDate fedDate = new FedDate(date,Double.parseDouble(split[1]));
                    fedReserve.addFedDate(fedDate);

                    if (i == 50)
                        break;
                    i++;
                }
                buffer.close();
                filer.close();
                System.out.println(fedReserve);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
