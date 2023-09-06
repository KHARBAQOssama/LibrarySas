package Management;

import Models.Author;
import Models.Book;
import Models.Model;
import database.Database;
import helpers.Displayer;
import library.Main;

import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Manager {
    static Database database = new Database();
    static Connection connection = database.getConnection();
    public static Scanner console = new Scanner(System.in);
    private static String[] bookInfo = {
            "The book's title",
            "The book's ISBN",
            "The quantity",
            "The author's name",
    };
    public static void addBook(){
        Book book = new Book();
        Author author = new Author();
        System.out.println("Enter "+bookInfo[0]);
        book.setTitle(console.nextLine());
        System.out.println("Enter "+bookInfo[1]);
        book.setISBN(console.nextLine());
        System.out.println("Enter "+bookInfo[2]);
        book.setQuantity(console.nextInt());
        System.out.println("Enter "+bookInfo[3]);
        console.nextLine();
        author.setName(console.nextLine());

        author = author.createIfNotExist();
        book.setAuthor(author);
        book.create();

        System.out.println("Book has been added successfully");
        Displayer.lastChoices();
    }
    public static void deleteBook(){
        Book book = new Book();
        System.out.println("Enter "+bookInfo[1]);
        book.setISBN(console.nextLine());
        book.delete();
    }
    public static void updateBook(){
        System.out.println("Enter "+bookInfo[0]);
    }



}
