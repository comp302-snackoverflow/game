package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.chrono.Chronometer;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.GiftBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.HollowBarrier;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.lanceofdestiny.state.GameState;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.view.View;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;


public class LevelHandler {
    private static final View fireBallView = View.of(View.FIREBALL);
    private static View currentFireBallView = View.of(View.FIREBALL);
    private static final View overwhelmedFireBallView = View.of(View.OVERWHELMED_FIREBALL);
    private static final View lanceView = View.of(View.LANCE);
    private static final View remainView = View.of(View.REMAIN);
    private static final Logger logger = LogManager.getLogger(LevelHandler.class);
    private final BarrierRenderer barrierRenderer = new BarrierRenderer();
    private static final View hexView = View.of(View.HEX);
    private static final View spellBoxView = View.of(View.SPELL_BOX);
    private Level level;
    private SpellHandler spellHandler;
    private LevelPanel levelPanel;
    private long lastHexCreationTime = 0;

    private Character lastMoving;
    private boolean tapMoving;

    public LevelHandler(Level level ) {
        this.level = level;
        spellHandler = new SpellHandler(this);
    }


    public void resizeLanceImage() {
        Lance lance = getLance();
        lanceView.resizeImage((int) lance.getLength(), (int) lance.getThickness());
        tapMoving = false;
        lastMoving = null;
    }

    public void resizeFireBallImage() {
        FireBall fireBall = getFireBall();
        if(fireBall.isOverwhelming()){
            currentFireBallView = overwhelmedFireBallView;
        }
        else{
            currentFireBallView = fireBallView;
        }
        currentFireBallView.resizeImage(fireBall.getSize(), fireBall.getSize());
    }

    public void resizeBarrierImages() {
        barrierRenderer.resizeBarrierImages(getBarriers());
    }

    public void resizeSpellBoxImage(){
        if (!getSpellBoxes().isEmpty()) {
            spellBoxView.resizeImage(level.getSpellBoxes().getFirst().getSize(), level.getSpellBoxes().getFirst().getSize());
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
        g.drawImage(currentFireBallView.getImage(), (int) fireBall.getXPosition(), (int) fireBall.getYPosition(), null);
        // uncomment the below line to see FireBall hit box
        // g.drawRect((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
    }

    public void renderBarriers(Graphics g) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object cannot be null.");
        }
        if (getBarriers() == null) {
            throw new IllegalStateException("Barriers list cannot be null.");
        }
        barrierRenderer.renderBarriers(g, getBarriers());
    }

    public void renderSpellBox(Graphics g){
        List<SpellBox> spellBoxes = level.getSpellBoxes();

        // Output the number of remains for debugging purposes
        //System.out.println(remains.size());

        // Iterate through the remains and render those that are marked as dropped
        for (SpellBox spellBox : spellBoxes.stream().filter(SpellBox::isDropped).toList()) {
            renderSpellBoxView(g, spellBox);
        }
    }

    public void renderRemains(Graphics g) {
        List<Remain> remains = level.getRemains();
        for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {
            renderRemainView(g, remain);
        }
    }

    public void renderHexes(Graphics g) {
        // Retrieve the hexes from the current level
        List<Hex> hexes = level.getHexes();
        if (hexes != null){
            for (Hex hex : hexes) {
                g.drawImage(hexView.getImage(), (int) hex.getXPosition(), (int) hex.getYPosition(), null);
            }
        }
    }

    private void renderRemainView(Graphics g, Remain remain) {
        g.drawImage(remainView.getImage(), (int) remain.getXPosition(), (int) remain.getYPosition(), null);
    }

    public void handleGameLogic(long currentTime, Chronometer chronometer, int upsSet) {
        Lance lance = getLance();
        boolean moveLeft = KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed;
        boolean moveRight = KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed;
        boolean rotateCCW = KeyboardHandler.buttonAPressed && !KeyboardHandler.buttonDPressed;
        boolean rotateCW = KeyboardHandler.buttonDPressed && !KeyboardHandler.buttonAPressed;
        double holdSpeed = lance.getSpeedWithHold();
        double tapSpeed = lance.getSpeedWithTap();

        handleLanceMovement(moveLeft, moveRight, tapSpeed, holdSpeed, chronometer, upsSet);
        handleRotationLogic(rotateCCW, -Lance.rotationSpeed, upsSet);
        handleRotationLogic(rotateCW, Lance.rotationSpeed, upsSet);
        handleSteadyStateLogic(!rotateCCW && !rotateCW, Lance.horizontalRecoverySpeed, upsSet);

        handleFireballLogic(upsSet);

        handleBarriersMovement(currentTime, upsSet);

        handleCollisionLogic(currentTime, chronometer);

        handleHexMovement(upsSet);

        handleChanceReductionLogic();
        handleRemainLogic(upsSet);
        handleSpellBoxLogic();

        handleYmir();
        updateTimeInSeconds(chronometer);
        updateSpells();

        createHex(currentTime / 1000000);

    }

    private void updateTimeInSeconds(Chronometer chronometer){
        level.updateTimeInSeconds((long) (chronometer.getCurrentTime() / 1e6));
    }

    private void handleScoreLogic(Chronometer chronometer) {
        // FIXME: chronometer's elapsed time must be saved into DB
        int score = getScore() +  300 / (level.getSecondsPassed() + 1);
        level.setScore(score);
        // newScore = oldScore + 300 / (currentTime - gameStartingTime) //TODO: is gameStartingTime needed ? Ask this to meriç/mert/ömer.
    }

    // Warning: DO NOT try to make this method clean. You will most likely fail.
    // Let's spend our time in more valuable stuff like writing actually useful code
    // instead of trying to invent key tap event in Swing.
    // Just check for bugs and leave it be.
    // Copilot's thoughts about this function: "I'm not sure what you're trying to do here."
    // (It couldn't even suggest any reasonable code for this)
    private void handleLanceMovement(boolean leftPressed, boolean rightPressed, double tapSpeed, double holdSpeed, Chronometer chronometer, int upsSet) {
        Lance lance = getLance();
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

    private void handleCollisionLogic(long currentTime, Chronometer chronometer) {
        Lance lance = getLance();
        if (lance.canCollide(currentTime)) {
            if (CollisionHandler.checkFireBallEntityCollisions(getFireBall(), lance)) {
                lance.setLastCollisionTimeInMillis(currentTime / 1e6);
            }
        }

        CollisionHandler.checkFireBallBorderCollisions(getFireBall(), LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        CollisionHandler.handleHexCollision(getHexes(), getBarriers());

        List<Barrier> barriers = getBarriers();
        CollisionHandler.checkFireballBarriersCollisions(getFireBall(), barriers);

        for (Barrier barrier : barriers.stream().toList()) {
            if (barrier.isDead()) {
                if (!(barrier instanceof HollowBarrier)){
                    handleScoreLogic(chronometer);
                }
                if (barrier instanceof ExplosiveBarrier b) {
                    b.dropRemains();
                }
                if (barrier instanceof GiftBarrier b) {
                    b.dropSpellBox();
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
    }

    private void handleRemainLogic(int upsSet) {
        List<Remain> remains = getRemains();
        for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {
            remain.move(remain.getSpeed() / upsSet);
            if (remain.getYPosition() > LanceOfDestiny.getScreenHeight()) {
                remains.remove(remain);
            } else if (CollisionHandler.checkRemainLanceCollisions(getLance(), remain)) {
                SoundHandler.playRemainHitSound();
                decreaseChances();
                remains.remove(remain);
            }
        }
    }

    private void handleSpellBoxLogic() {
        List<SpellBox> spellBoxes = getSpellBoxes();
        for (SpellBox spellBox : spellBoxes.stream().filter(SpellBox::isDropped).toList()) {
            spellBox.move();

            if (CollisionHandler.checkSpellBoxLanceCollisions(getLance(), spellBox)){
                SoundHandler.playGiftSound();
                spellBoxes.remove(spellBox);
                collectSpell(spellBox.getSpell());
            }

            if (spellBox.getYPosition() > LanceOfDestiny.getScreenHeight()) {
                spellBoxes.remove(spellBox);
            }
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

    private void handleHexMovement(int upsSet) {
        for (Hex hex: getHexes()){
            hex.move(hex.getSpeed() / upsSet);
        }
    }

    private void handleChanceReductionLogic() {
        FireBall fb = getFireBall();
        if (fb.getYPosition() + fb.getSize() >= LanceOfDestiny.getScreenHeight()) {
            decreaseChances();
            //TODO: Stop the game if the chances become 0!
            fb.stopFireball();
            fb.stickToLance(getLance());
        }
    }

    private double getMsPerUpdate(int upsSet) {
        return 1000.0 / upsSet;
    }

    private void renderSpellBoxView(Graphics g, SpellBox spellBox) {
        g.drawImage(spellBoxView.getImage(), (int) spellBox.getXPosition(), (int) spellBox.getYPosition(), null);
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
    public List<SpellBox> getSpellBoxes(){
        return level.getSpellBoxes();
    }

    /**
     * Creates a new hex at a position relative to the lance.
     */

    public void startCreatingHex() {
        spellHandler.startCreatingHex(level);
    }

    public List<Hex> getHexes(){
        return level.getHexes();
    }

    public void extendLance() {
        spellHandler.extendLance(getLance());
    }

    /**
     * Applies the Overwhelming Spell. This spell creates a massive fireball that
     * travels across the screen, damaging and destroying barriers in its path.
     */
    public void applyOverwhelmingSpell() {
        spellHandler.overwhelmingSpell(level);
    }

    public void updateSpells() {
        spellHandler.updateSpells(level);
    }

    public void createHex(long currentTime) {
        spellHandler.createHex(level, currentTime, lastHexCreationTime);
    }

    public void collectSpell(char spell){
        
        SpellBox.incrementSpellCount(spell);
        switch(spell) {
            case(SpellBox.EXTENSION_SPELL), (SpellBox.HEX_SPELL):
                level.collectSpell(spell);
                break;
            case(SpellBox.OVERWHELMING_SPELL):
                applyOverwhelmingSpell();
                break;
            case(SpellBox.FELIX_FELICIS_SPELL):
                spellHandler.felixFelicis(level);
                break;
            default:
                return;
        }
        if (levelPanel != null) {
            levelPanel.updateSpellCounts();
            levelPanel.repaint();
            levelPanel.revalidate();
        }

    }
    public void useSpell(char spell) {
        if (!level.inventoryHasSpell(spell)) return;

        if (SpellBox.decrementSpellCount(spell)) {
            switch (spell) {
                case SpellBox.EXTENSION_SPELL:
                    extendLance();
                    level.removeSpell(spell);
                    break;
                case SpellBox.HEX_SPELL:
                    startCreatingHex();
                    level.removeSpell(spell);
                    break;
                case SpellBox.OVERWHELMING_SPELL:
                    applyOverwhelmingSpell();
                    break;
                default:
                    return;
            }
            // Update the spell counts in the LevelPanel
            if (levelPanel != null) {
                levelPanel.updateSpellCounts();
            }
        } else {
            // Handle the case where there are no more spells left
            System.out.println("No more spells left of type: " + spell);
        }
    }


    public void setLevelPanel(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
    }

    public void setLastHexCreationTime(long currentTime) {
        lastHexCreationTime = currentTime;
    }

    public void handleYmir() {
        spellHandler.handleYmir(level, levelPanel);
    }

    public long getRemainingTimeForYmir() {
        return 30 - spellHandler.getYmirTime();
    }
    public SpellHandler getSpellHandler() {
        return spellHandler;
    }

    public int getScore(){
        return level.getScore();
    }

    private void decreaseChances() {
        level.decreaseChances();
    }


    public boolean isFinished() {
        if (level.getChances() == 0 || level.getBarriers().isEmpty())   {
            LanceOfDestiny.setCurrentGameState(GameState.PAUSE);
            return true;
        }

        return false;
    }

    public boolean isWon(){
        if (level.getBarriers().isEmpty()) return true;

        return false;
    }
}
