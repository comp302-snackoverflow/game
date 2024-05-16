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
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

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
    public boolean saveGame(FireBall fireball, Lance lance, List<Barrier> barriers) {
        FireballData fireballData = getFireballData(fireball);
        LanceData lanceData = getLanceData(lance);
        List<BarrierData> barrierData = barriers.stream().map(this::getBarrierData).toList();
        GameData data = new GameData(fireballData, lanceData, barrierData, 0.0);

        return dbHandler.saveGame("test", data);
    }

    // TODO Save with the actual username
    public boolean saveMap(List<Barrier> barriers) {
        List<BarrierData> barrierData = barriers.stream().map(this::getBarrierData).toList();
        return dbHandler.saveMap("test", barrierData);
    }

    private FireballData getFireballData(FireBall fireball) {
        fireball.adjustPositionAndSpeed(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), 1, 1);
        double x = fireball.getXPosition();
        double y = fireball.getYPosition();
        double dx = fireball.getDx();
        double dy = fireball.getDy();
        // Retrieve the old size in case the user resumes the game
        fireball.adjustPositionAndSpeed(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return new FireballData(x, y, dx, dy);
    }

    private LanceData getLanceData(Lance lance) {
        lance.adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), 1, 1);
        double x = lance.getXPosition();
        double y = lance.getYPosition();
        double angle = lance.getRotationAngle();
        lance.adjustPositionAndSize(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return new LanceData(x, y, angle);
    }

    private BarrierData getBarrierData(Barrier barrier) {
        barrier.adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), 1, 1);
        double x = barrier.getXPosition();
        double y = barrier.getYPosition();
        int health = barrier.getHealth();
        String type = barrier.getClass().getSimpleName();
        int barrierID = dbHandler.getBarrierIdFromType(type);
        barrier.adjustPositionAndSize(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return new BarrierData(x, y, health, barrierID);
    }
}
