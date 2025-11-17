package project.common;

public class PriceDate {
    private MyDate date;
    private double close;
    private long volume;
    private double open;
    private double high;
    private double low;


    public PriceDate(MyDate date, double close, long volume, double open, double high, double low) {
        this.close = close;
        this.volume = volume;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
    }

    public MyDate getDate() {
        return this.date;
    }

    public double getClose() {
        return this.close;
    }

    public long getVolume() {
        return this.volume;
    }

    public double getOpen() {
        return this.open;
    }

    public double getHigh() {
        return this.high;
    }

    public double getLow() {
        return this.low;
    }

    @Override
    public String toString() {
        return "\nDate: " + this.date.toString() + " Close: $" + this.close + " Volume: " + this.volume + " Open: $" + this.open + " High: $" + this.high + " Low: $" + this.low;
    }
}
