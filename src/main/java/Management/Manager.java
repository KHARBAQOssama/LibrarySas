package Management;

import Models.Author;
import Models.Book;
import Models.Model;
import database.Database;
import helpers.Displayer;
import helpers.Validator;
import library.Main;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Manager {
    public static Scanner console = new Scanner(System.in);
    private static String[] bookInfo = {
            "Enter The book's title",
            "Enter The book's ISBN",
            "Enter The quantity",
            "Enter The author's name",
    };
    private static String[] bookNewInfo = {
            "Enter New book's title",
            "Enter New quantity",
            "Enter New author's name",
    };
    public static void addBook(){
        Book book = new Book();
        Author author = new Author();
        System.out.println(bookInfo[0]);
        book.setTitle(Validator.getValidString());
        System.out.println(bookInfo[1]);
        book.setISBN(Validator.getValidString());
        System.out.println(bookInfo[2]);
        book.setQuantity(Validator.getValidInteger("positive"));
        System.out.println(bookInfo[3]);
        author.setName(Validator.getValidString());

        author = author.createIfNotExist();

        book.setAuthor(author);
        book.create();

        System.out.println("Book has been added successfully");
        Displayer.lastChoices();
    }
    public static void deleteBook(){
        Book book = new Book();
        System.out.println(bookInfo[1]);
        book.setISBN(console.nextLine());

        book = book.get();
        if (book.getTitle() == null){
            System.out.println("there is no book with this isbn");
            deleteBook();
        }else{
            book.delete();
        }

    }
    public static void updateBook(){
        System.out.println(bookInfo[1]);
        Book book = new Book();
        book.setISBN(Validator.getValidString());
        book = book.get();
        if (book.getTitle() == null){
            System.out.println("there is no book with this isbn");
            updateBook();
        }else{
            System.out.println("Book Info");
            Displayer.displayBook(book);
            book = getUpdatedInfo(book);

            book.update();
        }
    }

    public static Book getUpdatedInfo(Book book){
        System.out.println("Enter the new informations:");
        System.out.println("If you don't wanna update something, do not fill the field (press Enter to skip):");
        Author author = new Author();

        System.out.println(bookNewInfo[0]);
        String title = console.nextLine();
        if(!Objects.equals(title, "")){
            book.setTitle(title);
        }
        int quanity = book.getQuantity();
        System.out.println(bookNewInfo[1]);
        book.setQuantity(Validator.getValidInteger(null));
        if(book.getQuantity() != 0){
            int available = book.getAvailable();
            book.setAvailable(book.getAvailable() + (book.getQuantity() - quanity));
        }
        System.out.println(bookNewInfo[2]);
        String authorName = console.nextLine();

        if(!Objects.equals(authorName, "")){
            author.setName(authorName);
            author = author.createIfNotExist();
            book.setAuthor(author);
        }
        return book;
    }
    public static void displayBooks(){
        List<Book> books = Book.getBooks(null);
        Displayer.displayBooks(books);
    }
    public static void searchBooks(){
        System.out.println("You can search by the book title or the author name:");
        String searchPhrase = console.nextLine();
        List<Book> books = Book.getBooks(searchPhrase);
        Displayer.displayBooks(books);
    }



}
