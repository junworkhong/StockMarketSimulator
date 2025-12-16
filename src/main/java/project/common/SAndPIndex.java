package project.common;

import java.util.*;

public class SAndPIndex extends Investment{
    private final List<PriceDate> prices;
    private final Map<MyDate, PriceDate> priceMap;

    public SAndPIndex(String name) {
        super(name);
        this.prices = new ArrayList<>();
        this.priceMap = new TreeMap<>();
    }

    @Override
    public void addPriceDate(PriceDate priceDate) {
        this.prices.add(priceDate);
    }

    public void addPriceMap(MyDate date, PriceDate priceDate){
        this.priceMap.put(date, priceDate);
    }

    public Map<MyDate, PriceDate> getPriceMap() {
        return this.priceMap;
    }

    public List<PriceDate> getPrices() {
        return this.prices;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nPrices: " + this.prices;
    }
}
