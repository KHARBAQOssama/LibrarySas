package Models;

import Management.Manager;
import database.Database;
import helpers.Displayer;

import java.sql.*;
import java.util.*;

public class Book extends Model{
    static Database database = new Database();
    static Connection connection = database.getConnection();
    private String title ;
    private String ISBN ;
    private int quantity;
    private int available ;
    private int lost ;
    private Author author;
    private boolean deleted;
    public  Book(){

    }
    public Book(String title,String ISBN, Author author){
        this.title = title;
        this.ISBN = ISBN;
        this.author = author;
    }
    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setAuthor(Author author){
        this.author = author;
    }
    public void setAvailable(int available){
        this.available = available;
    }
    public void setLost(int lost){
        this.lost = lost;
    }
    public Author getAuthor(){
        return this.author;
    }
    public String getTitle(){
        return this.title;
    }
    public String getISBN(){
        return this.ISBN;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public int getAvailable(){
        return this.available;
    }
    public int getLost(){
        return this.lost;
    }
    public Book[] availableBooks(){
        return null;
    }
    @Override
    protected  Book[] getAll() {
        return null;
    }
    public static List<Book> getBooks(String searchPhrase){
        List<Book> books=new ArrayList<Book>();

        String sqlQuery = "SELECT b.*, a.name AS author_name FROM books AS b JOIN authors AS a ON b.author_id = a.id";
        if (searchPhrase != null){
            sqlQuery += " WHERE (b.title LIKE ? OR a.name LIKE ?)";
        }
        sqlQuery += " AND b.deleted = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            if (searchPhrase != null) {
                String searchParam = "%" + searchPhrase + "%";
                preparedStatement.setString(1, searchParam);
                preparedStatement.setString(2, searchParam);
                preparedStatement.setBoolean(3, false);
            }else{
                preparedStatement.setBoolean(1, false);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Book book = new Book();
                Author author = new Author();
                book.ISBN = resultSet.getString("ISBN");
                book.title =resultSet.getString("title");
                book.quantity = resultSet.getInt("quantity");
                book.available = resultSet.getInt("available");
                book.lost = resultSet.getInt("lost");
                author.setName(resultSet.getString("author_name"));
                book.author = author;

                books.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    @Override
    public Book get() {
        Book book = new Book();
        String sqlQuery = "SELECT b.*, a.name AS author_name FROM books AS b JOIN authors AS a ON b.author_id = a.id WHERE b.ISBN = ? AND b.deleted = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.ISBN);
            preparedStatement.setBoolean(2,false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                book.setTitle(resultSet.getString("title"));
                book.setISBN(resultSet.getString("ISBN"));
                book.setQuantity(resultSet.getInt("quantity"));
                book.setAvailable(resultSet.getInt("available"));
                Author author =new Author();
                author.setName(resultSet.getString("author_name"));
                book.setAuthor(author);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    protected Book get(String ISBN){
        return null;
    }
    @Override
    public void create() {
        String checking = this.matchedISBN();
        switch (checking){
            case "match":

                this.increaseQuantity();
                System.out.println("This book already exist, and his quantity has been increased");
                break;
            case "ISBN match":
                System.out.println("This ISBN is owned by another book, correct your information :");
                Manager.addBook();
                break;
            case "create":
                this.save();
                System.out.println("Book has been added successfully");
                break;
        }

    }

    protected String matchedISBN(){

        String sqlQuery = "SELECT * FROM books WHERE ISBN = ?";
        Book book = executeGetBookQuery(sqlQuery);
        if (book.getQuantity() != 0 && Objects.equals(book.getAuthor().getName(), this.author.getName()) && Objects.equals(book.getTitle(), this.title)){
            return "match";
        }else if(book.getQuantity() != 0 && Objects.equals(book.getISBN(), this.ISBN)){
            return "ISBN match";
        }else{
            return "create";
        }

    }
    protected void increaseQuantity(){
        String sqlQuery = "UPDATE books SET quantity = quantity + ?,available = available + ? where ISBN = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,this.quantity);
            preparedStatement.setInt(2,this.quantity);
            preparedStatement.setString(3,this.ISBN);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected void save(){
        String sqlQuery = "INSERT INTO books (title,ISBN,quantity,author_id,available) Values (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.title);
            preparedStatement.setString(2,this.ISBN);
            preparedStatement.setInt(3,this.quantity);
            preparedStatement.setInt(4,this.author.getId());
            preparedStatement.setInt(5,this.quantity);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("something went wrong while creating the book");
            Displayer.lastChoices();
        }
    }
    @Override
    public Book update() {
        String sqlQuery = "UPDATE books SET ";
        if(this.title != null){
            sqlQuery += "title = ?";
        }
        if(this.quantity != 0){
            sqlQuery += ", quantity = ?";
            sqlQuery += ", available = ?";
        }
        if(this.author.getName() != null){
            sqlQuery += ", author_id = ?";
        }
            sqlQuery += " WHERE ISBN = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            int count = 1;
            if(this.title != null){
                preparedStatement.setString(count,this.title);
                count++;
            }
            if(this.quantity != 0){
                preparedStatement.setInt(count,this.quantity);
                preparedStatement.setInt(count+1,this.available);
                count= count+2;
            }
            if(this.author.getName() != null){
                preparedStatement.setInt(count,this.author.getId());
                count++;
            }
            preparedStatement.setString(count,this.ISBN);
            preparedStatement.executeUpdate();
            System.out.println("Book has been updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Displayer.lastChoices();
        return null;
    }

    @Override
    protected Book[] search(String value) {
        return null;
    }

    @Override
    public void delete(){
        String sqlQuery = "Update books SET deleted = 1 WHERE ISBN = ? AND deleted = 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.ISBN);
            preparedStatement.executeUpdate();
            System.out.println("Book has been deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        Displayer.lastChoices();
    }
    private Book executeGetBookQuery(String sqlQuery){
        Book book = new Book();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.ISBN);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                book.setTitle(resultSet.getString("title"));
                book.setISBN(resultSet.getString("ISBN"));
                book.setQuantity(resultSet.getInt("quantity"));
                Author author = new Author();
                author.setId(resultSet.getInt("author_id"));
                author.get();
                book.setAuthor(author);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return book;
    }
    public static  int getAvailableBooksCount(){
        String sqlQuery = "SELECT SUM(available) AS count FROM books WHERE deleted = 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            while(resultSet.next()){
                count = resultSet.getInt("count");
            }
            return count;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static  int getLostBooksCount(){
        String sqlQuery = "SELECT SUM(lost) AS count FROM books WHERE deleted = 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            while(resultSet.next()){
                count = resultSet.getInt("count");
            }
            return count;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
