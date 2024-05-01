package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.view.BarrierView;

import java.util.*;

public class BuildPanelModel {
    int simpleBarrierCount = 0;
    int firmBarrierCount = 0;
    int explosiveBarrierCount = 0;
    int giftBarrierCount = 0;
    public BuildPanelModel() {

    }

    public int getFirmBarrierCount() {
        return firmBarrierCount;
    }

    public void setFirmBarrierCount(int firmBarrierCount) {
        this.firmBarrierCount = firmBarrierCount;
    }

    public int getExplosiveBarrierCount() {
        return explosiveBarrierCount;
    }

    public void setExplosiveBarrierCount(int explosiveBarrierCount) {
        this.explosiveBarrierCount = explosiveBarrierCount;
    }

    public int getSimpleBarrierCount() {
        return simpleBarrierCount;
    }

    public void setSimpleBarrierCount(int simpleBarrierCount) {
        this.simpleBarrierCount = simpleBarrierCount;
    }

    public int getGiftBarrierCount() {
        return giftBarrierCount;
    }

    public void setGiftBarrierCount(int giftBarrierCount) {
        this.giftBarrierCount = giftBarrierCount;
    }

    public void countBarriers(HashMap<List<Double>, BarrierView> barriers) {
        int firmCount = 0;
        int simpleCount = 0;
        int explosiveCount = 0;
//        int giftCount = 0;

        //TODO: add the gift barrier once you have it !
        for (Map.Entry <List<Double>, BarrierView> e : barriers.entrySet()) {
            switch (e.getValue().getBarrier()) {
                case FirmBarrier ignored -> firmCount++;
                case ExplosiveBarrier ignored -> explosiveCount++;
                case SimpleBarrier ignored -> simpleCount++;
                default -> {}
            }
        }

        this.simpleBarrierCount = simpleCount;
        this.firmBarrierCount = firmCount;
        this.explosiveBarrierCount = explosiveCount;
        }


        //This method assumes that the correct number of barriers have been given, and also the total barrier
        //number does not exceed the whole map.
        public HashMap<List<Double>, BarrierView> generateRandomMap(ArrayList<Double> xIndices, ArrayList<Double> yIndices, int simpleNum, int explosiveNum, int giftNum, int firmNum) {
            HashMap<List<Double>, BarrierView> newMap = new HashMap<>();
            ArrayList<Double> xCopy = new ArrayList<>(xIndices);
            ArrayList<Double> yCopy = new ArrayList<>(yIndices);

            generateRandomBarrierType(newMap, xCopy, yCopy, simpleNum, "simple");
            generateRandomBarrierType(newMap, xCopy, yCopy, explosiveNum, "explosive");
            generateRandomBarrierType(newMap, xCopy, yCopy, firmNum, "firm");

            return newMap;
        }

        // this method generates barriers in random parts of the map according to their size.
        //TODO: ADD logic for the gift barrier later!!!!!
        public void generateRandomBarrierType (HashMap<List<Double>, BarrierView> barrierMap, ArrayList<Double> xIndices, ArrayList<Double> yIndices, int barrierNum, String barrierType) {

            
            for (int i = 1; i <= barrierNum; i++) {
                ArrayList<Double> coordinates = new ArrayList<>();
                Random random = new Random();
                int randomXIndex = random.nextInt(xIndices.size());
                int randomYIndex = random.nextInt(yIndices.size());

                // if the coordinate was already generated, generate again. Might change this part later
                // because it looks very inefficient.
                while (barrierMap.containsKey(new ArrayList<Double>(Arrays.asList(xIndices.get(randomXIndex), yIndices.get(randomYIndex))))) {
                    randomXIndex = random.nextInt(xIndices.size());
                    randomYIndex = random.nextInt(yIndices.size());
                }
                double x = xIndices.get(randomXIndex);
                double y = yIndices.get(randomYIndex);
                coordinates.add(x);
                coordinates.add(y);
                Barrier barrier;
                BarrierView view;

                double width = LanceOfDestiny.getScreenWidth();
                double height = LanceOfDestiny.getScreenHeight();

                switch (barrierType) {
                    case "simple":
                        barrier = new SimpleBarrier(x + width/104, y + (((height/2)-120)/7)/2);
                        view = new BarrierView(barrier);
                        scaleBarrierImages(view);
                        barrierMap.put(coordinates, view);
                        break;
                    case "explosive":
                        barrier = new ExplosiveBarrier(x + width/104, y +(((height/2)-120)/7)/2);
                        view = new BarrierView(barrier);
                        scaleBarrierImages(view);
                        barrierMap.put(coordinates, view);
                        break;
                    case "firm":
                        barrier = new FirmBarrier(x + width/104, y + (((height/2)-120)/7)/2);
                        view = new BarrierView(barrier);
                        scaleBarrierImages(view);
                        barrierMap.put(coordinates, view);
                        break;
                    default:
                        break;
                }

            }
        }

    public void scaleBarrierImages(BarrierView barrierView){
        barrierView.getBarrier().setL(LanceOfDestiny.getScreenWidth() / 10.0); // TODO: setL here
        barrierView.setBarrierImage(ImageHandler.resizeImage(
                barrierView.getBarrierImage(),
                (int) barrierView.getBarrier().getLength(),
                (int) barrierView.getBarrier().getThickness()
        ));
    }

    public boolean barrierConditionsSatisfied(int simpleBarrierCount, int firmBarrierCount, int explosiveBarrierCount, int giftBarrierCount) {
        return simpleBarrierCount >= 75 && firmBarrierCount >= 10 && explosiveBarrierCount >= 5 && giftBarrierCount >= 10;
    }
}
