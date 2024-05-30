package tr.edu.ku.comp302.domain.handler;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.*;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.services.threads.PausableThread;

public class SpellHandler {

    long ymirTime = 0;
    long previousTime;
    private char previous_spell_0 = '0';
    private char previous_spell_1 = '1';


    private final static char INFINITE_VOID = 'i';
    private final static char HOLLOW_BARRIER = 'h';
    private final static char DOUBLE_ACCEL = 'd';
    private long doubleAccelEndTime = 0;
    private long freezeBarriersEndTime = 0;
    private long lanceExtensionEndTime = 0;
    private long hexEndTime = 0;
    private long overwhelmingEndTime = 0;

    private Random random = new Random();
    LevelHandler levelHandler;

    public SpellHandler(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
    }


    public void extendLance(Lance lance) {
        lance.setLength(lance.getLength()*2);
        lance.setXPosition(lance.getXPosition() - lance.getLength()/4);
        levelHandler.resizeLanceImage();
        lanceExtensionEndTime = System.currentTimeMillis() + 30000;
        
    }

    public void shrinkLance(Lance lance) {
        lance.setXPosition(lance.getXPosition() + lance.getLength()/4);
        lance.setLength(lance.getLength()/2);
        levelHandler.resizeLanceImage();
    }

    public void felixFelicis(Level level) {
        level.increaseChances();
    }


    public void doubleAccel(Level level) {
        FireBall fireball = level.getFireBall();
        fireball.applyDoubleAccel();
        doubleAccelEndTime = System.currentTimeMillis() + 15000;
    }

    public void revertDoubleAccel(Level level) {
        FireBall fireball = level.getFireBall();
        fireball.revertDoubleAccel();
    }



    public void overwhelmingSpell(Level level) {
        FireBall fireBall = level.getFireBall();
        fireBall.setOverwhelming(true);
        levelHandler.resizeFireBallImage();
        overwhelmingEndTime = System.currentTimeMillis() + 30000;
    }

    public void endOverwhelmingBall(Level level){

        FireBall fireBall = level.getFireBall();
        fireBall.setOverwhelming(false);
        levelHandler.resizeFireBallImage();
    }

    public void handleYmir(Level level) {

        if (ymirTime < 30) {
            if (System.currentTimeMillis() - previousTime > 1000) {
                previousTime = System.currentTimeMillis();
                ymirTime++;

            }
        }
        else{
            applyNewSpell(level);
            SoundHandler.playLaughSound();
            previousTime = System.currentTimeMillis();
            ymirTime = 0;
        }


    }

    public void startCreatingHex(Level level) {
        hexEndTime = System.currentTimeMillis() + 30000;
    }

    public void createHex(Level level, long currentTime, long lastHexCreationTime) {
        if (currentTime < hexEndTime && (currentTime - lastHexCreationTime >= 1000)) {
            level.createHex();
            levelHandler.setLastHexCreationTime(currentTime);
        }
    }

    public char chooseYmirSpell() {
        List<Character> spells = new ArrayList<>();
        spells.add(INFINITE_VOID);
        spells.add(HOLLOW_BARRIER);
        spells.add(DOUBLE_ACCEL);

        // Remove the spell from the list if it's the same as the previous two spells
        if (previous_spell_0 == previous_spell_1) {
            spells.remove(Character.valueOf(previous_spell_1));
        }

        // Randomly select a spell from the remaining options
        char chosenSpell = spells.get(random.nextInt(spells.size()));

        // Update previous spells
        previous_spell_0 = previous_spell_1;
        previous_spell_1 = chosenSpell;

        return chosenSpell;
    }


    private void applyNewSpell(Level level) {

        char chosenSpell = chooseYmirSpell();

        switch (chosenSpell) {
            case INFINITE_VOID:
                handleFrozenBarriers(level);
                break;
            case HOLLOW_BARRIER:
                generateHollowBarriers(level.getBarriers());
                break;
            case DOUBLE_ACCEL:
                doubleAccel(level);
                break;
            default:
                break;
        }
        
    }

    public void generateHollowBarriers(List<Barrier> barriers) {
        SecureRandom secureRandom = new SecureRandom();
        int barriersToAdd = 8;

        while (barriersToAdd > 0) {
            double barrierWidth = barriers.get(0).getLength();
            int x = secureRandom.nextInt(LanceOfDestiny.getScreenWidth() - (int) barrierWidth - (int) barrierWidth / 2) + (int) barrierWidth / 2;
            int y = secureRandom.nextInt(LanceOfDestiny.getScreenHeight() / 2 - 40) + 20; // -20 barrier -20 padding

            Barrier randomBarrier = new HollowBarrier(x, y);
            randomBarrier.setLength(barrierWidth);

            // Check for collision
            if (!CollisionHandler.checkBarrierCollisionWithBarriers(randomBarrier, barriers)) {
                barriers.add(randomBarrier);
                barriersToAdd--;
            }
        }
    }

    public void handleFrozenBarriers(Level level) {

        freezeEightRandomBarriers(level);
        freezeBarriersEndTime = System.currentTimeMillis() + 15000;
    }

    public void freezeEightRandomBarriers(Level level) {
        List<Barrier> allBarriers = new ArrayList<>(level.getBarriers());
        int barrierCount = allBarriers.size();

        if (barrierCount < 8) {
            allBarriers.forEach(barrier -> barrier.freeze(true));
            return;
        }

        SecureRandom random = new SecureRandom();
        Collections.shuffle(allBarriers, random);

        List<Barrier> chosenBarriers = allBarriers.subList(0, 8);
        chosenBarriers.forEach(barrier -> barrier.freeze(true));
    }

    public void normalizeBarriers(Level level) {
        for (Barrier barrier : level.getBarriers()) {
            barrier.freeze(false);
        }
    }

    public void updateSpells(Level level) {
        long currentTime = System.currentTimeMillis();
        if (doubleAccelEndTime > 0 && currentTime >= doubleAccelEndTime) {
            revertDoubleAccel(level);
            doubleAccelEndTime = 0;
        }
        if (freezeBarriersEndTime > 0 && currentTime >= freezeBarriersEndTime) {
            normalizeBarriers(level);
            freezeBarriersEndTime = 0;
        }
        if (lanceExtensionEndTime > 0 && currentTime >= lanceExtensionEndTime) {
            shrinkLance(level.getLance());
            lanceExtensionEndTime = 0;
        }
        if (hexEndTime > 0 && currentTime >= hexEndTime) {
            hexEndTime = 0;
        }
        if (overwhelmingEndTime > 0 && currentTime >= overwhelmingEndTime) {
            endOverwhelmingBall(level);
            overwhelmingEndTime = 0;
        }
    }
    public long getYmirTime() {
        return ymirTime;
    }

}
