package com.dvm;

public class SweetTea extends Tea {

    private static final int money = 3;
    public SweetTea() {

        super(200, 0,1, 0,  5, 3,"Çay");
    }
    public static int getMoney() {
        return money;
    }

}
