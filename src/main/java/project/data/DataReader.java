package project.data;

import java.io.BufferedReader;
import java.io.FileReader;

public class DataReader {
    public static void main(String[] args) {
        String filename = "C:\\Users\\jhong\\Documents\\Documents\\Second Degree\\Columbia\\Fall 2025\\Clean OOD\\Project\\StockMarketSimulator\\CSVs\\sap500indexbenchmark.csv";
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br =  new BufferedReader(fr);

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (i == 50)
                    break;
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
