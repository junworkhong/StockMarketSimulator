package project.common;

import java.text.DecimalFormat;
import java.util.List;

public class StockETF {
    private String name;
    private Security securityType;
    private List<MyDate> date;
    private List<Double> close;
    private List<Double> volume;
    private List<Double> open;
    private List<Double> high;
    private List<Double> low;

    public StockETF(String name, Security securityType, List<MyDate> date, List<Double> close, List<Double> volume, List<Double> open, List<Double> high, List<Double> low) {
        this.name = name;
        this.securityType = securityType;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public Security getSecurityType() {
        return securityType;
    }

    public List<MyDate> getDate() {
        return this.date;
    }

    public List<Double> getClose() {
        return this.close;
    }

    public List<Double> getVolume() {
        return this.volume;
    }

    public List<Double> getOpen() {
        return this.open;
    }

    public List<Double> getHigh() {
        return this.high;
    }

    public List<Double> getLow() {
        return this.low;
    }

    public void returnStockETF() {
        System.out.println("Name: " + this.name + "\nType: " + this.securityType);
        DecimalFormat df = new DecimalFormat("0.##########");
        for (int i = 0; i < this.date.size(); i++) {
            System.out.println("Date: " + this.date.get(i).toString() + " Close: $" + this.close.get(i) + " Volume: " + df.format(this.volume.get(i)) + " Open: $" + this.open.get(i) + " High: $" + this.high.get(i) + " Low: $" + this.low.get(i));
        }
    }
}
