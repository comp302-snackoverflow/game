package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BuildHandler {

    public enum BarrierType{
        SIMPLE_BARRIER,
        EXPLOSIVE_BARRIER,
        FIRM_BARRIER,
        GIFT_BARRIER,
        EMPTY_BARRIER
    }

    private BarrierType[][] barriersOnMap = new BarrierType[25][6];
    private List<Double> xIndices;
    private List<Double> yIndices;
    private List<Barrier> barriers;
    private int simpleBarrierCount;
    private int firmBarrierCount;
    private int explosiveBarrierCount;
    private int giftBarrierCount;
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
        clearGrid();

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calculateIndices(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
    }

    public void reconfigureGrid(int rowNum, int colNum){
        BarrierType[][] reconfiguredBarriers = new BarrierType[rowNum][colNum];
        for (int i = 0; i < Math.min(barriersOnMap.length, reconfiguredBarriers.length); i++){
            System.arraycopy(barriersOnMap[i], 0, reconfiguredBarriers[i], 0, Math.min(barriersOnMap[i].length, colNum));
        }
        barriersOnMap = reconfiguredBarriers;
        fillNullGridsWithFlag();
    }
    private void clearGrid(){
        for (BarrierType[] barrierTypesRow : barriersOnMap) {
            Arrays.fill(barrierTypesRow, BarrierType.EMPTY_BARRIER);
        }
    }

    private void fillNullGridsWithFlag(){
        for (int i = 0; i < barriersOnMap.length; i++) {
            for (int j = 0; j < barriersOnMap[i].length; j++) {
                if (barriersOnMap[i][j] == null) {
                    barriersOnMap[i][j] = BarrierType.EMPTY_BARRIER;
                }
            }
        }
    }

    public void countBarriers(){
        for (BarrierType[] row : barriersOnMap) {
            for (BarrierType barrier : row) {
                switch (barrier) {
                    case SIMPLE_BARRIER:
                        simpleBarrierCount++;
                        break;
                    case FIRM_BARRIER:
                        firmBarrierCount++;
                        break;
                    case EXPLOSIVE_BARRIER:
                        explosiveBarrierCount++;
                        break;
                    case GIFT_BARRIER:
                        giftBarrierCount++;
                        break;
                    default:
                        break;
                }
            }
        }
    }
    @Deprecated(forRemoval = true)
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

    private void calculateIndices(int width, int height) {

    }

    public void paintPanel(Graphics g, int panelWidth, int panelHeight) {
        paintGrid(g, panelWidth, panelHeight);
    }

    private void paintGrid(Graphics g, int panelWidth, int panelHeight) {
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
        xIndices.remove(xIndices.size() - 1);
        yIndices.remove(xIndices.size() - 1);
    }


    public void setSelection(int mode) {
        selectionMode = mode;
    }


    public void generateRandomMap(int simpleBarrierCount, int firmBarrierCount, int explosiveBarrierCount, int giftBarrierCount) {
        clearGrid();    // Clear the existing map

        SecureRandom secureRandom = new SecureRandom();

        int totalCells = barriersOnMap.length * barriersOnMap[0].length;
        int totalBarriers = simpleBarrierCount + firmBarrierCount + explosiveBarrierCount + giftBarrierCount;
        if (totalBarriers > totalCells){
            // TODO: Add logger in here.
            return;
        }
        for (int i = 0; i < simpleBarrierCount; i++) {
            placeBarrierRandomly(BarrierType.SIMPLE_BARRIER, secureRandom);
        }
        for (int i = 0; i < firmBarrierCount; i++) {
            placeBarrierRandomly(BarrierType.FIRM_BARRIER, secureRandom);
        }
        for (int i = 0; i < explosiveBarrierCount; i++) {
            placeBarrierRandomly(BarrierType.EXPLOSIVE_BARRIER, secureRandom);
        }
        for (int i = 0; i < giftBarrierCount; i++) {
            placeBarrierRandomly(BarrierType.GIFT_BARRIER, secureRandom);
        }
    }

    private void placeBarrierRandomly(BarrierType barrierType, SecureRandom secureRandom) {
        int x, y;
        do {
            x = secureRandom.nextInt(barriersOnMap.length);
            y = secureRandom.nextInt(barriersOnMap[0].length);
        } while (barriersOnMap[x][y] != BarrierType.EMPTY_BARRIER);
        barriersOnMap[x][y] = barrierType;
    }

    public void saveMap() {}

    public void clearMap() {
        clearGrid();
    }

    public void handlePress(int x, int y) {}

    public void handleMouseDrag(int x, int y) {}

    public void handleMouseMove(int x, int y) {}
}
