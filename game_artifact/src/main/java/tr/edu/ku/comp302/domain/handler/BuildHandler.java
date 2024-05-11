package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;
import tr.edu.ku.comp302.ui.view.View;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class BuildHandler {

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

    public void setSelection(int mode) {
        selectionMode = mode;
    }


    public void generateRandomMap(int simpleBarrierCount, int firmBarrierCount, int explosiveBarrierCount, int giftBarrierCount) {

    }

    public void saveMap() {}

    public void clearMap() {
        barriersOnMap.clear();
        // TODO: Repaint
    }

    public void handlePress(int x, int y) {}

    public void handleMouseDrag(int x, int y) {}

    public void handleMouseMove(int x, int y) {}
}
