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
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;

import java.util.ArrayList;
import java.util.List;

public class LoadService {
    private static final Logger logger = LogManager.getLogger();
    private final DatabaseHandler dbHandler;
    private LoadService instance;

    private LoadService() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public LoadService getInstance() {
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

        double windowWidth = 1280;
        double windowHeight = 720;
        FireBall fb = loadFireBall(data.fireballData(), windowWidth, windowHeight);
        Lance lance = loadLance(data.lanceData(), windowWidth, windowHeight);
        List<Barrier> barriers = loadBarriers(data.barriersData(), windowWidth, windowHeight);
        // TODO: score must be loaded into LanceOfDestiny (probably),
        //  find a way to do it because its stupid to statically set it
        return new Level(lance, fb, barriers, new ArrayList<>());
    }

    private Lance loadLance(LanceData ld, double windowWidth, double windowHeight) {
        double x = ld.x() * windowWidth;
        double y = ld.y() * windowHeight;
        double angle = ld.angle();
        Lance lance = new Lance(x, y);
        lance.setRotationAngle(angle);
        return lance;
    }

    private FireBall loadFireBall(FireballData fd, double windowWidth, double windowHeight) {
        double x = fd.x() * windowWidth;
        double y = fd.y() * windowHeight;
        double dx = fd.dx() * windowWidth;
        double dy = fd.dy() * windowHeight;
        FireBall fb = new FireBall(x, y);
        fb.setDx(dx);
        fb.setDy(dy);
        return fb;
    }

    private Barrier loadBarrier(BarrierData bd, double windowWidth, double windowHeight) {
        double x = bd.x() * windowWidth;
        double y = bd.y() * windowHeight;
        int health = bd.health();
        int barrierId = bd.type();

        String type = dbHandler.getBarrierTypeFromId(barrierId);

        Barrier barrier = switch (type) {
            case SimpleBarrier.TYPE -> new SimpleBarrier(x, y);
            case FirmBarrier.TYPE -> new FirmBarrier(x, y);
            case ExplosiveBarrier.TYPE -> new ExplosiveBarrier(x, y);
            default -> new SimpleBarrier(x, y);
        };

        barrier.setHealth(health);

        return barrier;
    }

    private List<Barrier> loadBarriers(List<BarrierData> barrierData, double windowWidth, double windowHeight) {
        return barrierData.stream().map(
                bd -> loadBarrier(bd, windowWidth, windowHeight)
        ).toList();
    }
}
