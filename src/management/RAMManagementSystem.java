/*
 * TODO: FIX DATE SAVING
 * 
 */

package management;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

import Factory.RAMFactory;
import models.RAMItem;

public class RAMManagementSystem {
    private static final String PRODUCTS = "src/info/RAMModules.dat";
    private Map<String, RAMItem> ramMap;
    private ArrayList<RAMItem> ramList;
    private static final Scanner sc = new Scanner(System.in);

    public RAMManagementSystem() {
        checkIfFileExist();
        ramList = Utils.loadFromFile(PRODUCTS);

        if (ramList.isEmpty()) {
            System.out.println("[+] File status: Empty");
            ramList = new ArrayList<>();
        }

        ramMap = new HashMap<>();

        for (RAMItem item : ramList) {
            ramMap.put(item.getCode(), item);
        }

        // Cap nhat cho RAMFactory
        RAMFactory.initializeTypeCounters(ramMap);
    }


    /*
     *  Function 1: Add a new item
     */
    public void addRAMItem() {
        String type, brand, bus, productionDate;
        int quantity;
        boolean continueAdding = true;

        while (continueAdding) {
            System.out.println("===========NEW RAM INFORMATION=============");
            while (true) {
                type = Utils.getString("[+] Enter RAM Type (DDR4, LPDDR4, DDR5, LPDDR5): ", "[!] Please not enter empty string!");
                type = type.toUpperCase();

                if (isValidRAMType(type)) break;
                else System.out.println("[!] Invalid RAM type. Please enter one of the following types: DDR4, LPDDR4, DDR5, LPDDR5.");
            }
            
            brand = Utils.getString("[+] Enter brand: ", "[!] Please not enter empty string!");
            bus = Utils.getString("[+] Enter RAM's bus: ", "[!] Please not enter empty string!");
            String[] busParts = bus.split("\\D"); 
            bus = String.format("%s", busParts[0]) + "Mhz"; // Fix bus string (3200 -> 3200Mhz, 3200mh -> 3200Mhz)

            quantity = Utils.getPositiveInt("[+] Enter quantity: ", "[!] Please enter positive integer");
            // Add production date input here
            productionDate = Utils.getDate("[+] Enter production date (mm/dd/yyyy or mm-dd-yyyy): ", "[!] Please not enter empty string or invalid date!");

            RAMItem item = RAMFactory.createRAMItem(type, bus, brand, quantity, productionDate, true);
            ramMap.put(item.getCode(), item);
            ramList.add(item);

            System.out.println("RAM item added with code: " + item.getCode());

            System.out.print("[+] Do you want to add another item? (y/n): ");
            continueAdding = sc.nextLine().equalsIgnoreCase("y");
        }
    }



    private boolean isValidRAMType(String type) {
        switch (type) {
            case "DDR4":
            case "LPDDR4":
            case "DDR5":
            case "LPDDR5":
                return true;
            default:
                return false;
        }
    }


    /*
     *  Function 2: Search SubMenu
     *  
     *  Search by Type
     *  Search by Bus speed
     *  Search by Brand
     */
    public void searchMenu() {
        boolean continueSearching = true;

        while (continueSearching) {
            int choice;
            System.out.println("===Search menu===");
            System.out.println("1. Search by Type");
            System.out.println("2. Search by Bus");
            System.out.println("3. Search by Brand");
            System.out.println("4. Return to Main Menu");
            System.out.println("===================");
            System.out.print("Choose an option: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e){
                System.out.println("[!] Invalid input. Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    searchByType();
                    break;
                case 2:
                    searchByBus();
                    break;
                case 3:
                    searchByBrand();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("[-] Invalid choice. Please try again.");
            }
        }

    }

    private void searchByType() {
        String type = Utils.getString("Enter RAM Type to search: ", "[!] Please not enter empty string or invalid code");
        List<RAMItem> results = searchItem(item -> item.getType().equalsIgnoreCase(type));
        // Display
        displaySearchResult(results, "Type");
        
    }

    private void searchByBus() {
        String bus = Utils.getString("Enter RAM Bus to search: ", "[!] Please not enter empty string or invalid code");
        // Remove non-numeric char
        String normalizeInputBus = bus.replace("\\D", "");
        List<RAMItem> results = searchItem(item -> {
            String normalizedItemBus = item.getBus().replaceAll("\\D", "");
            // Check if the normalized user input matches part of the normalized item bus speed
            return item.isActive() && normalizedItemBus.contains(normalizeInputBus);
        });

        displaySearchResult(results, "Bus Speed");
    }

    private void searchByBrand() {
        String brand = Utils.getString("Enter RAM Brand to search: ", "[!] Please not enter empty string or invalid code");
        List<RAMItem> results = searchItem(item -> item.getBrand().equalsIgnoreCase(brand));
        displaySearchResult(results, "Brand");
    }

    /*
     * Searches for active RAM items in the list that match a specific condition defined by the given predicate
     */
    private List<RAMItem> searchItem(Predicate<RAMItem> predicate) {
        // Create n emptya list to store the results
        List<RAMItem> results = new ArrayList<>();
        for (RAMItem item : ramList) {
            // Check if the item is active and if it satisfies the condition defined by the predicate
            if (item.isActive() && predicate.test(item)) {
                results.add(item);
            }
        }
        return results;
    }

    // Display search result
    private void displaySearchResult(List<RAMItem> results, String title) {
        if (results.isEmpty()) {
            System.out.println("No RAM item found for [" + title + "]");
        } else {
            System.out.println("RAM items matching [" + title + "]");
            results.forEach(System.out::println);
        }

        System.out.println("Enter to back to the search menu...");
        sc.nextLine();
    }

    /*
     *  Function 3: Update information
     * 
     */

    public void updateItemInformation() {
        String code = Utils.getString("[+] Enter code to update: ", "[!] Please not enter empty string");
        RAMItem item = ramMap.get(code);

        if (item == null || !item.isActive()) {
            System.out.println("RAM item not found or inactive.");
            return;
        }

        String newType, newBrand, newBus;
        int newQuantity;

        System.out.println("[!] Notice: If you dont want to change, just press enter.");
        while (true) {
            newType = Utils.updateString("[+] Enter new Type (DDR4, LPDDR4, DDR5, LPDDR5) : ", item.getType()).toUpperCase();
            if (isValidRAMType(newType)) break;
            else System.out.println("[!] Invalid RAM type. Please enter one of the following types: DDR4, LPDDR4, DDR5, LPDDR5.");
        }
        newBrand = Utils.updateString("[+] Enter new Brand: ", item.getBrand());
        newBus = Utils.updateString("[+] Enter new Bus Speed: ", item.getBus());
        String[] busParts = newBus.split("\\D"); 
        newBus = String.format("%s", busParts[0]) + "Mhz"; // Fix bus string (3200 -> 3200Mhz, 3200mh -> 3200Mhz)
        newQuantity = Utils.updatePositiveInt("[+] Enter new Quantity: ", item.getQuantity());

        item.setType(newType);
        item.setBrand(newBrand);
        item.setBus(newBus);
        item.setQuantity(newQuantity);

        System.out.println("Item updated successfully");
        System.out.print("Updated item: " + item);
        
    }

    /*
     * Fucntion 5: Delete item
     *  Mark item as active = false
     *  then remove?
     */

    public void deleteItem() {
        boolean continueDeleting = true;
        while (continueDeleting) {
            String code = Utils.getString("[+] Enter code to delete: ", "[!] Please not enter empty string or invalid code");
            RAMItem item = ramMap.get(code);
    
            if (item == null || !item.isActive()) {
                System.out.println("RAM item not found or inactive.");
                return;
            }
    
            System.out.print("Are you sure you want to delete this item? (yes/no): ");
            String confirm = sc.nextLine().trim();
            if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                item.setActive(false);
                ramMap.remove(code);
                ramList.remove(item);
                System.out.println("[+] Item marked as inactive");
            }
            continueDeleting = false;
        }
        
    }

    public void showAllItems() {
        // Order: type -> bus -> brand (Active)
        List<RAMItem> activeItems = new ArrayList<>();
        for (RAMItem item : ramList) {
            if (item.isActive()) activeItems.add(item);
        }
        sortItem(activeItems);
        activeItems.forEach(System.out::println);
    }

    private void sortItem(List<RAMItem> items) {
        items.sort(Comparator.comparing(RAMItem::getType)
            .thenComparing(Comparator.comparing(RAMItem::getBus))
            .thenComparing(Comparator.comparing(RAMItem::getBrand)));
    }

    public void saveData() {
        Utils.saveToFile(PRODUCTS, ramList);
    }

    public void checkIfFileExist() {
        File file = new File(PRODUCTS);
        File dir = file.getParentFile();

        // Ensure the directory exists
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("[+] Directories created.");
            } else {
                System.out.println("[-] Failed to create directories.");
                return; // Exit if directories cannot be created
            }
        }


        // check if file existed
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("[+] File created: " + file.getName());
                } else {
                    System.out.println("[+] File already exists.");
                }
            } catch(Exception e) {
                System.out.println("[-] An error occurred while creating the file.");
            }
        } else {
            System.out.println("[+] File already exists.");
        }
    }
}