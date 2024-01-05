package com.dvm;

public class Latte extends Coffee {
    private static final int money = 5;
    public Latte() {
        super(250, 0, 1, 10, 5, 0, 0, "Latte");
    }
    public static int getMoney() {
        return money;
    }
}

