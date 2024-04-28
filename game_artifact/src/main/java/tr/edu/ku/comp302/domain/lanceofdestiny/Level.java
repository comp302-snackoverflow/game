package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private Lance lance;
    private FireBall fireBall;
    private List<Barrier> barriers = new ArrayList<>();

    public Level(Lance lance, FireBall fireBall, List<Barrier> barriers) {
        this.lance = lance;
        this.fireBall = fireBall;
        this.barriers = barriers;
    }

    public Level(Lance lance, FireBall fireBall) {
        this.lance = lance;
        this.fireBall = fireBall;
    }

    public Level(){
        double xPosLance = LanceOfDestiny.getScreenWidth() / 2. + LanceOfDestiny.getScreenWidth() / 20.;
        double yPosLance = LanceOfDestiny.getScreenHeight() * 8 / 10.;
        lance = new Lance(xPosLance, yPosLance);
        fireBall = new FireBall(0, 0);
        fireBall.stickToLance(lance);
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
}

