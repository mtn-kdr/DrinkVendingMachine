package com.dvm;

interface MachineOperations {

    void showSupply(DrinkVendingMachine dvm);
    void buy(DrinkVendingMachine dvm);
    String checkWhatIsNotEnoughCoffee(DrinkVendingMachine dvm, Coffee coffee);
    String checkWhatIsNotEnoughTea(DrinkVendingMachine dvm, Tea tea);
    void fill(DrinkVendingMachine dvm);
    void listDrinks(DrinkVendingMachine dvm);
    void loadStockFromFile(DrinkVendingMachine drinkVendingMachine);
    void saveStockToFile();

}
