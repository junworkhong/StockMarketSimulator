package project.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class BenchmarkReader {
    public static void main(String[] args) {
        File directory = new File("C:\\Users\\junyo\\OneDrive\\Documents\\School\\Third Year\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs");
        File[] filesAndDirs = directory.listFiles();
        try {
            if (filesAndDirs != null) {
                for (File file : filesAndDirs) {
                    if (file.getName().endsWith(".csv") && file.getName().toLowerCase().contains("sap500indexbenchmark")) {
                        FileReader filer = new FileReader(file);
                        BufferedReader buffer = new BufferedReader(filer);

                        String line = buffer.readLine();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
