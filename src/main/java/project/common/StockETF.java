package project.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StockETF {
    private final String tickerName;
    private final Security securityType;
    private final List<PriceDate> prices;

    public StockETF(String tickerName, Security securityType) {
        this.tickerName = tickerName;
        this.securityType = securityType;
        this.prices = new ArrayList<>();
    }

    public void addPriceDate (PriceDate priceDate){
        this.prices.add(priceDate);
    }

    public String getTickerName() {
        return this.tickerName;
    }

    public Security getSecurityType() {
        return this.securityType;
    }

    public List<PriceDate> getPrices() {
        return this.prices;
    }

    @Override
    public String toString() {
//        DecimalFormat df = new DecimalFormat("0.##########");
        return "Ticker Name: " + this.tickerName + "\nType: " + this.securityType +  "\nPrices: " + this.prices;
    }
}
