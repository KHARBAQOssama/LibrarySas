package Models;

import Management.Manager;
import database.Database;
import helpers.Displayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

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
    public void setALost(int lost){
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
    public Book[] availableBooks(){
        return null;
    }
    @Override
    protected  Book[] getAll() {
        return null;
    }

    @Override
    protected Book get() {
        Book book = new Book();
        String sqlQuery = "SELECT * FROM books WHERE ISBN + ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.ISBN);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                book.setTitle(resultSet.getString("title"));
                book.setQuantity(resultSet.getInt("quantity"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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
                System.out.println("This ISBN is owned by another book, correct your informations :");
                Manager.addBook();
                break;
            case "not match":
                this.save();
                break;
        }
        Displayer.lastChoices();
    }

    protected String matchedISBN(){

        String sqlQuery = "SELECT * FROM books WHERE ISBN = ?";
        Book book = executeGetBookQuery(sqlQuery);
        if (Objects.equals(book.getAuthor().getName(), this.author.getName()) && Objects.equals(book.getTitle(), this.title)){
            return "match";
        }else if(Objects.equals(book.getISBN(), this.ISBN)){
            return "ISBN match";
        }else{
            return "not match";
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
    protected Book update() {
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
                Author author = new Author();
                author.setId(resultSet.getInt("author_id"));
                author.get();
                book.setAuthor(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }
}
