package com.dvm;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

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

        boolean exit = false;
        int inputValue;
        while(!exit){
            System.out.println("""
                    1-Giriş Yap
                    2-Üye Ol
                    3-Çıkış Yap
                    Lütfen İşlem Seçiniz""");
            inputValue = scanner.nextInt();

            //TODO
            /* admin ozellikleri:
                anlik okuma(onceki dosya bilgileri geliyor)
                ---------------------------------
                try-catch uygun hata kontrolu
                ---------------------------------
                menunun duzgunlesitirilmesi
                final kavrami icin uygun yer bulunmasi

            * */

            // kullanici ve kullanici giris eklenecek
            /*kullanici:
                userId ve pass ile dogrulana
                bakiye sisteminin kullanici icinde olmasi
                kullanici degerleri dosyadan okunacak

            */

            switch (inputValue){
                case 1:
                    //giris yapma metodu calisacak, kullanici islemleri menusune yonlendirecek
                    user.login();
                    break;
                case 2:
                    //uye ol metodu calisacak
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
                    System.out.println("Gecerli bir deger giriniz.");
            }
        }
    }
}
