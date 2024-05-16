package tr.edu.ku.comp302.domain.handler;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.services.save.BarrierData;
import tr.edu.ku.comp302.domain.services.save.FireballData;
import tr.edu.ku.comp302.domain.services.save.GameData;
import tr.edu.ku.comp302.domain.services.save.LanceData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static DatabaseHandler instance;
    private final HikariDataSource dataSource;
    private final LoadingCache<Integer, String> barrierTypeCache;
    private final LoadingCache<String, Integer> barrierIdCache;
    private final Logger logger = LogManager.getLogger(DatabaseHandler.class);

    private DatabaseHandler() {
        barrierTypeCache =
            Caffeine.newBuilder()
                    .maximumSize(128)
                    .weakValues()
                    .build(this::getBarrierType);
        barrierIdCache =
            Caffeine.newBuilder()
                    .maximumSize(128)
                    .weakValues()
                    .build(this::getBarrierFromType);

        HikariConfig config = new HikariConfig("/hikari.properties");
        dataSource = new HikariDataSource(config);
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e);
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
            logger.error(e);
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
            logger.error(e);
        }
        return false;
    }

    public boolean isUsernameUnique(String username) {
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
            logger.error(e);
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
            logger.error(e);
        }

        return false;
    }

    public boolean saveMap(String username, List<BarrierData> barriers) {
        final String saveMap = "INSERT INTO Map (owner) VALUES (?);";
        int uid = getUidFromUsername(username);
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
            logger.error(e);
            return false;
        }
        return false;
    }

    /**
     * Load barriers from the database from a save or a map
     *
     * @param id   save_ref or map_ref
     * @param type "save" or "map" depending on what you want to load
     * @return A list containing the barriers in the save or map
     */
    public List<BarrierData> loadBarriers(int id, String type) {
        final String query = switch (type) {
            case "save" -> "SELECT * FROM Barrier WHERE save_ref = ? AND map_ref IS NULL";
            case "map" -> "SELECT * FROM Barrier WHERE map_ref = ? AND save_ref IS NULL";
            default -> null;
        };

        if (query == null) {
            throw new IllegalArgumentException("Invalid type: " + type + ". Type must be one of \"save\" or \"map\"");
        }

        List<BarrierData> barriers = new ArrayList<>();

        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        double x = rs.getDouble("x");
                        double y = rs.getDouble("y");
                        int health = rs.getInt("health");
                        int barrierType = rs.getInt("type");
                        barriers.add(new BarrierData(x, y, health, barrierType));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
        return barriers;
    }

    public boolean saveGame(String username, GameData data) {
        FireballData fireball = data.fireballData();
        LanceData lance = data.lanceData();
        List<BarrierData> barriers = data.barriersData();
        double score = data.score();

        final String saveGame = "INSERT INTO Save " +
                "(player_ref, fireball_x, fireball_y, fireball_dx, fireball_dy, " +
                "lance_x, lance_y, lance_angle, score) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int uid = getUidFromUsername(username);
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
            logger.error(e);
            return false;
        }
        return false;
    }

    public GameData loadGame(int saveId) {
        String query = "SELECT * FROM Save WHERE id = ?";
        FireballData fireball = null;
        LanceData lance = null;
        double score = 0.0;
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, saveId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        fireball = new FireballData(rs.getDouble("fireball_x"),
                                rs.getDouble("fireball_y"),
                                rs.getDouble("fireball_dx"),
                                rs.getDouble("fireball_dy"));
                        lance = new LanceData(rs.getDouble("lance_x"),
                                rs.getDouble("lance_y"),
                                rs.getDouble("lance_angle"));
                        score = rs.getDouble("score");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }

        List<BarrierData> barriers = loadBarriers(saveId, "save");
        return new GameData(fireball, lance, barriers, score);
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
                    } else if (saveRef != null && mapRef == null) {  // I know mapRef check is redundant
                        ps.setInt(5, saveRef);          // but it is for clarity
                        ps.setNull(6, java.sql.Types.INTEGER);
                    }
                    ps.addBatch();
                }
                int[] numUpdates = ps.executeBatch();
                for (int i = 0; i < numUpdates.length; i++) {
                    if (numUpdates[i] == -2) {
                        logger.debug("Execution {}: unknown number of rows updated", i);
                    } else {
                        logger.debug("Execution {} successful: {} rows updated", i, numUpdates[i]);
                    }
                }

                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }

    public int getUidFromUsername(String username) {
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
            logger.error(e);
        }
        return -1;
    }

    private int getBarrierFromType(String type) {
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
            logger.error(e);
        }
        return 1; // default to simple barrier
    }

    private String getBarrierType(int id) {
        final String query = "SELECT name FROM BarrierType WHERE id = ?";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return SimpleBarrier.class.getSimpleName(); // default to simple barrier
    }

    public String getBarrierTypeFromId(int id) {
        return barrierTypeCache.get(id);
    }

    public int getBarrierIdFromType(String type) {
        return barrierIdCache.get(type);
    }

}
