package Models;

import database.Database;
import helpers.DateGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowedBook extends Model{
    static Database database = new Database();
    static Connection connection = database.getConnection();
    private int id;
    private Book book;
    private User user;
    private Date start_date;
    private Date end_date;
    private boolean returned;
    private boolean lost;

    public BorrowedBook(Book book ,User user,Date start_date){
        this.book = book;
        this.user = user;
        this.start_date =start_date;
    }
    public BorrowedBook(Book book ,User user,Date start_date,Date end_date){
        this.book = book;
        this.user = user;
        this.start_date =start_date;
        this.end_date = end_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public Date getStart_date() {
        return start_date;
    }

    @Override
    protected BorrowedBook[] getAll() {
        return null;
    }

    @Override
    public BorrowedBook get() {
        String sqlQuery = "SELECT * FROM borrowedbooks WHERE book_ISBN = ? AND user_id = ? AND returned = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.book.getISBN());
            preparedStatement.setInt(2,this.user.getId());
            preparedStatement.setBoolean(3,false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                this.id = resultSet.getInt("id");
                this.start_date = resultSet.getDate("start_date");
                this.end_date = resultSet.getDate("end_date");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void create() {
        String sqlQuery = "INSERT INTO borrowedBooks (book_ISBN, user_id, start_date, end_date) VALUES (?,?,?,?);";
        java.sql.Date startDate = new java.sql.Date(this.start_date.getTime());
        java.sql.Date endDate = new java.sql.Date(this.end_date.getTime());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, this.book.getISBN());
            preparedStatement.setInt(2,this.user.getId());
            preparedStatement.setDate(3,startDate);
            preparedStatement.setDate(4,endDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnBook(){
        String sqlQuery = "UPDATE borrowedBooks AS bb " +
                "JOIN books AS b " +
                "ON b.ISBN = bb.book_ISBN " +
                "SET bb.returned = ?, " +
                "bb.lost = CASE WHEN bb.lost = 1 THEN 0 ELSE bb.lost END, " +
                "b.lost = CASE WHEN bb.lost = 1 THEN b.lost - 1 ELSE b.lost END, " +
                "b.available = b.available + 1 " +
                "WHERE  bb.book_ISBN = ? " +
                "AND bb.user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setBoolean(1,true);
            preparedStatement.setString(2,this.book.getISBN());
            preparedStatement.setInt(3,this.user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @Override
    protected BorrowedBook update() {
        return null;
    }

    @Override
    protected BorrowedBook[] search(String value) {
        return null;
    }
    public static List<BorrowedBook> getBorrowedBooks(){
        List<BorrowedBook> borrowedBooks = new ArrayList<BorrowedBook>();
        String sqlQuery = "SELECT " +
                "bb.start_date , " +
                "b.* , " +
                "u.*, " +
                "a.name AS author " +
                "FROM borrowedbooks AS bb " +
                "JOIN books AS b " +
                "ON bb.book_ISBN = b.ISBN " +
                "JOIN users AS u " +
                "ON bb.user_id = u.id " +
                "JOIN authors as a " +
                "ON b.author_id = a.id " +
                "WHERE bb.returned = 0;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Author author = new Author(resultSet.getString("author"));
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getInt("membership_number")
                );
                Book book = new Book(
                        resultSet.getString("title"),
                        resultSet.getString("ISBN"),
                        author
                );
                BorrowedBook borrowedBook = new BorrowedBook(
                        book,
                        user,
                        resultSet.getDate("start_date")
                );
                borrowedBooks.add(borrowedBook);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowedBooks;
    }
    public static int getBorrowedBooksCount(){
        String sqlQuery = "SELECT COUNT(*) AS count FROM borrowedbooks WHERE returned = 0 AND lost = 0";
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
    @Override
    protected void delete() {

    }
    public static void checkLost(){
        String sqlQuery1 ="UPDATE borrowedBooks AS bb SET bb.lost = CASE WHEN bb.lost = 0 AND bb.returned = 0 AND bb.end_date < CURDATE() THEN 1 ELSE bb.lost END;";
        String sqlQuery2 = "UPDATE books AS b\n" +
                "SET b.lost = (\n" +
                "    SELECT COUNT(*)\n" +
                "    FROM borrowedBooks AS bb\n" +
                "    WHERE bb.book_ISBN = b.ISBN AND bb.lost = 1 AND bb.returned = 0\n" +
                ")\n" +
                "WHERE EXISTS (\n" +
                "    SELECT 1\n" +
                "    FROM borrowedBooks AS bb\n" +
                "    WHERE bb.book_ISBN = b.ISBN AND bb.lost = 1 AND bb.returned = 0\n" +
                ");";
        String sqlQuery3 = "UPDATE books AS b\n" +
                "SET b.lost = 0\n" +
                "WHERE NOT EXISTS (\n" +
                "    SELECT 1\n" +
                "    FROM borrowedBooks AS bb\n" +
                "    WHERE bb.book_ISBN = b.ISBN AND bb.lost = 1 AND bb.returned = 0\n" +
                ");";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery1);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(sqlQuery2);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(sqlQuery3);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
