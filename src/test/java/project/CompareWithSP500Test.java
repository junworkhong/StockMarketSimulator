package project;

import org.junit.Test;
import project.common.*;
import project.processor.CompareWithSP500;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CompareWithSP500Test {

    private SAndPIndex makeFakeSP500(MyDate start, double startClose, MyDate end, double endClose) {
        SAndPIndex idx = new SAndPIndex("S&P 500");
        idx.addPriceMap(start, new PriceDate(startClose, 0L, 0.0, 0.0, 0.0));
        idx.addPriceMap(end, new PriceDate(endClose, 0L, 0.0, 0.0, 0.0));
        return idx;
    }

    private Portfolio makePortfolioWithHoldings(double initialInvestment,
                                                Map<String, StockETF> userStockMap,
                                                Map<String, Double> shares) {
        Map<String, Integer> allocations = new HashMap<>();
        Map<String, Double> threshold = new HashMap<>();
        DateResultMap dateResults = new DateResultMap();
        int stopLoss = 0, risk = 0, target = 0;

        return new Portfolio(
                initialInvestment,
                allocations,
                stopLoss,
                risk,
                target,
                threshold,
                userStockMap,
                shares,
                dateResults
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputs_throwIllegalArgumentException() {
        MyDate start = new MyDate("2020-01-02");
        MyDate end = new MyDate("2020-01-03");

        CompareWithSP500 op = new CompareWithSP500(makeFakeSP500(start, 100.0, end, 110.0));

        op.execute(null, start, end);
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingSPData_throwIllegalStateException() {
        MyDate start = new MyDate("2020-01-02");
        MyDate end = new MyDate("2020-01-03");

        // 故意缺 end
        SAndPIndex sp = new SAndPIndex("S&P 500");
        sp.addPriceMap(start, new PriceDate(100.0, 0L, 0.0, 0.0, 0.0));

        CompareWithSP500 op = new CompareWithSP500(sp);

        Portfolio p = makePortfolioWithHoldings(
                10000.0,
                new HashMap<>(),
                new HashMap<>()
        );

        op.execute(p, start, end);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidSPStartClose_throwIllegalStateException() {
        MyDate start = new MyDate("2020-01-02");
        MyDate end = new MyDate("2020-01-03");

        CompareWithSP500 op = new CompareWithSP500(makeFakeSP500(start, 0.0, end, 110.0));

        Portfolio p = makePortfolioWithHoldings(
                10000.0,
                new HashMap<>(),
                new HashMap<>()
        );

        op.execute(p, start, end);
    }

    @Test
    public void testNormalCase_setsPortfolioAndSPFields() {
        MyDate start = new MyDate("2020-01-02");
        MyDate end = new MyDate("2020-01-03");

        // S&P: 100 -> 110 => +10%
        CompareWithSP500 op = new CompareWithSP500(makeFakeSP500(start, 100.0, end, 110.0));


        Map<String, StockETF> userMap = new HashMap<>();
        Map<String, Double> shares = new HashMap<>();

        // AAPL: end close = 200, shares = 10
        StockETF aapl = new StockETF("AAPL", Security.Stock);
        aapl.addPriceMap(end, new PriceDate(200.0, 0L, 0.0, 0.0, 0.0));
        userMap.put("AAPL", aapl);
        shares.put("AAPL", 10.0);

        // GOOGL: shares = 0 (should skip)
        StockETF googl = new StockETF("GOOGL", Security.Stock);
        googl.addPriceMap(end, new PriceDate(999.0, 0L, 0.0, 0.0, 0.0));
        userMap.put("GOOGL", googl);
        shares.put("GOOGL", 0.0);

        // JPM: shares > 0 but no price on end (should skip)
        StockETF jpm = new StockETF("JPM", Security.Stock);
        userMap.put("JPM", jpm);
        shares.put("JPM", 5.0);

        Portfolio p = makePortfolioWithHoldings(10000.0, userMap, shares);


        double desiredCash = 1000.0;
        p.addBudget(desiredCash - p.getBudget());

        op.execute(p, start, end);

        // finalPortfolioValue = cash 1000 + AAPL 10*200 = 3000
        // profitLoss = 3000 - 10000 = -7000
        assertEquals(-7000.0, p.getTotalProfitLoss(), 1e-9);
        assertEquals(-70.0, p.getTotalProfitLossPercent(), 1e-9);

        // S&P return = (110-100)/100*100 = 10
        assertEquals(10.0, p.getSPReturn(), 1e-9);

        // over/under = -70 - 10 = -80
        assertEquals(-80.0, p.getSPReturnOverUnder(), 1e-9);
    }
    @Test
    public void testDefaultConstructor_covered() {
        // 只要能 new 出来，就覆盖了 CompareWithSP500()
        new CompareWithSP500();
    }

}
