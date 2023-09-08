package database;

import helpers.DataFaker;
import helpers.DateGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Seeder {
    static Database database = new Database();
    static Connection connection = database.getConnection();

    public static void seedUsers(int number){
        String sqlQuery = "INSERT INTO users (name, phone, membership_number) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            for (int i = 0; i < number; i++) {
                preparedStatement.setString(1, DataFaker.faker.name().fullName());
                preparedStatement.setString(2, DataFaker.faker.numerify("#############"));
                preparedStatement.setInt(3, DataFaker.faker.number().numberBetween(11111111, 2147483600));

                // Execute the insert statement
                preparedStatement.executeUpdate();
            }

            // Close the statement and connection
            preparedStatement.close();
            //database.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void seedAuthors(int number){
        String sqlQuery = "INSERT INTO authors (name) VALUES (?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            for (int i = 0; i < number; i++) {
                preparedStatement.setString(1, DataFaker.faker.name().fullName());
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            //database.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void seedBooks(int number){
        String sqlQuery = "INSERT INTO books (title, ISBN, quantity, available, author_id) VALUES (?,?,?,?,?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            Random random = new Random();
            for (int i = 0; i < number; i++) {
                String ISBN = DataFaker.faker.numerify("###-#-#####-#");
                if(getISBNs().contains(ISBN)){
                    ISBN = DataFaker.faker.numerify("###-#-#####-#");
                }
                List<Integer> authorIds = getIds("authors");
                int quantity = DataFaker.faker.number().numberBetween(10,1000);
                int randomIndex = random.nextInt(authorIds.size());
                preparedStatement.setString(1, DataFaker.faker.book().title());
                preparedStatement.setString(2,ISBN);
                preparedStatement.setInt(3,quantity);
                preparedStatement.setInt(4,quantity);
                preparedStatement.setInt(5,authorIds.get(randomIndex));
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            //database.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static  void seedBorrowings(int number){
        String sqlQuery = "INSERT INTO borrowedBooks (book_ISBN, user_id, start_date, end_date) VALUES (?,?,?,?);";
        List<String> bookISBNs = getISBNs();
        List<Integer> userIds = getIds("users");
        int booksLength = bookISBNs.size();
        int authorsLength = userIds.size();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            Random random = new Random();
            Date date = new Date();
            for (int i = 0; i < number; i++) {

                int randomAuthorIndex = random.nextInt(authorsLength);
                int randomBookIndex = random.nextInt(booksLength);
                java.sql.Date startDate = new java.sql.Date(date.getTime());
                java.sql.Date endDate = new java.sql.Date(DateGenerator.afterDaysFromNow(random.nextInt(30)).getTime());
                preparedStatement.setString(1, bookISBNs.get(randomBookIndex));
                preparedStatement.setInt(2,userIds.get(randomAuthorIndex));
                preparedStatement.setDate(3,startDate);
                preparedStatement.setDate(4,endDate);
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            //database.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> getIds(String table){
        List<Integer> ids = new ArrayList<>();
        String sqlQuery = "SELECT id FROM "+table +" WHERE deleted = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setBoolean(1,false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ids.add(resultSet.getInt("id"));
            }
            preparedStatement.close();
            //database.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }
    public static List<String> getISBNs(){
        List<String> ISBNs = new ArrayList<>();
        String sqlQuery = "SELECT ISBN FROM books WHERE deleted = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setBoolean(1,false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ISBNs.add(resultSet.getString("ISBN"));
            }
            preparedStatement.close();
            //database.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ISBNs;
    }
}
