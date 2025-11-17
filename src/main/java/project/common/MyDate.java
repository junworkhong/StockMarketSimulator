package project.common;

public class MyDate {
    private final String date;

    public MyDate (String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return date;
    }
}
