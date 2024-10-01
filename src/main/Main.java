package main;

import management.*;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        RAMManagementSystem ramManagement = new RAMManagementSystem();
        Scanner sc = new Scanner(System.in);

        // String num = "3200Mhz";
        // String[] parts = num.split("\\D");
        // for (String string : parts) {
        //     System.out.println(string);
        // }


        while (true) {
            Utils.showMainMenu();
            int choice;
            System.out.print("[+] Please enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    ramManagement.addRAMItem();
                    break;
                case 2:
                    ramManagement.searchMenu();
                    break;
                case 3:
                    ramManagement.updateItemInformation();
                    break;
                case 4:
                    ramManagement.deleteItem();
                    break;
                case 5:
                    ramManagement.showAllItems();
                    break;
                case 6:
                    ramManagement.saveData();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("[!] Invalid choice.");
                    break;
            }
            
        }
    }
}
