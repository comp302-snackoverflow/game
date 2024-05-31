package tr.edu.ku.comp302.domain.lanceofdestiny;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.GiftBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.HollowBarrier;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.handler.SoundHandler;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import java.awt.*;
import java.util.Iterator;
import java.util.List;



public class LanceOfDestiny implements Runnable {
    private final Logger logger = LogManager.getLogger(LanceOfDestiny.class);
    private LevelPanel levelPanel;  // TODO: change this when we implement more than one level
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private static int screenWidth = 1280;
    private static int screenHeight = 800;
    private double deltaUpdate = 0.0;
    private double deltaFrame = 0.0;
    private long updates = 0L;
    private long frames = 0L;
    private GameState currentGameState;
    private Thread gameThread;
    private Character lastMoving;
    private long lastMovingTime;
    private long previousTime;
    private long[] arrowKeyPressTimes = new long[2];    // [rightArrowKeyPressTime, leftArrowKeyPressTime]
    private double[] lanceMovementRemainder = new double[2];   // [remainderTotalRightArrowKey, remainderTotalLeftArrowKey]
    private boolean tapMoving;
    private LevelHandler levelHandler;
    private int secondsPassed = 0;
    private long nextTimeMS = 1;
    
    

    public LanceOfDestiny(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
        levelHandler = levelPanel.getLevelHandler();    // TODO: Ğ
        levelPanel.requestFocusInWindow();
        currentGameState = GameState.PLAYING;   // for testing purposes.
        lastMoving = null;
        lastMovingTime = 0;
        tapMoving = false;
        startGameLoop();
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        previousTime = System.nanoTime();
        // TODO: Change while loop condition
        while (true) {
            if (currentGameState.isPlaying()){
                long currentTime = System.nanoTime();
                deltaUpdate += (currentTime - previousTime) / timePerUpdate;
                deltaFrame += (currentTime - previousTime) / timePerFrame;
                update(currentTime);
                previousTime = currentTime;
            }else{  // TODO: Change this else statement whenever implemented other game states.
                try{
                    Thread.sleep(1000 / UPS_SET);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void update(long currentTime){
        boolean recall = false;
        if (deltaUpdate >= 1){
            handleGameLogic(currentTime);
            updates++;
            deltaUpdate--;
            recall = true;
        }
        if (deltaFrame >= 1){
            render();
            levelPanel.repaint();
            frames++;
            deltaFrame--;
            recall = true;
        }
        if (recall) {
            update(currentTime);
        }
    }

    private void handleGameLogic(long currentTime){
        Lance lance = levelHandler.getLance();
        boolean moveLeft = KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed;
        boolean moveRight = KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed;
        double holdSpeed = lance.getSpeedWithHold();
        double tapSpeed = lance.getSpeedWithTap();
        boolean rotateCCW = KeyboardHandler.buttonAPressed && !KeyboardHandler.buttonDPressed;
        boolean rotateCW = KeyboardHandler.buttonDPressed && !KeyboardHandler.buttonAPressed;

        handleLanceMovement(moveLeft, moveRight, arrowKeyPressTimes, currentTime, tapSpeed, holdSpeed, lanceMovementRemainder);
        handleRotationLogic(rotateCCW, -Lance.rotationSpeed);
        handleRotationLogic(rotateCW, Lance.rotationSpeed);
        handleSteadyStateLogic(!rotateCCW && !rotateCW, Lance.horizontalRecoverySpeed);

        handleFireballLogic();
        handleBarriersMovement(currentTime);
        handleCollisionLogic(currentTime);
        handleHexMovement();

        handleChanceReductionLogic();
        handleRemainLogic();
        handleSpellBoxLogic();
        handleYmir();
        handleSpellLogic();
        updateTimeInSeconds();
        updateSpells();
        levelHandler.createHex(currentTime / 1000000);
    }




    private void render(){
        int width = (int) levelPanel.getSize().getWidth();
        int height = (int) levelPanel.getSize().getHeight();
        if (screenWidth != width || screenHeight != height){
            levelPanel.setPanelSize(new Dimension(width, height));
            screenWidth = width;
            screenHeight = height;
        }
    }

    // Warning: DO NOT try to make this method clean. You will most likely fail.
    // Let's spend our time in more valuable stuff like writing actually useful code
    // instead of trying to invent key tap event in Swing.
    // Just check for bugs and leave it be.
    // Copilot's thoughts about this function: "I'm not sure what you're trying to do here."
    // (It couldn't even suggest any reasonable code for this)
    private void handleLanceMovement(boolean leftPressed, boolean rightPressed, long[] arrowKeyPressTimes, long currentTime, double tapSpeed, double holdSpeed, double[] lanceMovementRemainder) {
        Lance lance = levelPanel.getLevelHandler().getLance();
        if (leftPressed != rightPressed) {
            int index = leftPressed ? 1 : 0;
            Character oldLastMoving = lastMoving;
            lastMoving = leftPressed ? 'l' : 'r';
            lance.setDirection(leftPressed ? -1 : 1);

            if (tapMoving) {
                tapMoving = false;
                arrowKeyPressTimes[0] = 0;
                arrowKeyPressTimes[1] = 0;
                lanceMovementRemainder[0] = 0;
                lanceMovementRemainder[1] = 0;
            }

            if (oldLastMoving == null || !oldLastMoving.equals(lastMoving)) {
                lastMovingTime = currentTime;
            }

            if (arrowKeyPressTimes[index] == 0) {
                arrowKeyPressTimes[index] = currentTime;
            }

            double elapsedTime = currentTime - arrowKeyPressTimes[index];
            double elapsedMs = elapsedTime / 1_000_000.0 + lanceMovementRemainder[index];
            double speed = (currentTime - lastMovingTime) / 1_000_000.0 >= 50 ? holdSpeed : tapSpeed;
            int minPx = calculateMinIntegerPxMovement(speed);
            double minMsToMove = minPx * 1000.0 / speed;

            if (elapsedMs >= minMsToMove) {
                lance.updateXPosition(minPx);
                lanceMovementRemainder[index] = elapsedMs - minMsToMove;
                arrowKeyPressTimes[index] = System.nanoTime();
            }
        } else {
            if (lastMoving != null) {
                int index = lastMoving == 'l' ? 1 : 0;
                if (!tapMoving) {
                    tapMoving = true;
                    lanceMovementRemainder[index] = 0;
                    arrowKeyPressTimes[index] = currentTime;
                }
                double tapTime = (currentTime - lastMovingTime) / 1_000_000.0;
                if (tapTime >= 500) {
                    lance.setDirection(0);
                    tapMoving = false;
                    lastMoving = null;
                    lastMovingTime = 0;
                    lanceMovementRemainder[index] = 0;
                    arrowKeyPressTimes[index] = 0;
                } else {
                    double elapsedTime = currentTime - arrowKeyPressTimes[index];
                    double elapsedMs = elapsedTime / 1_000_000.0 + lanceMovementRemainder[index];
                    int minPx = calculateMinIntegerPxMovement(tapSpeed);
                    double minMsToMove = minPx * 1000. / tapSpeed;
                    if (elapsedMs >= minMsToMove) {
                        lance.updateXPosition(minPx);
                        lanceMovementRemainder[index] = elapsedMs - minMsToMove;
                        arrowKeyPressTimes[index] = System.nanoTime();
                    }
                }
            }
        }
    }

    private void handleYmir() {
        levelHandler.handleYmir();


    }

    private void handleHexMovement() {
        for (Hex hex: levelHandler.getHexs()){
            hex.move();
        }
    }
    
    private void handleRotationLogic(boolean keyPressed, double angularSpeed) {
        Lance lance = levelPanel.getLevelHandler().getLance();
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            lance.incrementRotationAngle(angularChange);
        }
    }

    private void handleSteadyStateLogic(boolean keyPressed, double angularSpeed) {
        Lance lance = levelPanel.getLevelHandler().getLance();
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            lance.returnToHorizontalState(angularChange);
        }
    }

    private void handleFireballLogic() {
        FireBall fb = levelPanel.getLevelHandler().getFireBall();
        if (!fb.isMoving()) {
            if (KeyboardHandler.buttonWPressed) {
                fb.launchFireball();
            } else {
                fb.stickToLance(levelHandler.getLance());
            }
        }

        fb.move();
    }

    /**
     * Handles the logic for collision checking and handling dead barriers.
     *
     * @param currentTime the current time in milliseconds
     * @return void
     */
    private void handleCollisionLogic(long currentTime) {
        List<Barrier> barriers = levelHandler.getBarriers();
        CollisionHandler.checkFireBallEntityCollisions(levelHandler.getFireBall(), levelHandler.getLance());
        CollisionHandler.checkFireBallBorderCollisions(levelHandler.getFireBall(), screenWidth, screenHeight);
        CollisionHandler.handleHexCollision(levelHandler.getHexs(), barriers);
        CollisionHandler.checkFireballBarriersCollisions(levelHandler.getFireBall(), barriers);

        Iterator<Barrier> iterator = barriers.iterator();
        while (iterator.hasNext()) {
            Barrier barrier = iterator.next();
            if (barrier.isDead()) {
                if (! (barrier instanceof HollowBarrier)){
                    handleScoreLogic(currentTime);
                }
                if (barrier instanceof ExplosiveBarrier b) {
                    b.dropRemains();
                }
                if (barrier instanceof GiftBarrier b) {
                    b.dropSpellBox();
                }
                iterator.remove();
            }
        }
    }

    private void handleBarriersMovement(long currentTime) {
        // FIXME: @ayazici21 please fix the below errors tysm - Meric
        for(Barrier barrier : levelHandler.getBarriers()){
            if(barrier.getMovementStrategy() != null){
                // If the barrier moved with 0.2 probability and stopped with 0.8, the barriers would stop a lot
                // with 1s intervals. However, if they moved endlessly after rolling <= 0.2, a lot of them would
                // start to move at the same time. So, I decided to increase testing time to 3s and also added
                // stopping with 0.2 probability for moving barriers. This is of course subject to change.
                if (!barrier.isMoving() && currentTime - barrier.getLastDiceRollTime() >= 3_000_000_000L) {
                    barrier.tryMove(currentTime);
                } else if (barrier.isMoving() && currentTime - barrier.getLastDiceRollTime() >= 3_000_000_000L) {
                    barrier.tryStop(currentTime);
                }
                barrier.handleCloseCalls(levelHandler.getBarriers());
                barrier.move(barrier.getSpeed() / UPS_SET);
            }
        }

        

    }


    private void handleSpellBoxLogic() {

        List<SpellBox> spellBoxes = levelHandler.getSpellBoxes();
        for (SpellBox spellBox : spellBoxes.stream().filter(SpellBox::isDropped).toList()) {

            spellBox.move();

            if (CollisionHandler.checkSpellBoxLanceCollisions(levelHandler.getLance(), spellBox)){
                SoundHandler.playGiftSound();
                spellBoxes.remove(spellBox);
                levelHandler.collectSpell(spellBox.getSpell());
            }

            if (spellBox.getYPosition() > screenHeight) {
                spellBoxes.remove(spellBox);
            }
        }
        
    }

    private void handleChanceReductionLogic() {
        FireBall fb = levelHandler.getFireBall();
        if (fb.getYPosition() + fb.getSize() >= screenHeight) {
            levelHandler.getLevel().decreaseChances();
            //TODO: Stop the game if the chances become 0!
            fb.stopFireball();
            fb.stickToLance(levelHandler.getLance());
        }
    }

    private void handleRemainLogic() {

        List<Remain> remains = levelHandler.getRemains();
        for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {

            remain.move();
            if (remain.getYPosition() > screenHeight) {
                remains.remove(remain);
            }
        }

        for (Iterator<Remain> iterator = remains.iterator(); iterator.hasNext(); ) {
            Remain remain = iterator.next();
            if (CollisionHandler.checkRemainLanceCollisions(levelHandler.getLance(), remain)) {
                SoundHandler.playRemainHitSound();
                levelHandler.getLevel().decreaseChances();
                iterator.remove();
            }
        }
    }

    //TODO: Ask mert/meriç/ömer on how to implement the time. Since it's in miliseconds, the score is just zero all the time.
    private void handleScoreLogic(long currentTime) {
        Level level = levelHandler.getLevel();
        long newScore = level.getScore() + 300 / (secondsPassed + 1);
        level.setScore((int)newScore);
        // newScore = oldScore + 300 / (currentTime - gameStartingTime) //TODO: is gameStartingTime needed ? Ask this to meriç/mert/ömer.
    }


    // Cleaned up the code by using a list to store threads to remove, and then removing all of them at once.
    // Removed the unnecessary comments. Also improved the readability of the code by breaking it up into smaller statements.

    private double calculateAngularChangePerUpdate(double angularSpeed) {
        return angularSpeed * getMsPerUpdate() / 1000.0;
    }

    private int calculateMinIntegerPxMovement(double speed) {
        double speedInMs = speed / 1000.0;
        double onePxMs = 1.0 / speedInMs;
        if (onePxMs >= getMsPerUpdate()) {
            return 1;
        } else {
            double pxPerUpdate = getMsPerUpdate() / onePxMs;
            return ((int) pxPerUpdate) + 1;
        }
    }

    private void handleSpellLogic() {
        if (KeyboardHandler.buttonHPressed) {
            levelHandler.useSpell(SpellBox.HEX_SPELL);
        }
        if (KeyboardHandler.buttonTPressed) {
            levelHandler.useSpell(SpellBox.EXTENSION_SPELL);
        }
    }

    private void updateTimeInSeconds(){
       
        if (System.currentTimeMillis() >= nextTimeMS){
            secondsPassed+=1;
            nextTimeMS = System.currentTimeMillis() + 1000;
        }
    }

    private double getMsPerUpdate() {
        return 1000.0 / UPS_SET;
    }



    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        LanceOfDestiny.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        LanceOfDestiny.screenHeight = screenHeight;
    }

    public static void setCurrentGameState(GameState gameState) {
        GameState.state = gameState;
    }

    private void updateSpells() {
        levelHandler.updateSpells();
    }

    


}
