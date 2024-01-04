package com.dvm;

public class Tea extends DrinkVendingMachine{
    private String name;

    public Tea(int water, int milk, int disposableCups, int coffeeBeans ,int tea, int sugar ,int money, String name) {
        super(water, milk, coffeeBeans, 1, money, tea, sugar);
        this.name = name;
    }
}
