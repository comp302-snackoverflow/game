package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.view.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class LevelHandler {
    private Level level;

    private static FireBallView fireBallView = new FireBallView();
    private static LanceView lanceView = new LanceView();
    private static BarrierView simpleBarrierView = new BarrierView("simple");

    private static BarrierView firmBarrierView = new BarrierView("firm");

    private static BarrierView explosiveBarrierView = new BarrierView("explosive");

    private static RemainView remainView = new RemainView();

    public LevelHandler(Level level) {
        this.level = level;
    }


    public void resizeLanceImage(){
        Lance lance = getLance();
        lanceView.setLanceImage();      // reset image to its original sizes
        lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
                (int) lance.getLength(),
                (int) lance.getThickness()));
    }

    public void resizeFireBallImage(){
        FireBall fireBall = getFireBall();
        fireBallView.setFireBallImage();
        fireBallView.setFireBallImage(ImageHandler.resizeImage(fireBallView.getFireBallImage(),
                fireBall.getSize(),
                fireBall.getSize()));
    }


    public void renderLance(Graphics g){
        Lance lance = level.getLance();
        Graphics2D g2d = (Graphics2D) g;
        double rotationAngle = Math.toRadians(lance.getRotationAngle());
        AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(rotationAngle, lance.getXPosition() + lance.getLength() / 2.0, lance.getYPosition() + lance.getThickness() / 2.0);
        g2d.drawImage(lanceView.getLanceImage(), (int) lance.getXPosition(), (int) lance.getYPosition(), null);
        g2d.setTransform(oldTransform);    // Reset transformation to prevent unintended rotations.

        // uncomment the below two lines to see Lance Hit Box and Lance Bounding Box
        // g2d.drawPolygon(lance.getActualHitbox());
        // ((Graphics2D) g).draw(lance.getLanceBounds());
    }

    public void renderFireBall(Graphics g){
        FireBall fireBall = level.getFireBall();
        g.drawImage(fireBallView.getFireBallImage(), (int) fireBall.getXPosition(), (int) fireBall.getYPosition(), null);
        // uncomment the below line to see FireBall hit box
        // g.drawRect((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
    }


    public void renderBarriers(Graphics g){
        List<Barrier> barriers = level.getBarriers();
        for (int i = 0; i < barriers.size(); i++){
            Barrier barrier = barriers.get(i);
            renderBarrier(g, barrier);
        }
    }

    private void renderBarrier(Graphics g, Barrier barrier){
        if (barrier instanceof ExplosiveBarrier){
            g.drawImage(explosiveBarrierView.getBarrierImage(), (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        }else if (barrier instanceof SimpleBarrier){
            g.drawImage(simpleBarrierView.getBarrierImage(), (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        }else if (barrier instanceof FirmBarrier){
            renderFirmBarrier(g, barrier);
        }
    }

    private void renderFirmBarrier(Graphics g, Barrier barrier){
        g.drawImage(firmBarrierView.getBarrierImage(), (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        int health = barrier.getHealth();
        int textX = (int) barrier.getXPosition() + firmBarrierView.getBarrierImage().getWidth(null) / 2;
        int textY = (int) barrier.getYPosition() - 3; // 3 pixels above the barrier
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.drawString(String.valueOf(health), textX, textY);
    }

    public void renderRemains(Graphics g){
        List<Remain> remains = level.getRemains();
        for (int i = 0; i < remains.size(); i++){
            Remain remain = remains.get(i);
            renderRemainView(g, remain);
        }
    }

    private void renderRemainView(Graphics g, Remain remain){
        g.drawImage(remainView.getRemainImage(), (int) remain.getXPosition(), (int) remain.getYPosition(), null);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Barrier> getBarriers(){
        return level.getBarriers();
    }

    public List<Remain> getRemains(){
        return level.getRemains();
    }

    public FireBall getFireBall(){
        return level.getFireBall();
    }

    public Lance getLance(){
        return level.getLance();
    }

}
