package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gwent";
    private static final String USER = "gwentUser";
    private static final String PASSWORD = "alikh53";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();
        String query = "SELECT username FROM Users";  // Adjust table and column names as per your database schema

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
    }
}
