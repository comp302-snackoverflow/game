package tr.edu.ku.comp302.domain.handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseHandler {
    private static DatabaseHandler instance;
    private final String DATABASE_URL;
    private final String USER;
    private final String PASSWORD;
    private DatabaseHandler() {
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream("./game_artifact/src/main/resources/database.config")) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DATABASE_URL = prop.getProperty("url");
        USER = prop.getProperty("username");
        PASSWORD = prop.getProperty("password");
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSaltByUsername(String username) {
        final String query = "SELECT salt FROM Player WHERE username = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, username);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getString("salt");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateLogin(String username, String password) {
        final String query = "SELECT * FROM Player WHERE username = ? AND password = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isUsernameUnique(String username) {
        boolean usernameIsUnique = true;
        final String query = "SELECT * FROM Player WHERE username = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    return !rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    /// This method assumes that the person who checked it already verified that the username is unique !!!
    public boolean createUser(String username, String password, String salt) {
        final String query = "INSERT INTO Player (username, password, salt) VALUES (?, ?, ?)";

        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, salt);

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
