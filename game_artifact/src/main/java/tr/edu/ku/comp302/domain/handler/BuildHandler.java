package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;
import tr.edu.ku.comp302.ui.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class BuildHandler {

    public enum BarrierType{
        SIMPLE_BARRIER,
        EXPLOSIVE_BARRIER,
        FIRM_BARRIER,
        GIFT_BARRIER
    }

    private List<Barrier> barriersOnMap = new ArrayList<>();
    private int simpleBarrierCount;
    private int firmBarrierCount;
    private int explosiveBarrierCount;
    private int giftBarrierCount;
    private int selectionMode;
    private BuildPanel buildPanel;

    private BarrierRenderer barrierRenderer = new BarrierRenderer();

    public static final int DELETE_MODE = -1;
    public static final int SIMPLE_MODE = 0;
    public static final int FIRM_MODE = 1;
    public static final int EXPLOSIVE_MODE = 2;
    public static final int GIFT_MODE = 3;
    private static final int REQUIRED_SIMPLE_BARRIER_COUNT = 75;
    private static final int REQUIRED_FIRM_BARRIER_COUNT = 10;
    private static final int REQUIRED_EXPLOSIVE_BARRIER_COUNT = 5;
    private static final int REQUIRED_GIFT_BARRIER_COUNT = 10;



    public BuildHandler(BuildPanel panel) {

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // calculateIndices(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
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
//                case GiftingBarrier ignored -> giftingBarrierCount++;
                default -> {}
            }
        }
    }

    public boolean countsSatisfied() {
        return simpleBarrierCount >= REQUIRED_SIMPLE_BARRIER_COUNT &&
                firmBarrierCount >= REQUIRED_FIRM_BARRIER_COUNT &&
                explosiveBarrierCount >= REQUIRED_EXPLOSIVE_BARRIER_COUNT /* &&
                giftingBarrierCount >= REQUIRED_GIFT_BARRIER_COUNT */;
    }


    public void paintPanel(Graphics g, int panelWidth, int panelHeight) {
        /*
        TODO:
         */
    }

    private void paintBuildingSection(int panelWidth, int panelHeight){

    }

    public void setSelection(int mode) {
        selectionMode = mode;
    }


    public void generateRandomMap(int simpleBarrierCount, int firmBarrierCount, int explosiveBarrierCount, int giftBarrierCount) {
        clearMap();
        JPanel buildSection = buildPanel.getBuildSection();
        double barrierWidth = buildSection.getWidth() / 50.;
        SecureRandom secureRandom = new SecureRandom();

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



    private void generateRandomBarrier(double barrierWidth, BarrierType barrierType, SecureRandom secureRandom){
        int buildSectionWidth = buildPanel.getBuildSection().getWidth();
        int buildSectionHeight = buildPanel.getBuildSection().getHeight();
        boolean collided;
        int x, y;
        Barrier randomBarrier;
        do{
            x = secureRandom.nextInt(buildSectionWidth - (int) barrierWidth);
            y = secureRandom.nextInt(buildSectionHeight - (int) 20.);
            randomBarrier = createBarrier(barrierType, x, y, barrierWidth);
            collided = checkBarrierCollisionWithBarriers(randomBarrier);
        }while(collided);
        barriersOnMap.add(randomBarrier);

    }

    private Barrier createBarrier(BarrierType barrierType, int x, int y, double barrierWidth){
        Barrier barrier;
        switch (barrierType) {
            case SIMPLE_BARRIER -> barrier = new SimpleBarrier(x, y);
            case FIRM_BARRIER -> barrier = new FirmBarrier(x, y);
            case EXPLOSIVE_BARRIER -> barrier = new ExplosiveBarrier(x, y);
            // case GIFT_BARRIER -> barrier = new GiftBarrier(x, y);
            default -> barrier = new SimpleBarrier(x, y);
        }
        barrier.setLength(barrierWidth);
        return barrier;
    }

    public void saveMap() {}

    public void clearMap() {
        barriersOnMap.clear();
        simpleBarrierCount = 0;
        firmBarrierCount = 0;
        giftBarrierCount = 0;
        explosiveBarrierCount = 0;
        buildPanel.repaint();
    }

    public void handlePress(int x, int y) {}

    public void handleMouseDrag(int x, int y) {}

    public void handleMouseMove(int x, int y) {}

    public boolean checkBarrierCollisionWithBarriers(Barrier barrier){
        for (Barrier b : barriersOnMap) {
            if (b != barrier && checkBarrierCollision(b, barrier)) {
                return true;
            }
        }
        return false;
    }
    private boolean checkBarrierCollision(Barrier b1, Barrier b2){
        return b1.getBoundingBox().intersects(b2.getBoundingBox());
    }
}
