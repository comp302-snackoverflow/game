package tr.edu.ku.comp302.domain.services.save;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.handler.DatabaseHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.util.List;

public class SaveService {
    private static final Logger logger = LogManager.getLogger(SaveService.class);
    private static SaveService instance;
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


    public boolean saveGame(FireBall fireball, Lance lance, List<Barrier> barriers) {
        FireballData fireballData = getFireballData(fireball, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        LanceData lanceData = getLanceData(lance, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        List<BarrierData> barrierData = barriers.stream().map(barrier -> getBarrierData(barrier, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight())).toList();
        GameData data = new GameData(fireballData, lanceData, barrierData, 0.0);

        return dbHandler.saveGame(data);
    }

    public boolean saveMap(List<Barrier> barriers) {
        return saveMap(barriers, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
    }

    public boolean saveMap(List<Barrier> barriers, double width, double height) {
        List<BarrierData> barrierData = barriers.stream().map(barrier -> getBarrierData(barrier, width, height)).toList();
        return dbHandler.saveMap(barrierData);
    }

    private FireballData getFireballData(FireBall fireball, double width, double height) {
        fireball.adjustPositionAndSpeed(width, height, 1, 1);
        double x = fireball.getXPosition();
        double y = fireball.getYPosition();
        double dx = fireball.getDx();
        double dy = fireball.getDy();
        // Retrieve the old size in case the user resumes the game
        fireball.adjustPositionAndSpeed(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return new FireballData(x, y, dx, dy);
    }

    private LanceData getLanceData(Lance lance, double width, double height) {
        lance.adjustPositionAndSize(width, height, 1, 1);
        double x = lance.getXPosition();
        double y = lance.getYPosition();
        double angle = lance.getRotationAngle();
        lance.adjustPositionAndSize(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return new LanceData(x, y, angle);
    }

    private BarrierData getBarrierData(Barrier barrier, double width, double height) {
        barrier.adjustPositionAndSize(width, height, 1, 1);
        double x = barrier.getXPosition();
        double y = barrier.getYPosition();
        int health = barrier.getHealth();
        String type = barrier.getClass().getSimpleName();
        int barrierID = dbHandler.getBarrierIdFromType(type);
        barrier.adjustPositionAndSize(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return new BarrierData(x, y, health, barrierID);
    }
}
