package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.chrono.Chronometer;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.view.View;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class LevelHandler {
    private static final View fireBallView = View.of(View.FIREBALL);
    private static final View lanceView = View.of(View.LANCE);
    private static final View remainView = View.of(View.REMAIN);
    private static final Logger logger = LogManager.getLogger(LevelHandler.class);
    private final BarrierRenderer barrierRenderer = new BarrierRenderer();
    private Level level;

    private Character lastMoving;
    private boolean tapMoving;

    public LevelHandler(Level level) {
        this.level = level;
        lastMoving = null;
        tapMoving = false;
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
        barrierRenderer.resizeBarrierImages(getBarriers());
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
        synchronized (lance) {
            g2d.rotate(rotationAngle, lance.getXPosition() + lance.getLength() / 2.0, lance.getYPosition() + lance.getThickness() / 2.0);
            g2d.drawImage(lanceView.getImage(), (int) lance.getXPosition(), (int) lance.getYPosition(), null);
            g2d.setTransform(oldTransform);    // Reset transformation to prevent unintended rotations.
        }
        // uncomment the below two lines to see Lance Hit Box and Lance Bounding Box
        // g2d.drawPolygon(lance.getActualHitbox());
        // ((Graphics2D) g).draw(lance.getLanceBounds());
    }

    public void renderFireBall(Graphics g) {
        FireBall fireBall = level.getFireBall();
        synchronized (fireBall) {
            g.drawImage(fireBallView.getImage(), (int) fireBall.getXPosition(), (int) fireBall.getYPosition(), null);
            // uncomment the below line to see FireBall hit box
            // g.drawRect((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
        }
    }

    public void renderBarriers(Graphics g) {
        var barriers = getBarriers();
        synchronized (barriers) {
            barrierRenderer.renderBarriers(g, barriers);
        }
    }

    public void renderRemains(Graphics g) {
        List<Remain> remains = level.getRemains();
        synchronized (remains) {
            for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {
                renderRemainView(g, remain);
            }
        }
    }

    private void renderRemainView(Graphics g, Remain remain) {
        g.drawImage(remainView.getImage(), (int) remain.getXPosition(), (int) remain.getYPosition(), null);
    }

    public void handleGameLogic(long currentTime, Chronometer chronometer, int upsSet) {
        boolean moveLeft = KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed;
        boolean moveRight = KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed;

        boolean rotateCCW = KeyboardHandler.buttonAPressed && !KeyboardHandler.buttonDPressed;
        boolean rotateCW = KeyboardHandler.buttonDPressed && !KeyboardHandler.buttonAPressed;
        handleLanceMovement(moveLeft, moveRight, chronometer, upsSet);
        handleRotationLogic(rotateCCW, -Lance.rotationSpeed, upsSet);
        handleRotationLogic(rotateCW, Lance.rotationSpeed, upsSet);
        handleSteadyStateLogic(!rotateCCW && !rotateCW, Lance.horizontalRecoverySpeed, upsSet);

        handleFireballLogic(upsSet);

        handleBarriersMovement(currentTime, upsSet);

        handleCollisionLogic(currentTime);
    }

    // Warning: DO NOT try to make this method clean. You will most likely fail.
    // Let's spend our time in more valuable stuff like writing actually useful code
    // instead of trying to invent key tap event in Swing.
    // Just check for bugs and leave it be.
    // Copilot's thoughts about this function: "I'm not sure what you're trying to do here."
    // (It couldn't even suggest any reasonable code for this)
    private void handleLanceMovement(boolean leftPressed, boolean rightPressed, Chronometer chronometer, int upsSet) {
        Lance lance = getLance();
        double holdSpeed = lance.getSpeedWithHold();
        double tapSpeed = lance.getSpeedWithTap();

        long currentTime = chronometer.getCurrentTime();
        if (leftPressed != rightPressed) {
            int index = leftPressed ? 1 : 0;
            Character oldLastMoving = lastMoving;
            lastMoving = leftPressed ? 'l' : 'r';
            lance.setDirection(leftPressed ? -1 : 1);

            if (tapMoving) {
                tapMoving = false;
                chronometer.resetArrowKeyPressTimes();
                chronometer.resetLanceMovementRemainders();
            }

            if (oldLastMoving == null || !oldLastMoving.equals(lastMoving)) {
                chronometer.setLastMovingTime(currentTime);
            }

            if (chronometer.getArrowKeyPressTime(index) == 0) {
                chronometer.setArrowKeyPressTime(index, currentTime);
            }

            double elapsedTime = currentTime -  chronometer.getArrowKeyPressTimes()[index];
            double elapsedMs = elapsedTime / 1_000_000.0 + chronometer.getLanceMovementRemainder(index);
            double speed = (currentTime - chronometer.getLastMovingTime()) / 1_000_000.0 >= 50 ? holdSpeed : tapSpeed;
            int minPx = calculateMinIntegerPxMovement(speed, upsSet);
            double minMsToMove = minPx * 1000.0 / speed;

            if (elapsedMs >= minMsToMove) {
                lance.updateXPosition(minPx);
                chronometer.setLanceMovementRemainder(index, elapsedMs - minMsToMove);
                chronometer.setArrowKeyPressTime(index, currentTime);
            }
        } else {
            if (lastMoving != null) {
                int index = lastMoving == 'l' ? 1 : 0;
                if (!tapMoving) {
                    tapMoving = true;
                    chronometer.setLanceMovementRemainder(index, 0);
                    chronometer.setArrowKeyPressTime(index, currentTime);
                }
                double tapTime = (currentTime - chronometer.getLastMovingTime()) / 1_000_000.0;
                if (tapTime >= 500) {
                    lance.setDirection(0);
                    tapMoving = false;
                    lastMoving = null;
                    chronometer.setLastMovingTime(0L);
                    chronometer.resetLanceMovementRemainders();
                    chronometer.resetArrowKeyPressTimes();
                } else {
                    double elapsedTime = currentTime - chronometer.getArrowKeyPressTime(index);
                    double elapsedMs = elapsedTime / 1_000_000.0 + chronometer.getLanceMovementRemainder(index);
                    int minPx = calculateMinIntegerPxMovement(tapSpeed, upsSet);
                    double minMsToMove = minPx * 1000. / tapSpeed;
                    if (elapsedMs >= minMsToMove) {
                        lance.updateXPosition(minPx);
                        chronometer.setLanceMovementRemainder(index, elapsedMs - minMsToMove);
                        chronometer.setArrowKeyPressTime(index, currentTime);
                    }
                }
            }
        }
    }

    private void handleRotationLogic(boolean keyPressed, double angularSpeed, int upsSet) {
        Lance lance = getLance();
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed, upsSet);
            lance.incrementRotationAngle(angularChange);
        }
    }

    private void handleSteadyStateLogic(boolean keyPressed, double angularSpeed, int upsSet) {
        Lance lance = getLance();
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed, upsSet);
            lance.returnToHorizontalState(angularChange);
        }
    }

    private void handleFireballLogic(int upsSet) {
        FireBall fb = getFireBall();
        if (!fb.isMoving()) {
            if (KeyboardHandler.buttonWPressed) {
                fb.launchFireball();
            } else {
                fb.stickToLance(getLance());
            }
        }
        // FIXME assumes this is called UPS_SET times per second
        fb.move(fb.getDx() / upsSet, fb.getDy() / upsSet);
    }

    private void handleCollisionLogic(long currentTime) {
        Lance lance = getLance();
        if (lance.canCollide(currentTime)) {
            if (CollisionHandler.checkFireBallEntityCollisions(getFireBall(), lance)) {
                lance.setLastCollisionTimeInMillis(currentTime / 1e6);
            }
        }

        CollisionHandler.checkFireBallBorderCollisions(getFireBall(), LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());

        List<Barrier> barriers = getBarriers();
        CollisionHandler.checkFireballBarriersCollisions(getFireBall(), barriers);

        for (Barrier barrier : barriers.stream().toList()) {
            if (barrier.isDead()) {
                if (barrier instanceof ExplosiveBarrier b) {
                    b.dropRemains();
                }
                barriers.remove(barrier);
            }
        }
    }

    private void handleBarriersMovement(long currentTime, int upsSet) {
        for (Barrier barrier : getBarriers()) {
            if (barrier.getMovementStrategy() != null) {
                // If the barrier moved with 0.2 probability and stopped with 0.8, the barriers would stop a lot
                // with 1s intervals. However, if they moved endlessly after rolling <= 0.2, a lot of them would
                // start to move at the same time. So, I decided to increase testing time to 3s and also added
                // stopping with 0.2 probability for moving barriers. This is of course subject to change.
                if (!barrier.isMoving() && currentTime - barrier.getLastDiceRollTime() >= 3_000_000_000L) {
                    barrier.tryMove(currentTime);
                } else if (barrier.isMoving() && currentTime - barrier.getLastDiceRollTime() >= 3_000_000_000L) {
                    barrier.tryStop(currentTime);
                }
                barrier.handleCloseCalls(getBarriers());
                barrier.move(barrier.getSpeed() / upsSet);
            }
        }

        List<Remain> remains = getRemains();
        for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {
            remain.move(remain.getSpeed() / upsSet);
            if (remain.getYPosition() > LanceOfDestiny.getScreenHeight()) {
                remains.remove(remain);
            }
            //TODO: handle collision with lance
        }
    }

    private double calculateAngularChangePerUpdate(double angularSpeed, int upsSet) {
        return angularSpeed * getMsPerUpdate(upsSet) / 1000.0;
    }

    private int calculateMinIntegerPxMovement(double speed, int upsSet) {
        double speedInMs = speed / 1000.0;
        double onePxMs = 1.0 / speedInMs;
        if (onePxMs >= getMsPerUpdate(upsSet)) {
            return 1;
        } else {
            double pxPerUpdate = getMsPerUpdate(upsSet) / onePxMs;
            return ((int) pxPerUpdate) + 1;
        }
    }

    private double getMsPerUpdate(int upsSet) {
        return 1000.0 / upsSet;
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
