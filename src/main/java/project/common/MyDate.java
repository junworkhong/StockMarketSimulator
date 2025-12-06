package project.common;

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

        if (this.date.equals(d2.date)) {
            return 0;
        }

        String[] split1 = this.date.split("-");
        String[] split2 = d2.date.split("-");

        int year1 = Integer.parseInt(split1[0]);
        int year2 = Integer.parseInt(split2[0]);

        int month1 = Integer.parseInt(split1[1]);
        int month2 = Integer.parseInt(split2[1]);

        int day1 = Integer.parseInt(split1[2]);
        int day2 = Integer.parseInt(split2[2]);

        if (year1 > year2)
            return 1;
        else if (year1 == year2 && month1 > month2)
            return 1;
        else if (year1 == year2 && month1 == month2 && day1 > day2)
            return 1;
        else
            return -1;
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
