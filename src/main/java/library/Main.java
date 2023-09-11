package library;

import Management.Manager;
import Models.BorrowedBook;
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
            "View Statics",
            "exit"
            };

    public static void main(String[] args) {
          /*Seeder.seedUsers(250);
          Seeder.seedAuthors(250);
          Seeder.seedBooks(250);
          Seeder.seedBorrowings(10000);*/
        BorrowedBook.checkLost();
        beginProcess();
    }

    public static void beginProcess(){
        Displayer.displayOptions(mainOptions);
        System.out.println("choose an operation : ");
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
            case 4:
                Manager.borrowBook();
                break;
            case 5:
                Manager.returnBook();
                break;
            case 6:
                Manager.borrowedBooks();
                break;
            case 7:
                Manager.deleteBook();
                break;
            case 8:
                Manager.updateBook();
                break;
            case 9:
                Manager.displayStatics();
                break;
            case 10:
                break;
            default:
                System.out.println("please choose an existing option");
                beginProcess();
                break;
        }
    }


}
