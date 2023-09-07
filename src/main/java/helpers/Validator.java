package helpers;

import java.util.Scanner;

public class Validator {

    public static Scanner console = new Scanner(System.in);
    public static String getValidString() {
        String input;
        do {
            input = console.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Please enter a valid data.");
            }
        } while (input.isEmpty());

        return input;
    }



    public static int getValidInteger(String positive) {
        int number;

        do {
            String input = console.nextLine().trim();

            if (positive == null && input.isEmpty()) {
                return 0;
            }

            try {
                number = Integer.parseInt(input);

                if (positive != null && number <= 0) {
                    System.out.println("Please enter a positive number.");
                } else if (number <= 0) {
                    System.out.println("Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
                number = -1;
            }
        } while (number <= 0);

        return number;
    }

}
