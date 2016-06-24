package com.kohanevich.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denis on 6/25/2016
 */
public class AtmCalculator {

    private Map<Integer, Integer> bank = new HashMap<>();

    public AtmCalculator() {
        bank.put(20, 20);
        bank.put(50, 20);
        bank.put(100, 20);
        bank.put(200, 20);
        bank.put(500, 20);
    }

    public void withdraw(int amount) {
        Integer integer = bank.get(amount);
        if (integer != null) {
            bank.put(amount, bank.get(amount) - 1);
        }
    }

    public int checkAmount() {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : bank.entrySet()) {
            sum += entry.getKey() * entry.getValue();
        }
        return sum;
    }
}
