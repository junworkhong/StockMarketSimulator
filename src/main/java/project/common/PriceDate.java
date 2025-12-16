package project.common;

import java.text.DecimalFormat;

public class PriceDate {
    private final double close;
    private final long volume;
    private final double open;
    private final double high;
    private final double low;


    public PriceDate(double close,
                     long volume,
                     double open,
                     double high,
                     double low) {
        this.close = close;
        this.volume = volume;
        this.open = open;
        this.high = high;
        this.low = low;
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
        DecimalFormat df = new DecimalFormat("#.##");
        return " Close: $" + df.format(this.close) + " Volume: " + this.volume + " Open: $" + df.format(this.open) + " High: $" + df.format(this.high) + " Low: $" + df.format(this.low);
    }
}
