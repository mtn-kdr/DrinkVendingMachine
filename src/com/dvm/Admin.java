package com.dvm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.util.Scanner;

import static com.dvm.User.dvm;


public class Admin implements AdminOperations{
    Scanner scanner = new Scanner(System.in);
    User user = User.getInstance();

    private double adminWallet;

    public double getAdminWallet() {
        return adminWallet;
    }

    public void setAdminWallet(double adminWallet) {
        this.adminWallet = adminWallet;
    }

    boolean admin = false;

    public void adminLogin() {
        user.initValues();

        System.out.print("Admin ID: ");
        String adminID = scanner.nextLine();

        System.out.print("Admin Password: ");
        String adminPass = scanner.nextLine();

        for (int i = 0; i < user.userinfo.size(); i++) {
            String[] userInfoArray = user.userinfo.get(i).split("\t");
            if (userInfoArray.length >= 3 && userInfoArray[0].equals(adminID) && userInfoArray[1].equals(adminPass) && userInfoArray[2].equalsIgnoreCase("admin")) {
                System.out.print("Merhaba " + adminID);
                admin = true;
                break;
            }
        }
    }

    public void adminMenu() {

        File file = new File("adminwallet.txt");
        FileWriter fstream;

        boolean exit = false;
        if (admin) {
            while (!exit) {
                System.out.print("""
                        \n1-Kasadaki parayi cek
                        2-Kullanici Bilgilerini Goruntule/Duzenle
                        3-Kullanici ekle
                        """);

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        try {
                            setAdminWallet(dvm.getMoneyCase());
                            fstream = new FileWriter("adminwallet.txt", true);
                            BufferedWriter output = new BufferedWriter(fstream);
                            output.write("Sahibin cuzdanina " + dvm.getMoneyCase() + " eklendi.\n");

                            dvm.setMoneyCase(0);
                            setAdminWallet(dvm.getMoneyCase() + getAdminWallet());

                            System.out.print("Guncel sahip cuzdani bakiyesi: \n" + getAdminWallet());
                            System.out.print("Guncel kasa bakiyesi:  " + dvm.getMoneyCase());

                            output.close();
                            fstream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "2":
                        getUserInfos();
                        updateUserInfo();
                        break;

                    case "3":
                        try  {
                            fstream = new FileWriter(User.USER_FILE_PATH, true);
                            BufferedWriter output = new BufferedWriter(fstream);

                            System.out.print("Kullanici adi: ");
                            String newUsername = scanner.nextLine();

                            System.out.print("Parola: ");
                            String newPass = scanner.nextLine();

                            System.out.print("Kullanici tipi: ");
                            String newUserType = scanner.nextLine();

                            System.out.print("Kullanici bakiyesi: ");
                            String newUserBalance = scanner.nextLine();

                            output.write(newUsername + "\t" + newPass + "\t" + newUserType + "\t" + newUserBalance + "\n");
                            output.close();
                            fstream.close();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            }

        } else {
            System.out.println("Hatali veya eksik bilgi.");
        }
    }

    public void getUserInfos() {
        System.out.println("username\tpassword\tuser type\tbalance");
        for (String user : user.userinfo) {
            System.out.println(user);
        }
    }

    public void updateUserInfo() {
        System.out.print("Islem yapilacak Kullanici adini giriniz: ");
        String selectedUser = scanner.next();

        System.out.println("""
                Islem Seciniz
                1-Kullanici bilgilerini degistir
                2-Kullanici sil
                3-exit
                """);

        boolean exit = false;

        while (!exit) {

            String choice = scanner.next();

            switch (choice) {
                case "1":
                    try (BufferedReader reader = new BufferedReader(new FileReader(User.USER_FILE_PATH));
                         PrintWriter writer = new PrintWriter(new FileWriter("temp.txt"))) {

                        System.out.print("Kullanici adi: ");
                        String username = scanner.nextLine();

                        System.out.print("Parola: ");
                        String pass = scanner.nextLine();

                        System.out.print("Kullanici tipi: ");
                        String userType = scanner.nextLine();

                        System.out.print("Kullanici bakiyesi: ");
                        String userBalance = scanner.nextLine();

                        File tempFile = new File("temp.txt");

                        String line;

                        while ((line = reader.readLine()) != null) {
                            if (line.trim().startsWith(selectedUser)) {
                                String[] userInfoArray = line.split("\t");
                                userInfoArray[0] = username;
                                userInfoArray[1] = pass;
                                userInfoArray[2] = userType;
                                userInfoArray[3] = userBalance;
                                line = String.join("\t", userInfoArray);
                            }
                            writer.println(line);
                            writer.flush();
                        }

                        writer.close();
                        reader.close();

                        if (user.file.delete()) {
                            if (tempFile.renameTo(user.file)) {

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
                    break;

                case "2":
                    try (BufferedReader reader = new BufferedReader(new FileReader(User.USER_FILE_PATH));
                         PrintWriter writer = new PrintWriter(new FileWriter("temp.txt"))) {

                        File tempFile = new File("temp.txt");
                        String line;

                        while ((line = reader.readLine()) != null) {
                                if (!line.trim().startsWith(selectedUser)){
                                    writer.println(line + "\t");
                                    writer.flush();
                            }
                        }
                        writer.close();
                        reader.close();

                        if (user.file.delete()) {
                            if (tempFile.renameTo(user.file)) {

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
                    break;
                case "3":
                    exit = true;
                    break;
            }
        }
    }
}
    /*try (BufferedReader reader = new BufferedReader(new FileReader(User.USER_FILE_PATH));
                         PrintWriter writer = new PrintWriter(new FileWriter("temp.txt"))){
                        File tempFile = new File("temp.txt");

                        String line;


                        while ((line = reader.readLine()) != null){
                            if (!line.trim().startsWith(selectedUser)){
                                writer.println(line + "\t");
                                writer.flush();
                            }
                        }
                        writer.close();
                        reader.close();


                        if (tempFile.exists()) {
                            if (tempFile.renameTo(user.file)) {
                                System.out.println("Dosya adı başarıyla değiştirildi.");
                            } else {
                                System.err.println("Dosya adı değiştirilirken bir hata oluştu.");
                            }
                        } else {
                            System.err.println("Eski dosya bulunamadı.");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }*/