package com.dvm;

public class UnsweetenedTea extends Tea{
    private static final int money = 5;
    public  UnsweetenedTea(){

        super(200, 0,1, 0, 10, 0 , "Sütlü Çay");
    }
    public static int getMoney() {
        return money;
    }

}
