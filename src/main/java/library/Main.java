package library;

import Management.Manager;
import database.Database;
import database.Seeder;
import helpers.Displayer;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static Scanner console = new Scanner(System.in);
    static Database database = new Database();
    static Connection connection = database.getConnection();
    private static String[] mainOptions = {
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

    public static void main(String[] args) {
        // Seeder.seedUsers(20);
        // Seeder.seedAuthors(20);
        // Seeder.seedBooks(40);
        // Seeder.seedBorrowings(80);

        beginProcess();
    }

    public static void beginProcess(){
        Displayer.displayOptions(mainOptions);
        System.out.println("chose an operation : ");
        int choice = console.nextInt();
        runOperation(choice);
    }
    public static void runOperation(int operationNumber){
        switch (operationNumber){
            case 1:
                Manager.addBook();
                break;
            case 2:
                Manager.displayBooks();
                break;
            case 3:
                Manager.searchBooks();
                break;
            case 7:
                Manager.deleteBook();
                break;
            case 8:
                Manager.updateBook();
                break;
            default:
                System.out.println("no implementation yet");
                Displayer.lastChoices();
                break;
        }
    }


}
