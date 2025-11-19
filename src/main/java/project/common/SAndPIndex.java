package project.common;

import java.util.ArrayList;
import java.util.List;

public class SAndPIndex extends Investment{
    private final List<PriceDate> prices;

    public SAndPIndex(String name) {
        super(name);
        this.prices = new ArrayList<>();
    }

    @Override
    public void addPriceDate(PriceDate priceDate) {
        this.prices.add(priceDate);
    }

    public List<PriceDate> getPrices() {
        return this.prices;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nPrices: " + this.prices;
    }
}
