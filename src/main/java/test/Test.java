package test;

import database.Database;
import java.sql.*;

public class Test extends Database{
    public  static void main(String[] args){
        String query = "SELECT * FROM users";
        Database db = new Database();

        try {
            Connection connection = db.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String email = resultSet.getString("created_at");
                System.out.println(email);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeConnection();
        }
    }
}
