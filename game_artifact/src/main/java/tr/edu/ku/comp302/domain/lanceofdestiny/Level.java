package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private Lance lance;
    private FireBall fireBall;
    private List<Barrier> barriers;
    private List<Remain> remains;
    private double score;

    private static List<Level> levels = new ArrayList<>();

    public Level(Lance lance, FireBall fireBall, List<Barrier> barriers, double score) {
        this.lance = lance;
        this.fireBall = fireBall;
        this.barriers = barriers;
        this.remains = new ArrayList<>();
        this.score = score;

        for (Barrier barrier : barriers) {
            if (barrier instanceof ExplosiveBarrier b) {
                remains.add(b.getRemain());
            }
        }

        levels.add(this);
    }

    public Level(Lance lance, FireBall fireBall, List<Barrier> barriers) {
        this(lance, fireBall, barriers, 0);
    }

    public Level(Lance lance, FireBall fireBall) {
        this(lance, fireBall, new ArrayList<>());
    }

    public Level(){
        // Keeping this just just in case.
//        double xPosLance = LanceOfDestiny.getScreenWidth() / 2. + LanceOfDestiny.getScreenWidth() / 20.;
//        double yPosLance = LanceOfDestiny.getScreenHeight() * 8 / 10.;
//        Lance lance = new Lance(xPosLance, yPosLance);
//        FireBall fireBall = new FireBall(0, 0);
//        fireBall.stickToLance(lance);
        this(
            new Lance(LanceOfDestiny.getScreenWidth() / 2.0 + LanceOfDestiny.getScreenWidth() / 20.0,
                LanceOfDestiny.getScreenHeight() * 0.8),
            new FireBall(0, 0));
    }

    public Lance getLance() {
        return lance;
    }

    public void setLance(Lance lance) {
        this.lance = lance;
    }

    public FireBall getFireBall() {
        return fireBall;
    }

    public void setFireBall(FireBall fireBall) {
        this.fireBall = fireBall;
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
    }

    public List<Remain> getRemains() {
        return remains;
    }

    public void setRemains(List<Remain> remains) {
        this.remains = remains;
    }

    public static List<Level> getLevels() {
        return levels;
    }

    public static void setLevels(List<Level> levels) {
        Level.levels = levels;
    }
}

