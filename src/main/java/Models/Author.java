package Models;

import Management.Manager;
import database.Database;
import helpers.Displayer;

import java.sql.*;

public class Author extends Model {
    static Database database = new Database();
    static Connection connection = database.getConnection();
    private int id;
    private String name;
    private boolean deleted;

    public void setName(String name){
        this.name = name;
    }
    public void setDeleted(boolean deleted){
        this.deleted = deleted;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
    public boolean getDeleted(){
        return this.deleted;
    }
    @Override
    protected Author[] getAll() {
        return null;
    }

    @Override
    protected Author get() {
        Author author = new Author();
        String sqlQuery = "SELECT * FROM authors WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,this.id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                this.name = resultSet.getString("name");
                this.deleted = resultSet.getBoolean("deleted");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while getting the author");
            Displayer.lastChoices();
        }
        return author;
    }

    @Override
    protected void create() {
        String sqlQuery = "INSERT INTO authors (name) values (?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,this.name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("something went wrong while creating the author");
            Displayer.lastChoices();
        }
    }

    @Override
    protected Author update() {
        return null;
    }

    @Override
    protected Author[] search(String value) {
        return null;
    }

    @Override
    protected void delete() {

    }
    protected static Author get(String name){
        Author author = new Author();
        String sqlQuery = "SELECT * FROM authors WHERE name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
                author.setDeleted(resultSet.getBoolean("deleted"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while getting the author");
            Displayer.lastChoices();
        }
        return author;
    }

    public Author createIfNotExist(){
        Author existAuthor = get(this.name);
        if(existAuthor.getId() == 0) {
            this.create();
            existAuthor = get(this.name);
        }
        return  existAuthor;
    }
}
