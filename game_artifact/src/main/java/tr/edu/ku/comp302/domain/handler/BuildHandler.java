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
import java.awt.image.BufferedImage;
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

    private int oldWidth;
    private int oldHeight;
    private List<Barrier> barriersOnMap = new ArrayList<>();
    private int simpleBarrierCount;
    private int firmBarrierCount;
    private int explosiveBarrierCount;
    private int giftBarrierCount;
    private int selectionMode;
    private BuildPanel buildPanel;

    private BarrierRenderer barrierRenderer = new BarrierRenderer();
    public static final int NOTHING_SELECTED_MODE = -2;

    public static final int DELETE_MODE = -1;
    public static final int SIMPLE_MODE = 1;
    public static final int FIRM_MODE = 2;
    public static final int EXPLOSIVE_MODE = 3;
    public static final int GIFT_MODE = 4;
    private static final int REQUIRED_SIMPLE_BARRIER_COUNT = 75;
    private static final int REQUIRED_FIRM_BARRIER_COUNT = 10;
    private static final int REQUIRED_EXPLOSIVE_BARRIER_COUNT = 5;
    private static final int REQUIRED_GIFT_BARRIER_COUNT = 10;



    public BuildHandler(BuildPanel panel) {
        buildPanel = panel;
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component component = e.getComponent();
                if (component instanceof BuildPanel){
                    JPanel buildSection = ((BuildPanel) component).getBuildSection();
                    resize(buildSection.getWidth(), buildSection.getHeight());

                }
            }
        });
    }

    private void resize(int width, int height){
        buildPanel.getBuildSection().setSize(width, height);
        for (Barrier b : barriersOnMap){
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
//                case GiftingBarrier ignored -> giftingBarrierCount++;
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
                explosiveBarrierCount >= REQUIRED_EXPLOSIVE_BARRIER_COUNT /* &&
                giftBarrierCount >= REQUIRED_GIFT_BARRIER_COUNT */ &&
                // FIXME: Decide on below constant later.
                simpleBarrierCount + firmBarrierCount + explosiveBarrierCount + giftBarrierCount <= 200;
    }


    public void paintPanel(Graphics g, int panelWidth, int panelHeight) {
        double lanceLength = panelWidth / 10.;
        barrierRenderer.resizeBarrierImages(barriersOnMap);
        barrierRenderer.renderBarriers(g, barriersOnMap);
        BufferedImage lanceImage = View.of(View.LANCE).getImage();
        int lanceXPos = (int) (panelWidth - lanceLength) / 2;
        int lanceYPos = panelHeight * 6 / 8;
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
        if (!countsSatisfied(simpleBarrierCount, firmBarrierCount, explosiveBarrierCount, giftBarrierCount)){
            // TODO: Add system message and logger
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



    private void generateRandomBarrier(double barrierWidth, BarrierType barrierType, SecureRandom secureRandom){
        int buildSectionWidth = buildPanel.getBuildSection().getWidth();
        int buildSectionHeight = buildPanel.getBuildSection().getHeight() * 4 / 8; // TODO: Change ratio later to a constant value
        boolean collided;
        int x, y;
        Barrier randomBarrier;
        do{
            x = (int)barrierWidth / 2 + secureRandom.nextInt(buildSectionWidth - (int) barrierWidth - (int) barrierWidth / 2);
            y = 20 + secureRandom.nextInt(buildSectionHeight - 40);  // -20 barrier -20 padding
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

    public void handlePress(int x, int y) {
        if (selectionMode == -2){
            return;
        }
        System.out.println(selectionMode);
        if (barriersOnMap.size() <= 200){
            putBarrierToMap(x, y, selectionMode);
            buildPanel.repaint();
        }else{
            // TODO: Add logger message and system msg.
        }

    }

    public void handleMouseDrag(int x, int y) {}

    public void handleMouseMove(int x, int y) {

    }

    private void putBarrierToMap(int x, int y, int selectionMode){
        JPanel buildSection = buildPanel.getBuildSection();
        int xMax = buildSection.getWidth() - buildSection.getWidth() / 50;
        int xMin = buildSection.getWidth() / 100; // Barrier size / 2
        int yMax = buildSection.getHeight() * 4 / 8  - 20;
        int yMin = 20;
        if (x <= xMax && y <= yMax && x >= xMin && y >= yMin){
            Barrier createdBarrier = createBarrier(buildSection.getWidth() / 50, x, y, selectionMode);
            if (!checkBarrierCollisionWithBarriers(createdBarrier)){
                barriersOnMap.add(createdBarrier);
            }else{
                // TODO: Add logger message.
            }
        }
    }

    private Barrier createBarrier(int width, int x, int y, int selectionMode){
        Barrier barrier;
        switch(selectionMode){
            case BuildHandler.SIMPLE_MODE -> barrier = new SimpleBarrier(x, y);
            case BuildHandler.FIRM_MODE -> barrier = new FirmBarrier(x, y);
            case BuildHandler.EXPLOSIVE_MODE -> barrier = new ExplosiveBarrier(x, y);
            // case BuildHandler.GIFT_MODE -> barrier = new GiftBarrier(x, y);
            default -> barrier = new SimpleBarrier(x, y); // TODO: Add logger obj
        }
        barrier.setLength(width);
        return barrier;
    }


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

    public ArrayList<Barrier> generateRandomMapTest(int simpleBarrierCount, int firmBarrierCount, int explosiveBarrierCount, int giftBarrierCount) {
        clearMap();
        JPanel buildSection = buildPanel.getBuildSection();
        double barrierWidth = buildSection.getWidth() / 50.;
        ArrayList<Barrier> randomMap = new ArrayList<>();
        SecureRandom secureRandom = new SecureRandom();
        if (!countsSatisfied(simpleBarrierCount, firmBarrierCount, explosiveBarrierCount, giftBarrierCount)){
            // TODO: Add system message and logger
            return null;
        }
        for (int i = 0; i < simpleBarrierCount; i++) {
            Barrier barrier = generateRandomBarrierTest(barrierWidth, BarrierType.SIMPLE_BARRIER, secureRandom);
            randomMap.add(barrier);
        }
        for (int i = 0; i < firmBarrierCount; i++) {
            Barrier barrier = generateRandomBarrierTest(barrierWidth, BarrierType.FIRM_BARRIER, secureRandom);
            randomMap.add(barrier);
        }
        for (int i = 0; i < explosiveBarrierCount; i++) {
            Barrier barrier = generateRandomBarrierTest(barrierWidth, BarrierType.EXPLOSIVE_BARRIER, secureRandom);
            randomMap.add(barrier);
        }
        for (int i = 0; i < giftBarrierCount; i++) {
            Barrier barrier = generateRandomBarrierTest(barrierWidth, BarrierType.GIFT_BARRIER, secureRandom);
            randomMap.add(barrier);
        }
        countBarriers(barriersOnMap);
        return randomMap;
    }



    private Barrier generateRandomBarrierTest(double barrierWidth, BarrierType barrierType, SecureRandom secureRandom){
        int buildSectionWidth = buildPanel.getBuildSection().getWidth();
        int buildSectionHeight = buildPanel.getBuildSection().getHeight() * 4 / 8; // TODO: Change ratio later to a constant value
        boolean collided;
        int x, y;
        Barrier randomBarrier;
        do{
            x = (int)barrierWidth / 2 + secureRandom.nextInt(buildSectionWidth - (int) barrierWidth - (int) barrierWidth / 2);
            y = 20 + secureRandom.nextInt(buildSectionHeight - 40);  // -20 barrier -20 padding
            randomBarrier = createBarrier(barrierType, x, y, barrierWidth);
            collided = checkBarrierCollisionWithBarriers(randomBarrier);
        }while(collided);
        barriersOnMap.add(randomBarrier);
        return randomBarrier;
    }

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
}
