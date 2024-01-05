package com.dvm;

public class Coffee extends DrinkVendingMachine {

    private String name;
    public Coffee(int water, int milk, int disposableCups, int coffeeBeans, int money, int tea, int sugar, String name) {
        super(water, milk, disposableCups, coffeeBeans, tea, sugar);
        this.name = name;
    }
}
