package project.common;

import project.ui.SimulatorUI;

import java.util.Comparator;
import java.util.Objects;

public class MyDate implements Comparable<MyDate> {
    private final String date;
    public MyDate (String date) {
        this.date = date;
    }

    @Override
    public int compareTo(MyDate d2) {
        //Returns 1 if d1 is later than d2, i.e. January 2024 vs March 2021
        if (d2 == null || d2.date == null)
            return -1;

        if (this.date.equals(d2.date)) {
            return 0;
        }

        String[] split1 = this.date.split("-");
        String[] split2 = d2.date.split("-");

        if (split1.length != 2 || split2.length != 2)
            throw new IllegalArgumentException("Invalid date format");

        int year1 = 0;
        int year2 = 0;
        int month1 = 0;
        int month2 = 0;
        int day1 = 0;
        int day2 = 0;

        try {
            year1 = Integer.parseInt(split1[0]);
            year2 = Integer.parseInt(split2[0]);

            month1 = Integer.parseInt(split1[1]);
            month2 = Integer.parseInt(split2[1]);

            day1 = Integer.parseInt(split1[2]);
            day2 = Integer.parseInt(split2[2]);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid date format");
        }

        if (year1 > year2)
            return 1;
        else if (year1 == year2 && month1 > month2)
            return 1;
        else if (year1 == year2 && month1 == month2 && day1 > day2)
            return 1;
        else
            return -1;
    }

    public boolean isValidDate (MyDate date) {
        SimulatorUI sim =  new SimulatorUI();
        if (!date.toString().contains("-"))
            return false;

        String[] split = date.toString().split("-");

        if (split.length != 3)
            return false;

        int year;
        int month;
        int day;

        if (sim.checkIfPositiveInteger(split[0], split[1], split[2])) {
            year = Integer.parseInt(split[0]);
            month = Integer.parseInt(split[1]);
            day = Integer.parseInt(split[2]);
        }else
            return false;

        if (year < 2020 || year > 2025 || month < 1 || month > 12 || day < 1 || day > 31)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyDate)) return false;
        MyDate other = (MyDate) o;
        return this.date.equals(other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
