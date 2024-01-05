package com.dvm;

import java.io.*;
import java.util.*;


public class User implements UserOperations {
    protected static final String USER_FILE_PATH = "userinfo.txt";

    static DrinkVendingMachine dvm = new DrinkVendingMachine(3000, 1000, 1000, 24,  1000, 1000);
    static Scanner scanner = new Scanner(System.in);
    private static User instance;
    private String userID;
    private String userPass;
    private String userType;
    private double userBalance;


    ArrayList<String> userinfo = new ArrayList<>();

    File file = new File("userinfo.txt");

    private User() {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Dosya oluşturulurken bir hata oluştu.", e);
            }
        }
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    public double getUserBalance() {
        return userBalance;
    }
    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }


    @Override
    public void signUp() {

        System.out.print("Kullanıcı adı: ");
        Scanner newUserIDInput = new Scanner(System.in);
        userID = newUserIDInput.nextLine();

        System.out.print("Parola: ");
        Scanner newUserPassInput = new Scanner(System.in);
        userPass = newUserPassInput.nextLine();

        userType = "user";

        try (FileWriter fstream = new FileWriter(USER_FILE_PATH, true);
            BufferedWriter output = new BufferedWriter(fstream)){

            output.write(userID + "\t" + userPass + "\t" + userType + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void initValues() {
        fileToArray();
        for (int i = 0; i < userinfo.size(); i++) {
            String[] userInfoArray = userinfo.get(i).split("\t");
            if (userInfoArray.length >= 4 && userInfoArray[0].equals(getUserID())) {
                setUserBalance(Double.parseDouble(userInfoArray[3]));
                break;
            }
        }
    }


    @Override
    public void login() {

        System.out.print("Kullanıcı adınızı giriniz: ");
        String loginUserID = scanner.next();
        setUserID(loginUserID);

        System.out.print("Parolanızı giriniz: ");
        String loginUserPass = scanner.next();
        setUserPass(loginUserPass);

        boolean userControl = false;
        initValues();

        for (String user : userinfo) {
            String[] userInfoArray = user.split("\t");
            if (userInfoArray.length >= 2 && userInfoArray[0].equals(userID) && userInfoArray[1].equals(userPass)) {
                setUserType(userInfoArray[2]);
                userControl = true;
                break;
            }
        }

        if (userControl) {
            System.out.println("Merhaba " + loginUserID);
            System.out.println("-------------------------");

            boolean exit = false;
            String choice;
            dvm.loadStockFromFile(dvm);

            while (!exit) {
                System.out.print("""
                        1-İçecek Listesi
                        2-Malzeme Ekle
                        3-Malzeme Durumunu Göster
                        4-Bakiye İşlemleri
                        5-Geri Dön
                        """);

                choice = scanner.next();

                switch (choice) {
                    case "1":
                        dvm.listDrinks(dvm);
                        System.out.println("İçecek almak ister misiniz? (evet, hayır)");
                        String userInput = scanner.next();
                        if (userInput.equalsIgnoreCase("evet")){
                            dvm.buy(dvm);
                            dvm.saveStockToFile();

                        }
                        else if (userInput.equalsIgnoreCase("hayır")){
                            break;
                        }
                        else{
                            System.out.println("Geçerli bir değer giriniz.");
                        }
                        break;
                    case "2":
                        dvm.fill(dvm);
                        break;
                    case "3":
                        dvm.showSupply(dvm);
                        break;
                    case "4":
                        balance();
                        break;
                    case "5":
                        exit = true;
                        break;
                }
            }
        } else {
            System.out.println("Kullanıcı bulunamadı.");
        }
    }


    void fileToArray() {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH));
            while ((line = reader.readLine()) != null) {
                userinfo.add(line);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void balance() {
        boolean exit = false;

        while (!exit) {
            System.out.print("""
                    1-Sorgulama
                    2-Yükleme
                    3-Geri Dön
                    """);
            String choice = scanner.next();
            switch (choice) {
                case "1":
                    System.out.print("Güncel bakiye: " + this.userBalance+"\n");
                    for (int i = 0; i < userinfo.size(); i++) {
                        String[] userInfoArray = userinfo.get(i).split("\t");
                        if (userInfoArray.length >= 3 && userInfoArray[0].equals(getUserID())) {
                            userInfoArray[3] = String.valueOf(this.userBalance);
                            break;
                        }
                    }
                    break;
                case "2":
                    try {
                        System.out.print("Yüklemek istediğiniz miktarı giriniz: ");
                        double depositAmount = scanner.nextDouble();

                        if (getUserType().equalsIgnoreCase("admin")) {
                            System.out.print("Sahibin para yüklemesine gerek yok.\n");
                        }
                        else {
                            if (depositAmount >= 0) {
                                userBalance = this.getUserBalance() + depositAmount;
                                for (int i = 0; i < userinfo.size(); i++) {
                                    String[] userInfoArray = userinfo.get(i).split("\t");
                                    if (userInfoArray.length >= 3 && userInfoArray[0].equals(getUserID())) {
                                        userInfoArray[3] = String.valueOf(userBalance);
                                        fileUpdate();
                                        break;
                                    }
                                }

                            } else {
                                System.out.println("Girilen değer negatif olamaz.");
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Geçerli bir değer giriniz.");
                    }
                case "3":
                    exit = true;
                    break;

                default:
                    System.out.println("Geçersiz seçenek.");
                    break;
            }
        }
    }


    @Override
    public void fileUpdate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH));
             PrintWriter writer = new PrintWriter(new FileWriter("temp.txt"))){
            File tempFile = new File("temp.txt");

            String line;


            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith(userID)) {
                    String[] userInfoArray = line.split("\t");
                    userInfoArray[3] = String.valueOf(this.userBalance);
                    line = String.join("\t", userInfoArray);
                }
                writer.println(line);
                writer.flush();
            }

            writer.close();
            reader.close();

            if (file.delete()) {
                if (tempFile.renameTo(file)) {
                    System.out.println("Kullanıcı Bakiyesi: "+getUserBalance());
                } else {
                    System.err.println("Dosya adı değiştirilirken bir hata oluştu.");
                }
            } else {
                System.err.println("Eski dosya bulunamadı.");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}