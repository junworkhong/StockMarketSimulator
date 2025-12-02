package project.common;

public abstract class Investment {
    protected String name;

    protected Investment(String name){
        this.name = name;
    }

    public abstract void addPriceDate (PriceDate priceDate);

    public String getName() {
        return this.name;
    }
}
