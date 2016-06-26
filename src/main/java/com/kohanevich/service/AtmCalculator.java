package com.kohanevich.service;

import com.google.common.collect.Maps;

import java.util.*;

import static com.kohanevich.service.Status.AVAILABLE_ONLY;
import static com.kohanevich.service.Status.EMPTY_ATM;
import static com.kohanevich.service.Status.build;

/**
 * Created by Denis on 6/25/2016
 */
public class AtmCalculator implements Calculator {

    private static final Integer MAX_COUNT_BANKNOTES = 10;
    private Map<Integer, Integer> atm = new HashMap<>();

    public AtmCalculator() {
    }

    public AtmCalculator(Map<Integer, Integer> atm) {
        this.atm = atm;
    }

    public Status withdraw(int requestedAmount) {
        List<Integer> denominations = new ArrayList<>(atm.keySet());
        Collections.sort(denominations);

        if (requestedAmount <= 0) {
            return offerMinDenomination();
        }

        int remain = requestedAmount;
       /* atm.keySet().stream().sorted((i1, i2) -> i2.compareTo(i1)).forEach(i -> {
            for () {
                if (remain > i) {
                    int countToWithdraw = remain / i;
                    int remain = remain % i;
                    atm.put(i, atm.get(i) - countToWithdraw);
                }
            }
        });*/
        int totalBanknotes = 0;
        HashMap<Integer, Integer> map = Maps.newHashMap();
        Collections.reverse(denominations);

        for (Integer denomination : denominations) {
            if (remain >= denomination && atm.get(denomination) != 0 && totalBanknotes < MAX_COUNT_BANKNOTES) {
                int currentBanknotes = remain / denomination;
                if (atm.get(denomination) < currentBanknotes) {
                    currentBanknotes = atm.get(denomination);
                }
                if (totalBanknotes + currentBanknotes <= MAX_COUNT_BANKNOTES) {
                    totalBanknotes += currentBanknotes;
                    remain = remain % denomination;
                    map.put(denomination, currentBanknotes);
                } else {
                    int availableCount = MAX_COUNT_BANKNOTES - totalBanknotes;
                    totalBanknotes += availableCount;
                    remain -= denomination * availableCount;
                    map.put(denomination, availableCount);
                }
            }
        }

        if (map.isEmpty()) {
            return offerMinDenomination();
        } else if (remain != 0) {
            return build(AVAILABLE_ONLY, requestedAmount - remain);
        } else {
            map.forEach((key,value) -> atm.put(key, atm.get(key) - value));
            return Status.AVAILABLE;
        }
    }

    private Status offerMinDenomination() {
        return atm.keySet().stream().sorted()
                .filter(input -> atm.get(input) > 0).findFirst()
                .map(input -> build(AVAILABLE_ONLY, input))
                .orElse(EMPTY_ATM);
    }

    @Override
    public void deposit(int amount) {
        //TODO
    }

    public Map<Integer, Integer> getAtm() {
        return atm;
    }

    public void setAtm(Map<Integer, Integer> atm) {
        this.atm = atm;
    }
}

enum Status {
    AVAILABLE, AVAILABLE_ONLY, EMPTY_ATM;
    public int amount;

    public static Status build(Status status, int amount) {
        status.amount = amount;
        return status;
    }
}
