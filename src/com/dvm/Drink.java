package com.dvm;

public class Drink {
    private String name;
    private double money;
    public Drink(String name, double money) {
        this.name = name;
        this.money = money;
    }
    public String getName() {
        return name;
    }
    public double getMoney() {
        return money;
    }
}
