package database;

import java.sql.*;
public class Database {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jobify";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
