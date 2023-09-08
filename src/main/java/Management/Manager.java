package Management;

import Models.Author;
import Models.Book;
import Models.BorrowedBook;
import Models.User;
import helpers.DataFaker;
import helpers.Displayer;
import helpers.Validator;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Date;

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
    private static  String[] memberInfo = {
            "Enter Your name",
            "Enter your phone number",
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
    public static void borrowBook(){
        Book book = getBook();

        if (book.getAvailable() == 0){
            System.out.println("this book is not available for the moment come back tomorrow");
            Displayer.lastChoices();
        }

        System.out.println("Are you an old member (yes/no)?");
        User member = getMember();

        BorrowedBook borrowedBook = borrowBook(book,member);
        System.out.println("DONE == "+member.getName()+ " remember that "+member.getMembership_number()+ " is your Membership Number");
        System.out.println("Info == you have borrowed the book with the title"+book.getTitle()+ " But you have to return it before "+borrowedBook.getEnd_date());

        Displayer.lastChoices();
    }
    public static Book getBook(){
        System.out.println("Enter the ISBN of the book you want to borrow");
        Book book = new Book();
        do {
            book.setISBN(Validator.getValidString());
            book = book.get();
            if (book.getTitle() == null){
                System.out.println("there is no book with this isbn,\nPlease Correct your Isbn :");
            }
        }while (book.getTitle() == null);
        return book;
    }
    public static User getMember(){
        User member = new User();
        String isMember = Validator.getValidString();
        if (Objects.equals(isMember.toLowerCase(), "yes")){
            member = getExistingMember();
        }else if(Objects.equals(isMember.toLowerCase(), "no")){
            member = createMember();
        }


        return member;
    }
    public static User getExistingMember(){
        User existingMember = new User();
        System.out.println("Enter your membership number :");
        do {

            existingMember.setMembership_number(Validator.getValidInteger("positive"));
            existingMember = existingMember.get();
            if (existingMember.getId() == 0){
                System.out.println("there is no member with this membership number !! \nPlease enter a valid membership number:");
            }
        }while (existingMember.getId() == 0);

        return existingMember;
    }
    public static User createMember(){
        User newMember = new User();
        do {
            System.out.println(memberInfo[0]);
            newMember.setName(Validator.getValidString());
        }while(Objects.equals(newMember.getName(), "") || newMember.getName() == null);
        do {
            System.out.println(memberInfo[1]);
            newMember.setPhone(Validator.getValidString());
        }while(Objects.equals(newMember.getPhone(), "") || newMember.getPhone() == null);

        int newMemberShipNumber = DataFaker.faker.number().numberBetween(11111111, 2147483600);
        newMember.setMembership_number(newMemberShipNumber);
        newMember.create();
        newMember = newMember.get();

        return newMember;
    }
    public static BorrowedBook borrowBook(Book book,User member){
        Date start_date = new Date();
        Date end_Date = new Date();
        BorrowedBook borrowedBook = new BorrowedBook(book,member,start_date,end_Date);
        borrowedBook.create();
        borrowedBook = borrowedBook.get();
        return  borrowedBook;
    }
    public static void returnBook(){
        User borrower = getExistingMember();
        Book book = getBook();
        BorrowedBook borrowedBook = borrowBook(book,borrower);
        borrowedBook.returnBook();
        System.out.println("you have returned the book successfully ");
        Displayer.lastChoices();
    }
}
