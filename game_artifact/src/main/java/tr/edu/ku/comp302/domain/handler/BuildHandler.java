package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuildHandler {
    private List<Double> xIndices;
    private List<Double> yIndices;
    private List<Barrier> barriers;
    private int simpleBarrierCount;
    private int firmBarrierCount;
    private int explosiveBarrierCount;
    private int giftingBarrierCount;
    private int panelWidth;
    private int panelHeight;
    private int selectionMode;

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
        xIndices = new ArrayList<>();
        yIndices = new ArrayList<>();
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calculateIndices(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
    }

    public void countBarriers(List<Barrier> barriers) {
        simpleBarrierCount = 0;
        firmBarrierCount = 0;
        explosiveBarrierCount = 0;
        giftingBarrierCount = 0;

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

    private void calculateIndices(int width, int height) {

    }

    public void paintPanel(Graphics g, int panelWidth, int panelHeight) {
        paintGrid(g, panelWidth, panelHeight);
    }

    private void paintGrid(Graphics g, int panelWidth, int panelHeight) {
        double x_start= panelWidth/52;
        int yEnd = panelHeight >> 1;

        g.setColor(Color.BLACK);

        for (double x = 0; x <= panelWidth; x += panelWidth / 25.0){
            g.drawLine((int) x, 0, (int) x, yEnd);
            xIndices.add(x);
        }

        for(double y = 0; y <= yEnd + 1; y += yEnd / 6.0) {
            g.drawLine(0, (int)y, panelWidth, (int) y);
            yIndices.add(y);
        }
        // removed the last indices so that the generated barriers do not go out of bounds.
        xIndices.removeLast();
        yIndices.removeLast();


    }


    public void setSelection(int mode) {
        selectionMode = mode;
    }


    public void generateRandomMap(int simpleCount, int firmCount, int explosiveCount, int giftingCount) {
        List<Integer> barriersToAdd = new ArrayList<>(simpleCount + firmCount + explosiveCount + giftingCount);

        for (int i = 0; i < simpleCount; i++) {
            barriersToAdd.add(SIMPLE_MODE); // I know this is not for this purpose. But whatever, it will work
        }

        for (int i = 0; i < firmCount; i++) {
            barriersToAdd.add(FIRM_MODE);
        }

        for (int i = 0; i < explosiveCount; i++) {
            barriersToAdd.add(EXPLOSIVE_MODE);
        }

        for (int i = 0; i < giftingCount; i++) {
            barriersToAdd.add(GIFT_MODE);
        }

        Collections.shuffle(barriersToAdd);

    }

    public void saveMap() {}

    public void clearMap() {}

    public void handlePress(int x, int y) {}

    public void handleMouseDrag(int x, int y) {}

    public void handleMouseMove(int x, int y) {}
}
