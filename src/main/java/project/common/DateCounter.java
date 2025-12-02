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
        File directory = new File(path.toString() + "//AAPL Stock (Apple).csv");
//        File[] filesAndDirs = directory.listFiles();

        try {
            if (directory.isFile()) {
                FileReader filer = new FileReader(directory);
                BufferedReader buffer = new BufferedReader(filer);

                String line = buffer.readLine();
                while ((line = buffer.readLine()) != null) {
                    String[] split = line.split(",");

                    MyDate date = new MyDate(split[0]);
                    DateList.add(date);
//                    String regex = "[$\\s]";

//                    for (int a = 1; a < split.length; a++) {
//                        split[a] = split[a].replaceAll(regex, "");
//                    }
//
//                    String regex2 = "[.0\\s]";
//                    split[2] = split[2].replaceAll(regex2, "");
//
//                    PriceDate priceDate = new PriceDate(
//                            date,
//                            Double.parseDouble(split[1]),
//                            Long.parseLong(split[2]),
//                            Double.parseDouble(split[3]),
//                            Double.parseDouble(split[4]),
//                            Double.parseDouble(split[5])
//                    );
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
