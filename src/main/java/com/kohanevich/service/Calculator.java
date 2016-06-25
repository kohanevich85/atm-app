package com.kohanevich.service;

/**
 * Created by Denis on 6/25/2016
 */
public interface Calculator {
    public Status withdraw(Integer amount);
    public void deposit(int amount);
}
