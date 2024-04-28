package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.services.save.BarrierData;
import tr.edu.ku.comp302.domain.services.save.FireballData;
import tr.edu.ku.comp302.domain.services.save.LanceData;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


public class DatabaseHandler {
    private static DatabaseHandler instance;
    private final String DATABASE_URL;
    private final String USER;
    private final String PASSWORD;

    private static final Logger logger = LogManager.getLogger();

    private DatabaseHandler() {
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream("./game_artifact/src/main/resources/database.config")) {
            prop.load(fis);
        } catch (IOException e) {
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
        }

        return false;
    }

    public boolean saveMap(String username, List<BarrierData> barriers) {
        final String saveMap = "INSERT INTO Map (owner) VALUES (?);";
        int uid = getUIDFromUsername(username);
        if (uid == -1) {
            return false;
        }
        // insert the map
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(saveMap)) {
                ps.setInt(1, uid);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT LAST_INSERT_ID()")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return saveBarriers(barriers, rs.getInt(1), "map");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
        return false;
    }

    public boolean saveGame(String username, FireballData fireball, LanceData lance,
                            List<BarrierData> barriers, double score) {
        final String saveGame = "INSERT INTO Save " +
                "(player_ref, fireball_x, fireball_y, fireball_dx, fireball_dy, " +
                "lance_x, lance_y, lance_angle, score) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int uid = getUIDFromUsername(username);
        if (uid == -1) {
            return false;
        }
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(saveGame)) {
                ps.setInt(1, uid);
                ps.setDouble(2, fireball.x());
                ps.setDouble(3, fireball.y());
                ps.setDouble(4, fireball.dx());
                ps.setDouble(5, fireball.dy());
                ps.setDouble(6, lance.x());
                ps.setDouble(7, lance.y());
                ps.setDouble(8, lance.angle());
                ps.setDouble(9, score);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT LAST_INSERT_ID()")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return saveBarriers(barriers, rs.getInt(1), "save");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
        return false;
    }

    private boolean saveBarriers(List<BarrierData> barriers, int id, String to) {
        String saveBarrier = "INSERT INTO Barrier (x, y, health, type, save_ref, map_ref) VALUES (?, ?, ?, ?, ?, ?)";
        Integer saveRef = to.equals("save") ? id : null;
        Integer mapRef = to.equals("map") ? id : null;
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(saveBarrier)) {
                for (BarrierData barrier : barriers) {
                    ps.setDouble(1, barrier.x());
                    ps.setDouble(2, barrier.y());
                    ps.setInt(3, barrier.health());
                    ps.setInt(4, barrier.type());
                    if (saveRef == null && mapRef != null) {
                        ps.setNull(5, java.sql.Types.INTEGER);
                        ps.setInt(6, mapRef);
                    } else if (saveRef != null && mapRef == null) {
                        ps.setInt(5, saveRef);
                        ps.setNull(6, java.sql.Types.INTEGER);
                    }
                    ps.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }

    }

    public int getUIDFromUsername(String username) {
        final String query = "SELECT uid FROM Player WHERE username = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("uid");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return -1;
    }

    public int getBarrierFromName(String type) {
        final String query = "SELECT id FROM BarrierType WHERE name = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, type);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 1; // default to simple barrier
    }
}

