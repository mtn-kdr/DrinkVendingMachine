package com.dvm;

public class Latte extends Coffee {
    private static final int money = 5;
    public Latte() {
        super(250, 100, 1, 10,  0, 1, "Latte");
    }
    public static int getMoney() {
        return money;
    }
}

