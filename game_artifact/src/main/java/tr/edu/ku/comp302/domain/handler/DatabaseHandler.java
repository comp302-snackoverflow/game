package tr.edu.ku.comp302.domain.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {

    private static final String DATABASE_URL = "jdbc:mysql/34.165.13.125:3306/snackoverflow?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "comp302";
    private static final String PASSWORD = "comp302snackoverflow";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSaltByUsername(String username) {
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM User WHERE username = ?")) {
                ps.setString(2, username);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String salt = rs.getString("salt");
                    return salt;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateLogin(String username, String password) {
        boolean userExists = false;
        String sqlQuery = "SELECT * FROM User WHERE username = ? AND password = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(2, username);
                ps.setString(3, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userExists = true;
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userExists;
    }



}
