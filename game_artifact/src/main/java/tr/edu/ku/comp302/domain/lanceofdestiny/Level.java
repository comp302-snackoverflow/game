package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private Lance lance;
    private FireBall fireBall;
    private List<Barrier> barriers = new ArrayList<>();
    private List<Remain> remains = new ArrayList<>();

    private static List<Level> levels = new ArrayList<>();

    public Level(Lance lance, FireBall fireBall, List<Barrier> barriers, List<Remain> remains) {
        this.lance = lance;
        this.fireBall = fireBall;
        this.barriers = barriers;
        this.remains = remains;
        levels.add(this);
    }

    public Level(Lance lance, FireBall fireBall) {
        this.lance = lance;
        this.fireBall = fireBall;
        levels.add(this);
    }

    public Level(){
        double xPosLance = LanceOfDestiny.getScreenWidth() / 2. + LanceOfDestiny.getScreenWidth() / 20.;
        double yPosLance = LanceOfDestiny.getScreenHeight() * 8 / 10.;
        lance = new Lance(xPosLance, yPosLance);
        fireBall = new FireBall(0, 0);
        fireBall.stickToLance(lance);
        levels.add(this);
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

