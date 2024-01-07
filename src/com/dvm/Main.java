package com.dvm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    static DrinkVendingMachine dvm = new DrinkVendingMachine(3000, 1000, 1000, 24,  1000, 1000);

    public static void main(String[] argv) {
        System.out.print("""
                   ┌───────────────────────┐
                   │   _________________   │
                   │  |                 |  │
                   │  |   SMART CAFE    |  │
                   │  |                 |  │
                   │  |     [1] [2] [3] |  │
                   │  |     [4] [5] [6] |  │
                   │  |         [7]     |  │
                   │  |                 |  │
                   │  |  _____________  |  │
                   │  | |             | |  │
                   │  | |    []       | |  │
                   │  | |    []       | |  │
                   │  |_|_____________|_|  │
                   │   _________________   │
                   │  |                |   │
                   │  |  Hoş Geldiniz  |   │
                   │  |________________|   │
                   └───────────────────────┘
                """);
        Scanner scanner = new Scanner(System.in);

        User user = User.getInstance();
        Admin admin = new Admin();

        Path filePath = Paths.get("stock.txt");
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        boolean exit = false;
        int inputValue;


        while(!exit){
            System.out.println("""
                    1-Giriş Yap
                    2-Üye Ol
                    3-Çıkış Yap
                    Lütfen İşlem Seçiniz""");
            inputValue = scanner.nextInt();



            dvm.loadStockFromFile(dvm);
            switch (inputValue){
                case 1:
                    user.login();
                    break;
                case 2:
                    user.signUp();
                    break;
                case 3:
                    exit= true;
                    break;
                case 4:
                    admin.adminLogin();
                    admin.adminMenu();
                    break;
                default:
                    System.out.println("Geçerli bir değer giriniz.");
            }
        }
    }
}
