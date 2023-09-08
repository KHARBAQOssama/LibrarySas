package Models;

import database.Database;
import helpers.DateGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

    public BorrowedBook(Book book ,User user,Date start_date,Date end_date){
        this.book = book;
        this.user = user;
        this.start_date =start_date;
        this.end_date = end_date;
    }

    public Date getEnd_date() {
        return end_date;
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
        String sqlQuery = "UPDATE borrowedBooks SET returned = ?,lost = ? WHERE book_ISBN = ? AND user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setBoolean(1,true);
            preparedStatement.setBoolean(2,true);
            preparedStatement.setString(3,this.book.getISBN());
            preparedStatement.setInt(4,this.user.getId());
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

    @Override
    protected void delete() {

    }
}
