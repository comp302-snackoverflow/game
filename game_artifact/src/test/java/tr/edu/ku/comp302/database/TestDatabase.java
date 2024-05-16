package tr.edu.ku.comp302.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tr.edu.ku.comp302.domain.handler.DatabaseHandler;
import tr.edu.ku.comp302.domain.services.save.BarrierData;
import tr.edu.ku.comp302.domain.services.save.FireballData;
import tr.edu.ku.comp302.domain.services.save.GameData;
import tr.edu.ku.comp302.domain.services.save.LanceData;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TestDatabase {
    private DatabaseHandler dbHandler;
    private SecureRandom random;

    @BeforeEach
    public void setUp() {
        dbHandler = DatabaseHandler.getInstance();
        random = new SecureRandom();
    }

    @Test
    public void TestConnection() {
        dbHandler = DatabaseHandler.getInstance();
        Connection conn = dbHandler.getConnection();
        Assertions.assertNotNull(conn);
    }

    @Test
    public void testInstancesEqual() {
        DatabaseHandler dbHandler2 = DatabaseHandler.getInstance();
        DatabaseHandler dbHandler3 = DatabaseHandler.getInstance();
        Assertions.assertEquals(dbHandler2, dbHandler3);
        Assertions.assertEquals(dbHandler, dbHandler2);
    }

    @Test
    public void testIsUsernameUnique() {
        for (int i = 0; i < 10; i++) {
            _testIsUsernameUnique();
        }
    }

    @Test
    public void testSaveLoadMapConsistency() {
        for (int i = 0; i < 4; i++) {
            _testSaveLoadMapConsistency();
        }
    }

    @Test
    public void testSaveLoadGameConsistency() {
        for (int i = 0; i < 4; i++) {
            _testSaveLoadGameConsistency();
        }
    }

    private void _testIsUsernameUnique() {
        String username = randomUsername();
        Assertions.assertTrue(dbHandler.isUsernameUnique(username));

        Assertions.assertTrue(dbHandler.createUser(username, "", ""));

        Assertions.assertFalse(dbHandler.isUsernameUnique(username));
    }


    private void _testSaveLoadGameConsistency() {
        String username = randomUsername();
        dbHandler.createUser(username, "", "");

        var barriers = generateBarriers();
        LanceData lance = new LanceData(random.nextDouble(), random.nextDouble(), random.nextDouble(-45, 45));
        FireballData fireball = new FireballData(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
        GameData data = new GameData(fireball, lance, barriers, random.nextDouble(1000));
        boolean result = dbHandler.saveGame(username, data);
        Assertions.assertTrue(result);

        try (PreparedStatement ps = dbHandler.getConnection().prepareStatement("SELECT MAX(id) FROM Save, Player WHERE Save.player_ref = Player.uid AND Player.username = ?")) {
            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) {
                int saveId = rs.getInt(1);
                var dbData = dbHandler.loadGame(saveId);
                Assertions.assertEquals(data.lanceData(), dbData.lanceData());
                Assertions.assertEquals(data.fireballData(), dbData.fireballData());
                Assertions.assertEquals(data.score(), dbData.score());
                Assertions.assertEquals(data.barriersData(), dbData.barriersData());
            } else {
                Assertions.fail("Failed to retrieve the game from the database.");
            }
        } catch (SQLException e) {
            Assertions.fail("Failed to retrieve the game from the database.");
        }

    }


    private void _testSaveLoadMapConsistency() {
        String username = randomUsername();
        dbHandler.createUser(username, "", "");

        var barriers = generateBarriers();

        boolean result = dbHandler.saveMap(username, barriers);
        Assertions.assertTrue(result);

        try (PreparedStatement ps = dbHandler.getConnection().prepareStatement("SELECT MAX(id) FROM Map, Player WHERE Map.owner = Player.uid AND Player.username = ?")) {
            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) {
                int mapId = rs.getInt(1);
                var dbBarriers = dbHandler.loadBarriers(mapId, "map");
                Assertions.assertEquals(barriers.size(), dbBarriers.size());
                Assertions.assertEquals(barriers, dbBarriers);
            } else {
                Assertions.fail("Failed to retrieve the map from the database.");
            }
        } catch (SQLException e) {
            Assertions.fail("Failed to retrieve the map from the database.");
        }
    }

    private String randomUsername() {
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append((char) (random.nextInt(26) + 'a'));
        }

        return sb.toString();
    }

    private List<BarrierData> generateBarriers() {
        var barriers = new ArrayList<BarrierData>();
        for (int i = 0; i < 15; i++) {
            int type = i % 3 + 1;
            int healths = type == 1 ? random.nextInt(2, 5) : 1;
            barriers.add(new BarrierData(random.nextDouble(), random.nextDouble(), healths, type));
        }
        return barriers;
    }
}
