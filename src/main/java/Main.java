import database.Database;
import database.Seeder;
import helpers.Text;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Database database = new Database();
    static Connection connection = database.getConnection();
    private static String[] homeOptions = {
            "Add new book",
            "Display available books",
            "Search a book",
            "Borrow a book",
            "Return a book",
            "Display borrowed books",
            "Delete a book",
            "Update a book",
            "View Statics"
            };
    public static Scanner console = new Scanner(System.in);
    public static void main(String[] args) {
        Seeder.seedUsers(20);
        Seeder.seedAuthors(20);
        Seeder.seedBooks(40);
        Seeder.seedBorrowings(80);

        displayOptions(homeOptions);
        System.out.println("chose an operation");
        int choice = console.nextInt();
        runOperation(choice);
    }

    public static void runOperation(int operationNumber){

    }

    public static void displayOptions(String[] options){
        int length= options.length;
        for (int i=0 ;i< length;i++){
            System.out.println("________________________________________");
            int number = i+1;
            String option = "|     "+ Text.BOLD+Text.GREEN +number+"-  " +Text.GREEN +Text.BOLD + Text.RESET +options[i];
            int count = 100 - 70 - options[i].length();
            for (int j=0;j<count;j++){
                option += " ";
            }
            option += "|";
            System.out.println(option);
        }
        System.out.println("________________________________________");
    }
}
