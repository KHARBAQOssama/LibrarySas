package Models;


import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Model{
    static Database database = new Database();
    static Connection connection = database.getConnection();
    private int id;
    private String name;
    private int membership_number;
    private String phone;
    private boolean deleted;
    public User(){

    }
    public  User(String newName,String newPhone){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMembership_number() {
        return membership_number;
    }

    public void setMembership_number(int membership_number) {
        this.membership_number = membership_number;
    }

    @Override
    protected User[] getAll() {
        return null;
    }

    @Override
    public User get() {
        String sqlQuery = "SELECT * FROM users WHERE membership_number = ? AND deleted = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,this.membership_number);
            preparedStatement.setBoolean(2,false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                this.id = resultSet.getInt("id");
                this.name = resultSet.getString("name");
                this.phone = resultSet.getString("phone");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void create() {
        String sqlQuery = "INSERT INTO users (name, membership_number, phone) VALUES (?,?,?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.name);
            preparedStatement.setInt(2,this.membership_number);
            preparedStatement.setString(3,this.phone);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected User update() {
        return null;
    }

    @Override
    protected User[] search(String value) {
        return null;
    }

    @Override
    protected void delete() {

    }
}
