package com.dvm;

public class TeaLatte extends Tea{
    private static final int money = 4;

    public TeaLatte() {

        super(200, 100,1, 0, 10,  6, "Sütlü Çay");
    }
    public static int getMoney() {
        return money;
    }

}
