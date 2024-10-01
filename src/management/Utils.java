/**
 *
 * @author isepipi
 */

package management;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.RAMItem;

public class Utils {
    private static final Scanner sc = new Scanner(System.in);
    

    private static String getInput(String prompt, Predicate<String> validator, String errorMessage) {
        String input;

        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();

            if (!validator.test(input)) {
                System.out.println(errorMessage);
            } else {
                return input;
            }
        } while (true);
    }

    public static String getString(String prompt, String errorMessage) {
        return getInput(prompt, s -> !s.isEmpty(), errorMessage);
    }

    public static int getPositiveInt(String prompt, String errorMessage) {
        return Integer.parseInt(getInput(prompt, Utils::checkPositive, errorMessage));
    }

    private static boolean checkPositive(String num) {
        try {
            return Integer.parseInt(num) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean getBoolean(String prompt, String errorMessage) {
        return Boolean.parseBoolean(getInput(prompt, s -> s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"), errorMessage));
    }

    // UPDATE
    public static String updateString(String prompt, String oldValue) {
        String input = getInput(prompt, s -> true, "[!] Invalid input");
        return !input.isEmpty() ? input : oldValue; 
    }

    public static int updatePositiveInt(String prompt, int oldValue) {
        String input = getInput(prompt, s -> true, "[!] Invalid input");
        try {
            if (input.isEmpty()) return oldValue;

            int newValue = Integer.parseInt(input);
            if (newValue > 0) return newValue;
            System.out.println("[!] Please enter a positive integer.");
            return oldValue;
        } catch(NumberFormatException e) {
            System.out.println("Failed to convert from <String> to <Integer>. Returning old value.");
            return oldValue;
        }
    }

    public static boolean updateBoolean(String prompt, boolean oldValue) {
        String input = getInput(prompt, s -> true, "[!] Invalid input");
        
        if (input.isEmpty()) return oldValue;
        if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(input);
        } else {
            System.out.println("[-] Unknown input");
            return oldValue;
        }
    }

    public static String getDate(String prompt, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();

            if (validateAndParseDate(input)) {
                return input;
            } else {
                System.out.println(errorMessage);
            }
        }
    }
    
    // Check if date is valid (correct format)
    private static boolean validateAndParseDate(String input) {
        String regex = "(\\d{1,2})[-/](\\d{1,2})[-/](\\d{4})";
        /*
         * Eplain pattern:
         * \\d{1,2} for the month and day (1 or 2 digits)
         * \\d{4} for the year (4 digits)
         * [-/] for various formats with either / or - as separators
         */
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            int month = Integer.parseInt(matcher.group(1));
            int day = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));

            if (!isValidDate(day, month, year)) {
                return false;
            }


            LocalDate inputDate;
            try {
                inputDate = LocalDate.of(year, month, day);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date: " + input);
                return false;
            }

            // Current
            LocalDate currentDate = LocalDate.now();

            // Compares date
            if (inputDate.isBefore(currentDate) || inputDate.isEqual(currentDate)) {
                System.out.printf("Valid date: %02d/%02d/%d%n", month, day, year);
                return true;
            } else {
                System.out.println("[-] Please enter date before or equal to " + currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                return false;
            }
        }
        return false;
    }

    private static boolean isValidDate(int day, int month, int year) {
        switch (month) {
            case 4: case 6: case 9: case 11:
                return day <= 30;
            case 2:
                if (isLeapYear(year)) {
                    return day <= 29;
                } else {
                    return day <= 28;
                }
            default:
                return day <= 31;
        }
    }

    private static boolean isLeapYear(int year) {
        return ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
    }

    // Read & write
    public static void saveToFile(String filename, ArrayList<RAMItem> list) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(list);
            System.out.println("[+] Write file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(oos);
            closeStream(fos);
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<RAMItem> loadFromFile(String filename) {
        ArrayList<RAMItem> ramList = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            File file = new File(filename);
            if (file.length() == 0) {
                System.out.println("[+] File is empty. Returning an empty list.");
                return ramList;
            }

            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
            ramList = (ArrayList<RAMItem>) ois.readObject();
            System.out.println("[+] Data loaded from directory: " + filename);
        } catch (FileNotFoundException e) {
            // Create a file if not exist
            try {
                File file = new File(filename);
                file.createNewFile();
                System.out.println("[+] File created: " + filename);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeStream(ois);
            closeStream(fis);
        }

        System.out.println("[+] Data loaded from directory: " + filename);
        return ramList;
    }

    // Close stream
    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showMainMenu() {
        System.out.println("> ========= < RAM Management System > ========= <");
        System.out.println("1. Add RAM Item");
        System.out.println("2. Search RAM Item");
        System.out.println("3. Update RAM Item");
        System.out.println("4. Delete Item");
        System.out.println("5. Show All Items");
        System.out.println("6. Save Data To File");
        System.out.println("0. Exit");
    }

    public static boolean isBackMenu() {
        System.out.print("Do you want to back to Main Menu (y/n): ");
        String input = sc.nextLine().trim();
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
    }
}
