package com.dvm;

public class Cappuccino extends Coffee{

    private static final int money = 6;

    public Cappuccino() {
        super(250, 0, 1, 12, 5, 0, 0, "espresso");
    }
    public static int getMoney() {
        return money;
    }

}
