package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.GiftBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.HollowBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.services.save.SaveService;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;
import tr.edu.ku.comp302.ui.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class BuildHandler {
    public static final int NOTHING_SELECTED_MODE = -2;
    public static final int DELETE_MODE = -1;
    public static final int SIMPLE_MODE = 1;
    public static final int FIRM_MODE = 2;
    public static final int EXPLOSIVE_MODE = 3;
    public static final int GIFT_MODE = 4;
    private static final Logger logger = LogManager.getLogger(BuildHandler.class);
    private static final int REQUIRED_SIMPLE_BARRIER_COUNT = 75;
    private static final int REQUIRED_FIRM_BARRIER_COUNT = 10;
    private static final int REQUIRED_EXPLOSIVE_BARRIER_COUNT = 5;
    private static final int REQUIRED_GIFT_BARRIER_COUNT = 10;
    private final List<Barrier> barriersOnMap = new ArrayList<>();
    private final BuildPanel buildPanel;
    private final BarrierRenderer barrierRenderer = new BarrierRenderer();
    private int oldWidth;
    private int oldHeight;
    private int simpleBarrierCount;
    private int firmBarrierCount;
    private int explosiveBarrierCount;
    private int giftBarrierCount;
    private int selectionMode;

    public BuildHandler(BuildPanel panel) {
        buildPanel = panel;
        oldWidth = panel.getWidth();
        oldHeight = panel.getHeight();
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component component = e.getComponent();
                if (component instanceof BuildPanel panel) {
                    JPanel buildSection = panel.getBuildSection();

                    int newWidth = buildSection.getWidth();
                    int newHeight = buildSection.getHeight();
                    resizeBarriersOnMap(newWidth, newHeight);
                }
            }
        });
    }

    private void resizeBarriersOnMap(int width, int height) {
        for (Barrier b : barriersOnMap) {
            b.adjustPositionAndSize(oldWidth, oldHeight, width, height);
        }
        oldWidth = width;
        oldHeight = height;
    }

    public void countBarriers(List<Barrier> barriers) {
        simpleBarrierCount = 0;
        firmBarrierCount = 0;
        explosiveBarrierCount = 0;
        giftBarrierCount = 0;

        for (Barrier barrier : barriers) {
            switch (barrier) {
                case SimpleBarrier ignored -> simpleBarrierCount++;
                case FirmBarrier ignored -> firmBarrierCount++;
                case ExplosiveBarrier ignored -> explosiveBarrierCount++;
                case GiftBarrier ignored -> giftBarrierCount++;
                default -> {}
            }
        }
    }

    public boolean countsSatisfied() {
        return countsSatisfied(simpleBarrierCount, firmBarrierCount, explosiveBarrierCount, giftBarrierCount);
    }

    public boolean countsSatisfied(int simpleBarrierCount, int firmBarrierCount, int explosiveBarrierCount, int giftBarrierCount) {
        return simpleBarrierCount >= REQUIRED_SIMPLE_BARRIER_COUNT &&
                firmBarrierCount >= REQUIRED_FIRM_BARRIER_COUNT &&
                explosiveBarrierCount >= REQUIRED_EXPLOSIVE_BARRIER_COUNT &&
                giftBarrierCount >= REQUIRED_GIFT_BARRIER_COUNT &&
                // FIXME: Decide on below constant later.
                simpleBarrierCount + firmBarrierCount + explosiveBarrierCount + giftBarrierCount <= 200;
    }

    public void paintPanel(Graphics g, int panelWidth, int panelHeight) {
        double lanceLength = panelWidth / 10.;
        barrierRenderer.resizeBarrierImages(barriersOnMap);
        barrierRenderer.renderBarriers(g, barriersOnMap);
        BufferedImage lanceImage = View.of(View.LANCE).getImage();
        int lanceXPos = (int) (panelWidth - lanceLength) / 2;
        int lanceYPos = panelHeight * 7 / 8;
        g.drawImage(ImageHandler.resizeImage(lanceImage, (int) lanceLength, 20), lanceXPos, lanceYPos, null);
    }

    public void setSelection(int mode) {
        selectionMode = mode;
    }

    public void generateRandomMap(int simpleBarrierCount, int firmBarrierCount, int explosiveBarrierCount, int giftBarrierCount) {
        clearMap();
        JPanel buildSection = buildPanel.getBuildSection();
        double barrierWidth = buildSection.getWidth() / 50.;
        SecureRandom secureRandom = new SecureRandom();
        if (!countsSatisfied(simpleBarrierCount, firmBarrierCount, explosiveBarrierCount, giftBarrierCount)) {
            logger.info("Counts are not satisfied. simple: {}, firm: {}, explosive: {}, gift: {}", simpleBarrierCount, firmBarrierCount, explosiveBarrierCount, giftBarrierCount);

            return;
        }
        for (int i = 0; i < simpleBarrierCount; i++) {
            generateRandomBarrier(barrierWidth, BarrierType.SIMPLE_BARRIER, secureRandom);
        }
        for (int i = 0; i < firmBarrierCount; i++) {
            generateRandomBarrier(barrierWidth, BarrierType.FIRM_BARRIER, secureRandom);
        }
        for (int i = 0; i < explosiveBarrierCount; i++) {
            generateRandomBarrier(barrierWidth, BarrierType.EXPLOSIVE_BARRIER, secureRandom);
        }
        for (int i = 0; i < giftBarrierCount; i++) {
            generateRandomBarrier(barrierWidth, BarrierType.GIFT_BARRIER, secureRandom);
        }
        countBarriers(barriersOnMap);
    }

    private void generateRandomBarrier(double barrierWidth, BarrierType barrierType, SecureRandom secureRandom) {
        int buildSectionWidth = buildPanel.getBuildSection().getWidth();
        int buildSectionHeight = buildPanel.getBuildSection().getHeight() * 4 / 8; // TODO: Change ratio later to a constant value
        boolean collided;
        int x, y;
        Barrier randomBarrier;
        do {
            x = (int) barrierWidth / 2 + secureRandom.nextInt(buildSectionWidth - (int) barrierWidth - (int) barrierWidth / 2);
            y = 20 + secureRandom.nextInt(buildSectionHeight - 40);  // -20 barrier -20 padding
            randomBarrier = createBarrier(barrierType, x, y, barrierWidth);
            // collided = checkBarrierCollisionWithBarriers(randomBarrier);
            collided = CollisionHandler.checkBarrierCollisionWithBarriers(randomBarrier, barriersOnMap);
        }while(collided);
        barriersOnMap.add(randomBarrier);
    }

    private Barrier createBarrier(BarrierType barrierType, int x, int y, double barrierWidth) {
        Barrier barrier;
        switch (barrierType) {
            case SIMPLE_BARRIER -> barrier = new SimpleBarrier(x, y);
            case FIRM_BARRIER -> barrier = new FirmBarrier(x, y);
            case EXPLOSIVE_BARRIER -> barrier = new ExplosiveBarrier(x, y);
            case GIFT_BARRIER -> barrier = new GiftBarrier(x, y);
            case HOLLOW_BARRIER -> barrier = new HollowBarrier(x, y);
            default -> barrier = new SimpleBarrier(x, y);
        }
        barrier.setLength(barrierWidth);
        return barrier;
    }

    public Level getLevel() {
        LanceOfDestiny.setScreenWidth(buildPanel.getWidth());
        LanceOfDestiny.setScreenHeight(buildPanel.getHeight());
        Level level = new Level();
        level.setBarriers(new ArrayList<>(barriersOnMap));
        LevelHandler levelHandler = new LevelHandler(level);
        levelHandler.resizeLanceImage();
        levelHandler.resizeFireBallImage();
        levelHandler.resizeBarrierImages();
        levelHandler.resizeRemainImage();
        // FIXME: Resizing is wrong: Barriers overlap with each other
        //  It is still buggy without this and I think its about vertical positioning.
        level.getBarriers().forEach(barrier -> barrier.adjustPositionAndSize(buildPanel.getBuildSection().getWidth(), buildPanel.getBuildSection().getHeight(), buildPanel.getWidth(), buildPanel.getHeight()));
        return level;
    }

    public void saveMap(double width, double height) {
        if (!countsSatisfied()) {
            return;
        }
        if (SaveService.getInstance().saveMap(barriersOnMap, width, height)) {
            logger.info("Saved map successfully.");
            buildPanel.alertSaveSuccess();
            clearMap();
        } else {
            buildPanel.alertSaveFailure();
        }
    }

    public void clearMap() {
        barriersOnMap.clear();
        simpleBarrierCount = 0;
        firmBarrierCount = 0;
        giftBarrierCount = 0;
        explosiveBarrierCount = 0;
        buildPanel.repaint();
    }

    public void handlePress(int x, int y) {
        if (selectionMode == -2) {
            return;
        } else if (selectionMode == -1) {
            deleteBarrier(x, y);
        }
        else{
            if (barriersOnMap.size() < 200) {
                putBarrierToMap(x, y, selectionMode);
            } else {
                buildPanel.alertFullMap();
            }
        }
        buildPanel.repaint();
    }

    public void handleMouseDrag(int x, int y) {
    }

    public void handleMouseMove(int x, int y) {

    }

    private void putBarrierToMap(int x, int y, int selectionMode) {
        JPanel buildSection = buildPanel.getBuildSection();
        int xMax = buildSection.getWidth() - buildSection.getWidth() / 50;
        int xMin = buildSection.getWidth() / 100; // Barrier size / 2
        int yMax = buildSection.getHeight() * 4 / 8 - 20;
        int yMin = 20;
        if (x <= xMax && y <= yMax && x >= xMin && y >= yMin) {
            Barrier createdBarrier = createBarrier(buildSection.getWidth() / 50, x, y, selectionMode);
            // if (!checkBarrierCollisionWithBarriers(createdBarrier)){
            if (!CollisionHandler.checkBarrierCollisionWithBarriers(createdBarrier, barriersOnMap)) {
                barriersOnMap.add(createdBarrier);
            } else {
                // TODO: Add logger message.
            }
        }
    }

    private void deleteBarrier(int x, int y){
        Barrier tempBarrier = new SimpleBarrier(x, y);
        Barrier barrierToRemove = checkBarrierCollisionWithBarriers(tempBarrier);
        if (barrierToRemove != null){
            barriersOnMap.remove(barrierToRemove);

        }
    }

    private Barrier createBarrier(int width, int x, int y, int selectionMode) {
        Barrier barrier = switch (selectionMode) {
            case BuildHandler.SIMPLE_MODE -> new SimpleBarrier(x, y);
            case BuildHandler.FIRM_MODE -> new FirmBarrier(x, y);
            case BuildHandler.EXPLOSIVE_MODE -> new ExplosiveBarrier(x, y);
            case BuildHandler.GIFT_MODE -> new GiftBarrier(x, y);
            default -> {
                logger.warn("Unknown selection mode: {}", selectionMode);
                yield new SimpleBarrier(x, y);
            }
        };
        barrier.setLength(width);
        return barrier;
    }

    public Barrier checkBarrierCollisionWithBarriers(Barrier barrier) {
        for (Barrier b : barriersOnMap) {
            if (b != barrier && checkBarrierCollision(b, barrier)) {
                return b;
            }
        }
        return null;
    }

    private boolean checkBarrierCollision(Barrier b1, Barrier b2) {
        return b1.getBoundingBox().intersects(b2.getBoundingBox());
    }
    //TODO: delete the two above when tested correctly !

    public int getOldWidth() {
        return oldWidth;
    }

    public void setOldWidth(int oldWidth) {
        this.oldWidth = oldWidth;
    }

    public int getOldHeight() {
        return oldHeight;
    }

    public void setOldHeight(int oldHeight) {
        this.oldHeight = oldHeight;
    }

    private enum BarrierType {
        SIMPLE_BARRIER, EXPLOSIVE_BARRIER, FIRM_BARRIER, GIFT_BARRIER, HOLLOW_BARRIER
    }
}
