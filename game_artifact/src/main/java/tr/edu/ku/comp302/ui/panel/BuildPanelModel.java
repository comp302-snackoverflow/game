package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.entity.Barriers.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.Barriers.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.Barriers.SimpleBarrier;
import tr.edu.ku.comp302.ui.view.BarrierView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildPanelModel {
    int simpleBarrierCount = 0;
    int firmBarrierCount = 0;
    int explosiveBarrierCount = 0;
    int giftBarrierCount = 0;

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
        int giftCount = 0;

        //TODO: add the gift barrier once you have it !
        for (Map.Entry <List<Double>, BarrierView> e : barriers.entrySet()) {
            switch (e.getValue().getBarrier()) {
                case FirmBarrier f -> firmCount++;
                case ExplosiveBarrier ex -> explosiveCount++;
                case SimpleBarrier s -> simpleCount++;
                default -> {
                    break;
                }
            }

            }

        this.simpleBarrierCount = simpleCount;
        this.firmBarrierCount = firmCount;
        this.explosiveBarrierCount = explosiveCount;
        }
    }


