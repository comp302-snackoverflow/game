package tr.edu.ku.comp302.domain.services.save;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.DatabaseHandler;

import java.util.List;

public class SaveService {
    private static SaveService instance;
    private static final Logger logger = LogManager.getLogger();
    private final DatabaseHandler dbHandler;

    private SaveService() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static SaveService getInstance() {
        if (instance == null) {
            instance = new SaveService();
        }
        return instance;
    }

    // TODO Save with the actual username and game score
    public boolean saveGame(FireBall fireball, Lance lance,
                            List<Barrier> barriers, double windowWidth, double windowHeight) {
        FireballData fireballData = getFireballData(fireball, windowWidth, windowHeight);
        LanceData lanceData = getLanceData(lance, windowWidth, windowHeight);
        List<BarrierData> barrierData = barriers.stream().map(barrier -> getBarrierData(barrier, windowWidth, windowHeight)).toList();
        GameData data = new GameData(fireballData, lanceData, barrierData, 0.0);

        return dbHandler.saveGame("test", data);
    }

    // TODO Save with the actual username
    public boolean saveMap(List<Barrier> barriers, double windowWidth, double windowHeight) {
        List<BarrierData> barrierData = barriers.stream().map(
                barrier -> getBarrierData(barrier, windowWidth, windowHeight)
        ).toList();
        return dbHandler.saveMap("test", barrierData);
    }

    private FireballData getFireballData(FireBall fireball, double windowWidth, double windowHeight) {
        double x = fireball.getXPosition() / windowWidth;
        double y = fireball.getYPosition() / windowHeight;
        double dx = fireball.getDx() / windowWidth;
        double dy = fireball.getDy() / windowHeight;
        return new FireballData(x, y, dx, dy);
    }

    private LanceData getLanceData(Lance lance, double windowWidth, double windowHeight) {
        double x = lance.getXPosition() / windowWidth;
        double y = lance.getYPosition() / windowHeight;
        double angle = lance.getRotationAngle();
        return new LanceData(x, y, angle);
    }

    private BarrierData getBarrierData(Barrier barrier, double windowWidth, double windowHeight) {
        double x = barrier.getXPosition() / windowWidth;
        double y = barrier.getYPosition() / windowHeight;
        int health = barrier.getHealth();
        String type = switch (barrier) {
            case SimpleBarrier ignored -> SimpleBarrier.TYPE;
            case FirmBarrier ignored -> FirmBarrier.TYPE;
            case ExplosiveBarrier ignored -> ExplosiveBarrier.TYPE;
            default -> {
                logger.warn("Unknown barrier type: " + barrier.getClass().getName() + ". Saving as simple barrier.");
                yield SimpleBarrier.TYPE; // Barriers of unknown type will be saved as simple barriers
            }
        };
        int barrierID = dbHandler.getBarrierFromName(type);
        return new BarrierData(x, y, health, barrierID);
    }
}
