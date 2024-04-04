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
                    return rs.getString("salt");
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


    public boolean isUsernameUnique(String username) {
        boolean usernameIsUnique = true;
        String sqlQuery = "SELECT COUNT(*) FROM User WHERE username = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(2, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getInt(1) != 0) {
                            usernameIsUnique = false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernameIsUnique;
    }


    /// This method assumes that the person who checked it already verified that the username is unique !!!
    public boolean createUser(String username, String password, String salt) {
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
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
