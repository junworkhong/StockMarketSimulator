package project.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockETF extends Investment{
    private final Security securityType;
    private final List<PriceDate> prices;
    private final Map<MyDate, PriceDate> priceMap;

    public StockETF(String tickerName, Security securityType) {
        super(tickerName);
        this.securityType = securityType;
        this.prices = new ArrayList<>();
        this.priceMap = new HashMap<>();
    }

    @Override
    public void addPriceDate (PriceDate priceDate){
        this.prices.add(priceDate);
    }

    public void addPriceMap(MyDate date, PriceDate priceDate){
        this.priceMap.put(date, priceDate);
    }

    public String getTickerName() {
        return this.name;
    }

    public Security getSecurityType() {
        return this.securityType;
    }

    public List<PriceDate> getPrices() {
        return this.prices;
    }

    public Map<MyDate, PriceDate> getPriceMap() {
        return this.priceMap;
    }

    @Override
    public String toString() {
//        DecimalFormat df = new DecimalFormat("0.##########");
        return "Ticker Name: " + this.name + "\nType: " + this.securityType + "\nPrices: " + this.prices;
    }
}
