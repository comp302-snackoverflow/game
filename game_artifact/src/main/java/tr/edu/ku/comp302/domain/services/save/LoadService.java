package tr.edu.ku.comp302.domain.services.save;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.App;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.DatabaseHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;
import tr.edu.ku.comp302.ui.view.BarrierView;

import java.util.List;

public class LoadService {
    private static LoadService instance;
    private static final Logger logger = LogManager.getLogger();
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
      // FIXME LevelPanel needs MainFrame. Return a GameData instead
    public LevelPanel loadGame(int saveId, MainFrame mainFrame) {
        GameData data = dbHandler.loadGame(saveId);
        if (data == null) {
            return null;
        }

        double windowWidth = 1280;
        double windowHeight = 720;
        FireBall fb = loadFireBall(data.fireballData(), windowWidth, windowHeight);
        Lance lance = loadLance(data.lanceData(), windowWidth, windowHeight);
        List<Barrier> barriers = loadBarriers(data.barriersData(), windowWidth, windowHeight);
        FireBallView fbv = new FireBallView(fb);
        LanceView lv = new LanceView(lance);
        List<BarrierView> barrierViews = barriers.stream().map(BarrierView::new).toList();
        // TODO: score must be loaded into LanceOfDestiny (probably),
        //  find a way to do it because its stupid to statically set it
        return new LevelPanel(new Level(), lv, fbv, barrierViews, mainFrame);
    }
    public LevelPanel loadMap(int mapId, MainFrame mainFrame) {
        /*
         * FIXME: this works but has at least two issues:
         *  - barrier sizes are not consistent with the window size
         *  - the lance and fireball are positioned absolutely
         *  - just for the demo adding MainFrame to parameters
         *  but at least it works so yay
         */
        List<BarrierData> barriers = dbHandler.loadBarriers(mapId, "map");
        if (barriers == null) {
            return null;
        }

        double windowWidth = LanceOfDestiny.getScreenWidth();
        double windowHeight = LanceOfDestiny.getScreenHeight();
        Lance lance = new Lance(576, 600);
        LanceView lv = new LanceView(lance);
        Level level = new Level();
        FireBallView fbv = new FireBallView(new FireBall(632, 560));
        List<BarrierView> barrierViews =
            barriers.stream()
                    .map(b -> createBarrierView(b, windowWidth, windowHeight))
                    .toList();
        return new LevelPanel(level, lv, fbv, barrierViews, mainFrame);
    }

    private BarrierView createBarrierView(BarrierData bd, double width, double height) {
        double xPos = bd.x() * width;
        double yPos = bd.y() * height;
        int health = bd.health();
        String type = dbHandler.getBarrierTypeFromId(bd.type());
        return switch (type) {
            case SimpleBarrier.TYPE -> {
                Barrier b = new SimpleBarrier(xPos, yPos);
                b.setHealth(health);
                yield new BarrierView(b);
            }
            case FirmBarrier.TYPE -> {
                Barrier b = new FirmBarrier(xPos, yPos);
                b.setHealth(health);
                yield new BarrierView(b);
            }
            case ExplosiveBarrier.TYPE -> {
                Barrier b = new ExplosiveBarrier(xPos, yPos);
                b.setHealth(health);
                yield new BarrierView(b);
            }
            default -> {
                Barrier b = new SimpleBarrier(xPos, yPos);
                b.setHealth(health);
                yield new BarrierView(b);
            }
        };
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
