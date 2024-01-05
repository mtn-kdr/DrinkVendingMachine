package com.dvm;

public class Espresso extends Coffee {
    private static final int money = 7;

    public Espresso() {

        super(250, 0, 1, 15, 5, 0, 0, "espresso");
    }
    public static int getMoney() {
        return money;
    }
}
