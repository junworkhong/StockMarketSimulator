package project.common;

import java.util.ArrayList;
import java.util.List;

public class FedReserve{
   private String name = "Federal Reserve Interest Rates";
   private List<FedDate> fedDates = new ArrayList<>();

   public void addFedDate(FedDate fedDate){
       this.fedDates.add(fedDate);
   }

   @Override
   public String toString(){
       return this.name + "\n" + this.fedDates.toString();
   }
}
