package com.dvm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class DrinkVendingMachine implements MachineOperations {
    static Scanner scanner = new Scanner(System.in);
    static User user = User.getInstance();

    private int water;
    private int milk;
    private int coffeeBeans;
    private int disposableCups;
    private int tea;
    private int sugar;
    private Double moneyCase = 0.0;

    public DrinkVendingMachine(int water, int milk,  int disposableCups, int coffeeBeans, int tea, int sugar) {
        this.water = water;
        this.milk = milk;
        this.disposableCups = disposableCups;
        this.coffeeBeans = coffeeBeans;
        this.tea = tea;
        this.sugar=sugar;
    }

    public double getMoneyCase(){
        return moneyCase;
    }
    public void setMoneyCase(double moneyCase){
        this.moneyCase = moneyCase;
    }

    public int getWater() {
        return water;
    }

    public int getTea() {
        return tea;
    }

    public void setTea(int tea) {
        this.tea = tea;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getMilk() {
        return milk;
    }

    public void setMilk(int milk) {
        this.milk = milk;
    }

    public int getCoffeeBeans() {
        return coffeeBeans;
    }

    public void setCoffeeBeans(int coffeeBeans) {
        this.coffeeBeans = coffeeBeans;
    }

    public int getDisposableCups() {
        return disposableCups;
    }

    public void setDisposableCups(int disposableCups) {
        this.disposableCups = disposableCups;
    }

    public void saveStockToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("stock.txt"))) {

            writer.write("Water: " + this.getWater() + "\n");
            writer.write("Milk: " + this.getMilk() + "\n");
            writer.write("Coffee Beans: " + this.getCoffeeBeans() + "\n");
            writer.write("Disposable Cups: " + this.getDisposableCups() + "\n");
            writer.write("Money: " + this.getMoneyCase() + "\n");
            writer.write("Tea: " + this.getTea() + "\n");
            writer.write("Sugar: " + this.getSugar() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStockFromFile(DrinkVendingMachine dvm) {
        try (BufferedReader reader = new BufferedReader(new FileReader("stock.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    double itemAmount = Double.parseDouble(parts[1].trim());

                    switch (itemName) {
                        case "Water":
                            dvm.setWater((int) itemAmount);
                            break;
                        case "Milk":
                            dvm.setMilk((int) itemAmount);
                            break;
                        case "Coffee Beans":
                            dvm.setCoffeeBeans((int) itemAmount);
                            break;
                        case "Disposable Cups":
                            dvm.setDisposableCups((int) itemAmount);
                            break;
                        case "Money":
                            dvm.setMoneyCase(itemAmount);
                            break;
                        case "Tea":
                            dvm.setTea((int) itemAmount);
                            break;
                        case "Sugar":
                            dvm.setSugar((int) itemAmount);
                            break;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void showSupply(DrinkVendingMachine dvm){
        System.out.println("Makinenin Stok Durumu" +
                "\n-----------------------");
        System.out.println(dvm.getWater() + "mL su");
        System.out.println(dvm.getMilk() + "mL süt");
        System.out.println(dvm.getCoffeeBeans() + "gr kahve");
        System.out.println(dvm.getDisposableCups() + " tane bardak");
        System.out.println(dvm.getMoneyCase() + " TL");
        System.out.println(dvm.getTea()+"gr çay");
        System.out.println(dvm.getSugar()+"gr şeker");
    }
    public void buy(DrinkVendingMachine dvm){
        System.out.print("\nNe almak istersiniz: ");
        String inputValue = scanner.nextLine();
        double userBalanceBefore = user.getUserBalance();

        switch(inputValue){
            case "1" :
                double latteMoney = Latte.getMoney();
                String resultMessage = checkWhatIsNotEnoughCoffee(dvm, new Latte());
                if (userBalanceBefore >= latteMoney && !resultMessage.startsWith("Yeter")) {
                    System.out.println(resultMessage);
                    user.setUserBalance(userBalanceBefore - latteMoney);
                    dvm.setMoneyCase(dvm.getMoneyCase() + latteMoney);
                    user.fileUpdate();
                } else {
                    System.out.println("Yetersiz Bakiye veya malzeme");
                }
                break;
            case "2" :
                double espressoMoney = Espresso.getMoney();
                resultMessage = checkWhatIsNotEnoughCoffee(dvm, new Espresso());
                if (userBalanceBefore >= espressoMoney && !resultMessage.startsWith("Yeter")) {
                    System.out.println(resultMessage);
                    user.setUserBalance(userBalanceBefore - espressoMoney);
                    dvm.setMoneyCase(dvm.getMoneyCase() + espressoMoney);
                    user.fileUpdate();
                } else {
                    System.out.println("Yetersiz Bakiye veya malzeme");
                }
                break;
            case "3" :
                double cappMoney = Cappuccino.getMoney();
                resultMessage = checkWhatIsNotEnoughCoffee(dvm, new Cappuccino());
                if (userBalanceBefore >= cappMoney && !resultMessage.startsWith("Yeter")) {
                    System.out.println(resultMessage);
                    user.setUserBalance(userBalanceBefore - cappMoney);
                    dvm.setMoneyCase(dvm.getMoneyCase() + cappMoney);
                    user.fileUpdate();
                } else {
                    System.out.println("Yetersiz Bakiye veya malzeme");
                }
                break;
            case "4":
                double sweetMoney = SweetTea.getMoney();
                resultMessage = checkWhatIsNotEnoughTea(dvm, new SweetTea());
                if (userBalanceBefore >= sweetMoney && !resultMessage.startsWith("Yeter")) {
                    System.out.println(resultMessage);
                    user.setUserBalance(userBalanceBefore - sweetMoney);
                    dvm.setMoneyCase(dvm.getMoneyCase()+sweetMoney);
                    user.fileUpdate();
                }
                else {
                    System.out.println("Yetersiz Bakiye veya malzeme");
                }
                break;
            case "5":
                double unsweetMoney = UnsweetenedTea.getMoney();
                resultMessage = checkWhatIsNotEnoughTea(dvm, new UnsweetenedTea());
                if (userBalanceBefore >= unsweetMoney && !resultMessage.startsWith("Yeter")) {
                    System.out.println(resultMessage);
                    user.setUserBalance(userBalanceBefore - unsweetMoney);
                    dvm.setMoneyCase(dvm.getMoneyCase()+unsweetMoney);
                    user.fileUpdate();
                }
                else {
                    System.out.println("Yetersiz Bakiye veya malzeme");
                }
                break;
            case "6":
                double teaLatteMoney = TeaLatte.getMoney();
                resultMessage = checkWhatIsNotEnoughTea(dvm, new TeaLatte());
                if (userBalanceBefore >= teaLatteMoney && !resultMessage.startsWith("Yeter")){
                    System.out.println(resultMessage);
                    user.setUserBalance(userBalanceBefore - teaLatteMoney);
                    dvm.setMoneyCase(dvm.getMoneyCase()+teaLatteMoney);
                    user.fileUpdate();

                }
                else {
                    System.out.println("Yetersiz Bakiye");
                }
                break;
            case "7":
                break;
        }
    }


    public String checkWhatIsNotEnoughCoffee(DrinkVendingMachine dvm, Coffee coffees){
        if(dvm.getWater() < coffees.getWater()){
            return "Yeterli su yok, lütfen ekleme yapın";
        }else if(dvm.getMilk() < coffees.getMilk()){
            return "Yeterli süt yok, lütfen ekleme yapın";
        }else if(dvm.getCoffeeBeans()<coffees.getCoffeeBeans()){
            return "Yeterli kahve yok, lütfen ekleme yapın";
        }else if(dvm.getDisposableCups() < 1){
            return "Yeterli bardak yok, lütfen ekleme yapın";
        }else {
            dvm.setWater(dvm.getWater() - coffees.getWater());
            dvm.setMilk(dvm.getMilk() - coffees.getMilk());
            dvm.setCoffeeBeans(dvm.getCoffeeBeans() - coffees.getCoffeeBeans());
            dvm.setDisposableCups(dvm.getDisposableCups() - 1);
            return "  (\n" +
                    "   ) )\n" +
                    " ........\n" +
                    " |      |\n" +
                    " \\      /     \n" +
                    "  `----'\nAfiyet Olsun...";
        }
    }

    public String checkWhatIsNotEnoughTea(DrinkVendingMachine dvm, Tea tea){
        if(dvm.getWater() < tea.getWater()){
            return "Yeterli su yok, lütfen ekleme yapın";
        }else if(dvm.getMilk() < tea.getMilk()){
            return "Yeterli süt yok, lütfen ekleme yapın";
        }else if(dvm.getTea()<tea.getTea()){
            return "Yeterli çay yok, lütfen ekleme yapın";
        }else if(dvm.getDisposableCups() < 1){
            return "Yeterli bardak yok, lütfen ekleme yapın";
        }else if (dvm.getSugar() < tea.getSugar()) {
            return "Yeterli şeker yok, lütfen ekleme yapın";
        } else {
            dvm.setWater(dvm.getWater() - tea.getWater());
            dvm.setMilk(dvm.getMilk() - tea.getMilk());
            dvm.setTea(dvm.getTea() - tea.getTea());
            dvm.setDisposableCups(dvm.getDisposableCups() - 1);
            return "  (\n" +
                    "   ) )\n" +
                    " ........\n" +
                    " |      |\n" +
                    " \\      /     \n" +
                    "  `----'\nAfiyet Olsun...";
        }
    }
    public void fill(DrinkVendingMachine dvm){
        System.out.print("Eklemek istediğiniz su miktarını yazın(mL): ");
        dvm.setWater(scanner.nextInt() + dvm.getWater());
        System.out.print("Eklemek istediğiniz süt miktarrını yazın(mL): ");
        dvm.setMilk(scanner.nextInt() +  dvm.getMilk());
        System.out.print("Eklemek istediğiniz çay miktarını yazın(gr): ");
        dvm.setTea(scanner.nextInt() + dvm.getTea());
        System.out.print("Eklemek istediğiniz kahve miktarını yazın(gr): ");
        dvm.setCoffeeBeans(scanner.nextInt() + dvm.getCoffeeBeans());
        System.out.print("Eklemek istediğiniz şeker miktarını yazın(gr): ");
        dvm.setSugar(scanner.nextInt() + dvm.getSugar());
        System.out.print("Eklemek istediğiniz bardak miktarını yazın: ");
        dvm.setDisposableCups(scanner.nextInt() + dvm.getDisposableCups());
    }

    @Override
    public void listDrinks(DrinkVendingMachine dvm) {
        System.out.println("İçecek Listesi" +
                "\n-----------------------");
        System.out.println("""
                1-Latte
                2-Espresso
                3-Cappuccino
                4-Çay
                5-Çay (Şekersiz)
                6-Sütlü Çay
                7-Geri Dön
                """);
    }
}
