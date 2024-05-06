package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.view.View;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class LevelHandler {
    private static final View fireBallView = View.of(View.FIREBALL);
    private static final View lanceView = View.of(View.LANCE);
    private static final View simpleBarrierView = View.of(View.SIMPLE_BARRIER);
    private static final View firmBarrierView = View.of(View.FIRM_BARRIER);
    private static final View explosiveBarrierView = View.of(View.EXPLOSIVE_BARRIER);
    private static final View remainView = View.of(View.REMAIN);
    private final Logger logger = LogManager.getLogger(LevelHandler.class);
    private Level level;

    public LevelHandler(Level level) {
        this.level = level;
    }


    public void resizeLanceImage() {
        Lance lance = getLance();
        lanceView.resizeImage((int) lance.getLength(), (int) lance.getThickness());
    }

    public void resizeFireBallImage() {
        FireBall fireBall = getFireBall();
        fireBallView.resizeImage(fireBall.getSize(), fireBall.getSize());
    }

    public void resizeBarrierImages() {
        if (!getBarriers().isEmpty()) {
            Barrier b = getBarriers().getFirst();
            int length = (int) b.getLength();
            int thickness = (int) b.getThickness();
            simpleBarrierView.resizeImage(length, thickness);
            firmBarrierView.resizeImage(length, thickness);
            explosiveBarrierView.resizeImage(length, thickness);
        }
    }

    public void resizeRemainImage() {
        if (!getRemains().isEmpty()) {
            remainView.resizeImage(level.getRemains().getFirst().getSize(), level.getRemains().getFirst().getSize());
        }
    }


    public void renderLance(Graphics g) {
        Lance lance = level.getLance();
        Graphics2D g2d = (Graphics2D) g;
        double rotationAngle = Math.toRadians(lance.getRotationAngle());
        AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(rotationAngle, lance.getXPosition() + lance.getLength() / 2.0, lance.getYPosition() + lance.getThickness() / 2.0);
        g2d.drawImage(lanceView.getImage(), (int) lance.getXPosition(), (int) lance.getYPosition(), null);
        g2d.setTransform(oldTransform);    // Reset transformation to prevent unintended rotations.

        // uncomment the below two lines to see Lance Hit Box and Lance Bounding Box
        // g2d.drawPolygon(lance.getActualHitbox());
        // ((Graphics2D) g).draw(lance.getLanceBounds());
    }

    public void renderFireBall(Graphics g) {
        FireBall fireBall = level.getFireBall();
        g.drawImage(fireBallView.getImage(), (int) fireBall.getXPosition(), (int) fireBall.getYPosition(), null);
        // uncomment the below line to see FireBall hit box
        // g.drawRect((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
    }


    public void renderBarriers(Graphics g) {
        List<Barrier> barriers = level.getBarriers();
        for (Barrier barrier : barriers) {
            renderBarrier(g, barrier);
        }
    }

    private void renderBarrier(Graphics g, Barrier barrier) {
        var image = switch (barrier) {
            case SimpleBarrier ignored -> simpleBarrierView.getImage();
            case FirmBarrier ignored -> {
                renderFirmBarrier(g, barrier);
                yield null;
            }
            case ExplosiveBarrier ignored -> explosiveBarrierView.getImage();
            case Barrier ignored -> {
                logger.warn("renderBarrier: Unknown barrier type");
                yield View.of(View.MISSING_TEXTURE) // I hope this works
                          .resizeImage((int) barrier.getLength(), (int) barrier.getThickness());
            }
        };

        if (image != null) {
            g.drawImage(image, (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        }
    }

    private void renderFirmBarrier(Graphics g, Barrier barrier) {
        g.drawImage(firmBarrierView.getImage(), (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        int health = barrier.getHealth();
        int textX = (int) barrier.getXPosition() + firmBarrierView.getImage().getWidth(null) / 2;
        int textY = (int) barrier.getYPosition() - 3; // 3 pixels above the barrier
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.drawString(String.valueOf(health), textX, textY);
    }

    public void renderRemains(Graphics g) {
        List<Remain> remains = level.getRemains();
        System.out.println(remains.size());
        for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {
            renderRemainView(g, remain);
        }
    }

    private void renderRemainView(Graphics g, Remain remain) {
        g.drawImage(remainView.getImage(), (int) remain.getXPosition(), (int) remain.getYPosition(), null);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Barrier> getBarriers() {
        return level.getBarriers();
    }

    public List<Remain> getRemains() {
        return level.getRemains();
    }

    public FireBall getFireBall() {
        return level.getFireBall();
    }

    public Lance getLance() {
        return level.getLance();
    }

}
