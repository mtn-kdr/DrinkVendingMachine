package com.dvm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static com.dvm.User.dvm;


public class Admin implements AdminOperations{
    Scanner scanner = new Scanner(System.in);
    User user = User.getInstance();

    File file = new File("adminwallet.txt");
    FileWriter fstream;

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
        dvm.loadStockFromFile(dvm);

        System.out.print("Admin ID: ");
        String adminID = scanner.next();

        System.out.print("Admin Password: ");
        String adminPass = scanner.next();

        for (int i = 0; i < user.userinfo.size(); i++) {
            String[] userInfoArray = user.userinfo.get(i).split("\t");
            if (userInfoArray.length >= 3 && userInfoArray[0].equals(adminID) && userInfoArray[1].equals(adminPass) && userInfoArray[2].equalsIgnoreCase("admin")) {
                System.out.println("Merhaba " + adminID);
                admin = true;
                break;
            }
        }
    }


    public void adminMenu() {



        boolean exit = false;
        if (admin) {
            while (!exit) {
                System.out.print("""
                        1-Kasadaki parayı çek
                        2-Kullanıcı bilgilerini görüntüle/düzenle
                        3-Kullanıcı ekle
                        4-Geri çık
                        """);

                String choice = scanner.next();

                switch (choice) {
                    case "1":
                        withdrawMoney();
                        break;

                    case "2":
                        getUserInfos();
                        String cond = scanner.next();
                        if (cond.equalsIgnoreCase("geri"))
                        {
                            break;
                        }
                        else if(cond.equalsIgnoreCase("devam")){
                            updateUserInfo();
                        }
                        else {
                            System.out.println("Geçerli bir değer giriniz.");
                        }
                        break;

                    case "3":
                        adminAddUser();
                        break;
                    case "4":
                        exit = true;
                        break;
                }
            }

        } else {
            System.out.println("Hatalı veya eksik bilgi.");
        }
    }

    public void withdrawMoney(){
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();


            fstream = new FileWriter("adminwallet.txt", true);
            BufferedWriter output = new BufferedWriter(fstream);

            output.write(dtf.format(now)+" Tarihinde kasaya cüzdanına " + dvm.getMoneyCase() + " eklendi.\n");
            setAdminWallet(dvm.getMoneyCase() + getAdminWallet());
            dvm.setMoneyCase(0);

            System.out.println("Güncel sahip cüzdani bakiyesi: " + getAdminWallet());
            System.out.println("Güncel kasa bakiyesi:  " + dvm.getMoneyCase());

            output.close();
            fstream.close();

            dvm.saveStockToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void adminAddUser(){
        try  {
            fstream = new FileWriter(User.USER_FILE_PATH, true);
            BufferedWriter output = new BufferedWriter(fstream);


            boolean checkUsername = false;
            String newUsername = "";
            while (!checkUsername) {
                System.out.print("Kullanıcı adı: ");
                newUsername = scanner.next();
                if (newUsername.matches("[a-zA-Z0-9]{3,}")) {
                    checkUsername = true;
                } else if (!newUsername.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*")) {
                    System.out.println("Yalnızca harf ve rakam kullanın.");
                } else {
                    System.out.println("Kullanıcı adı en az 3 harf içermeli");
                }
            }


            boolean checkPassword = false;
            String newPass = "";

            while (!checkPassword) {
                System.out.print("Parola: ");
                newPass = scanner.next();
                if (newPass.matches("[a-zA-Z0-9]+")) {
                    checkPassword = true;
                } else {
                    System.out.println("Parola yalnızca harf ve rakam içermelidir.");
                }
            }

            String newUserType = "";
            boolean checkUserType = false;

            while (!checkUserType) {
                System.out.print("Kullanıcı tipi (admin/user): ");
                newUserType = scanner.next();
                if (newUserType.equalsIgnoreCase("admin") || newUserType.equalsIgnoreCase("user")) {
                    checkUserType = true;
                } else {
                    System.out.println("Kullanıcı tipi sadece 'admin' veya 'user' olabilir.");
                }
            }

            System.out.print("Kullanıcı bakiyesi: ");
            String newUserBalance = scanner.next();

            output.write(newUsername + "\t" + newPass + "\t" + newUserType + "\t" + newUserBalance + "\n");
            output.close();
            fstream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getUserInfos() {
        user.userinfo.clear();

        user.fileToArray();
        System.out.println("ID\tPASS\tTYPE\tBLNCE");
        for (String userinfos : user.userinfo){
            System.out.println(userinfos);
        }

        System.out.println("Devam etmek için 'devam' geri çıkmak için 'geri' yazınız.");

    }


    public void updateUserInfo() {
        System.out.print("İşlem yapılacak Kullanıcı adını giriniz: ");
        String selectedUser = scanner.next();

        System.out.print("""
                Islem Seciniz
                1-Kullanıcı bilgilerini degistir
                2-Kullanıcı sil
                3-Geri çık
                """);

        boolean exit = false;

        while (!exit) {

            String choice = scanner.next();

            switch (choice) {
                case "1":
                    try (BufferedReader reader = new BufferedReader(new FileReader(User.USER_FILE_PATH));
                         PrintWriter writer = new PrintWriter(new FileWriter("temp.txt"))) {

                        boolean checkUsername = false;
                        String username = "";

                        while (!checkUsername) {
                            System.out.print("Kullanıcı adı: ");
                            username = scanner.next();

                            if (username.matches("[a-zA-Z0-9]{3,}")) {
                                if (!username.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*")) {
                                    checkUsername = true;
                                } else {
                                    System.out.println("Kullanıcı adı sembol içeremez.");
                                }
                            } else {
                                System.out.println("Kullanıcı adı en az 3 harf içermeli ve sayı içermeli");
                            }
                        }


                        boolean checkPassword = false;
                        String pass = "";

                        while (!checkPassword) {
                            System.out.print("Parola: ");
                            pass = scanner.next();
                            if (pass.matches("[a-zA-Z0-9]+")) {
                                checkPassword = true;
                            } else {
                                System.out.println("Parola yalnızca harf ve rakam içermelidir.");
                            }
                        }


                        String userType = "";
                        boolean checkUserType = false;

                        while (!checkUserType) {
                            System.out.print("Kullanıcı tipi (admin/user): ");
                            userType = scanner.next();
                            if (userType.equalsIgnoreCase("admin") || userType.equalsIgnoreCase("user")) {
                                checkUserType = true;
                            } else {
                                System.out.println("Kullanıcı tipi sadece 'admin' veya 'user' olabilir.");
                            }
                        }


                        System.out.print("Kullanıcı bakiyesi: ");
                        String userBalance = scanner.next();

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
                                System.out.println("Kullanıcı bilgileri güncellendi.");
                                exit = true;
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

                        boolean userExists = false;

                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().startsWith(selectedUser.toLowerCase())){
                                writer.println(line + "\t");
                                writer.flush();
                            }
                            else {
                                userExists = true;
                            }

                        }
                        writer.close();
                        reader.close();

                        if (!userExists){
                            System.out.println("Kullanıcı bulunamadı.");
                            exit = true;
                            tempFile.delete();
                        }

                        else {
                            if (user.file.delete()) {
                                if (tempFile.renameTo(user.file)) {
                                    System.out.println("Kullanıcı sistemden silindi.");
                                    exit = true;
                                } else {
                                    System.err.println("Dosya adı değiştirilirken bir hata oluştu.");
                                }
                            } else {
                                System.err.println("Eski dosya bulunamadı.");
                            }
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