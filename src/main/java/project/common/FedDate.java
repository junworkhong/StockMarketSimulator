package project.common;

public class FedDate{
    private final MyDate observationDate;
    private final double interestRate;

    public FedDate(MyDate observationDate, double interestRate){
        this.observationDate = observationDate;
        this.interestRate = interestRate;
    }

    public MyDate getObservationDate(){
        return this.observationDate;
    }

    public double getInterestRate(){
        return this.interestRate;
    }

    @Override
    public String toString(){
        return "\nDate: " + this.observationDate.toString() + " Rate: " + this.interestRate + "%";
    }
}
