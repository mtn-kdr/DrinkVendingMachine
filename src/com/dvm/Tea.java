package com.dvm;

public class Tea extends DrinkVendingMachine{
    private String name;

    public Tea(int water, int milk, int disposableCups, int coffeeBeans ,int tea, int sugar , String name) {
        super(water, milk, coffeeBeans, 1,  tea, sugar);
        this.name = name;
    }

}
