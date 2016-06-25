package com.kohanevich.service;

import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.builder;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class AtmCalculatorTest {

    private AtmCalculator calculator;

    @Before
    public void init() {
        Map<Integer, Integer> initAtm = new HashMap<>();
        initAtm.put(20, 20);  // 1520 - 3*500 - 1*20 - 4
        initAtm.put(50, 20);
        initAtm.put(100, 20);
        initAtm.put(200, 20);
        initAtm.put(500, 20);
        calculator = new AtmCalculator(initAtm);
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][] {
            {500, builder().put(20, 20).put(50, 20).put(100, 20).put(200, 20).put(500, 19).build()},
            {300, builder().put(20, 20).put(50, 20).put(100, 19).put(200, 19).put(500, 20).build()},
            {250, builder().put(20, 20).put(50, 19).put(100, 20).put(200, 19).put(500, 20).build()},
        };
    }

    @DataProvider
    public static Object[][] dataErrorCase() {
        return new Object[][] {
                {-5, 20},
                {0, 20},
                {5, 20},
                {30, 20},
                {25, 20},
                {310, 300},
                {310, 300},
                {9999, 5000},
        };
    }

    @Test
    @UseDataProvider("data")
    public void testWithdraw(int withdrawAmount, Map<Integer, Integer> expectedAtm) {
        calculator.withdraw(withdrawAmount);
        assertEquals(expectedAtm, calculator.getAtm());
    }

    @Test
    @UseDataProvider("dataErrorCase")
    public void testWithdrawRestrictedAmount(int withdrawAmount, int allowedAmount) {
        Status withdraw = calculator.withdraw(withdrawAmount);
        assertEquals(allowedAmount, withdraw.amount);
    }
}