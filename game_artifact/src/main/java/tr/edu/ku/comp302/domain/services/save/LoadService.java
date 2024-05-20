package tr.edu.ku.comp302.domain.services.save;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.DatabaseHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoadService {
    private static final Logger logger = LogManager.getLogger(LoadService.class);
    private static LoadService instance;
    private final DatabaseHandler dbHandler;

    private LoadService() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static LoadService getInstance() {
        if (instance == null) {
            instance = new LoadService();
        }
        return instance;
    }

    public Level loadGame(int saveId) {
        GameData data = dbHandler.loadGame(saveId);
        if (data == null) {
            return null;
        }

        FireBall fb = loadFireBall(data.fireballData());
        Lance lance = loadLance(data.lanceData());
        List<Barrier> barriers = loadBarriers(data.barriersData());

        return new Level(lance, fb, barriers, data.score());
    }

    public Level loadMap(int mapId) {
        /*
         * FIXME: this works but has at least two issues:
         *  - barrier sizes are not consistent with the window size
         *  - the lance and fireball are positioned absolutely
         *  but at least it works so yay
         */
        List<BarrierData> barriersData = dbHandler.loadBarriers(mapId, "map");
        if (barriersData == null) {
            return null;
        }

        Lance lance = new Lance(576, 600);

        FireBall fireball = new FireBall(632, 560);

        List<Barrier> barriers = barriersData.stream().map(this::createBarrier).collect(Collectors.toCollection(ArrayList::new));

        return new Level(lance, fireball, barriers);
    }


    private Barrier createBarrier(BarrierData bd) {
        double xPos = bd.x();
        double yPos = bd.y();
        int health = bd.health();
        String type = dbHandler.getBarrierTypeFromId(bd.type());
        Barrier barrier;
        if (type.equals(SimpleBarrier.class.getSimpleName())) {
            barrier = new SimpleBarrier(xPos, yPos);
        } else if (type.equals(FirmBarrier.class.getSimpleName())) {
            barrier = new FirmBarrier(xPos, yPos);
        } else if (type.equals(ExplosiveBarrier.class.getSimpleName())) {
            barrier = new ExplosiveBarrier(xPos, yPos);
        } else {
            barrier = new SimpleBarrier(xPos, yPos);
            logger.warn("Unknown barrier type: {}, creating a simple barrier.", type);
        }

        barrier.setHealth(health);
        barrier.adjustPositionAndSize(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return barrier;
    }


    private Lance loadLance(LanceData ld) {
        double x = ld.x();
        double y = ld.y();
        double angle = ld.angle();
        Lance lance = new Lance(x, y);
        lance.setRotationAngle(angle);
        lance.adjustPositionAndSize(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return lance;
    }

    private FireBall loadFireBall(FireballData fd) {
        double x = fd.x();
        double y = fd.y();
        double dx = fd.dx();
        double dy = fd.dy();
        FireBall fb = new FireBall(x, y);
        fb.setDx(dx);
        fb.setDy(dy);

        fb.adjustPositionAndSpeed(1, 1, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        return fb;
    }


    private List<Barrier> loadBarriers(List<BarrierData> barrierData) {
        return barrierData.stream().map(this::createBarrier).toList();
    }
}
