package project.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateCounter {
    private static List<MyDate> DateList = new ArrayList<MyDate>();

    public DateCounter() {
        Path path = Paths.get(System.getProperty("user.dir"), "CSVs");
        // File directory = new File(path.toString() + "//AAPL Stock (Apple).csv");
        File directory = new File(path.toString() + File.separator + "AAPL Stock.csv");

        try {
            if (directory.isFile()) {
                FileReader filer = new FileReader(directory);
                BufferedReader buffer = new BufferedReader(filer);

                String line = buffer.readLine();
                while ((line = buffer.readLine()) != null) {
                    String[] split = line.split(",");

                    if (split.length == 0 || split[0] == null)
                        throw new IllegalStateException("Issue with date on csv");

                    MyDate date = new MyDate(split[0]);
                    DateList.add(date);
                }
            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<MyDate> getDateList() {
        return DateList;
    }

    @Override
    public String toString() {
        String output = "";
        for (MyDate date : DateList) {
            output += date + "\n";
        }

        return output;
    }

    public static void main(String[] args) {
        DateCounter date = new DateCounter();
        System.out.println(date);
    }
}
