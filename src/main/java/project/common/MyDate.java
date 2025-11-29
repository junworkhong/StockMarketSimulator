package project.common;

import java.util.Objects;

public class MyDate {
    private final String date;

    public MyDate (String date) {
        this.date = date;
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
