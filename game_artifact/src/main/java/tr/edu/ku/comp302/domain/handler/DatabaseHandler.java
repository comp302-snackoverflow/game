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
    public static Connection getConnection() {
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream("./src/main/java/database/database.config")) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String DATABASE_URL = prop.getProperty("database.URI");
        String USER = prop.getProperty("database.username");
        String PASSWORD = prop.getProperty("database.password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSaltByUsername(String username) {
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM User WHERE username = ?")) {
                ps.setString(2, username);

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

    public static boolean validateLogin(String username, String password) {
        String sqlQuery = "SELECT * FROM User WHERE username = ? AND password = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(2, username);
                ps.setString(3, password);

                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isUsernameUnique(String username) {
        boolean usernameIsUnique = true;
        String sqlQuery = "SELECT COUNT(*) FROM User WHERE username = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(2, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getInt(1) != 0) {
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    /// This method assumes that the person who checked it already verified that the username is unique !!!
    public static boolean createUser(String username, String password, String salt) {
        String sqlQuery = "INSERT INTO User (username, password, salt) VALUES (?,?,?)";

        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setString(4, salt);

                if (ps.executeUpdate() > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
