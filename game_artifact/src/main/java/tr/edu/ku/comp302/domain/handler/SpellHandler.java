package tr.edu.ku.comp302.domain.handler;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.*;
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


    private Random random = new Random();
    LevelHandler levelHandler;

    public SpellHandler(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
    }


    public void extendLance(Lance lance) {
        lance.setLength(lance.getLength()*2);
        //use new length
        lance.setXPosition(lance.getXPosition() - lance.getLength()/4);
        
    }

    public void shrinkLance(Lance lance) {
        //use extended length
        lance.setXPosition(lance.getXPosition() + lance.getLength()/4);

        lance.setLength(lance.getLength()/2);
        
        
    }

    public void felixFelicis(Level level) {
        level.increaseChances();
    }

    //TODO: Note to Eren: This spell lasts for 15 seconds.
    public void doubleAccel(Level level) {
        FireBall fireball = level.getFireBall();

        fireball.halfAccel();

        Runnable normalizeFireballSpeedTask = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                fireball.doubleAccel();
            }
        };

        PausableThread pausableThread = new PausableThread(() -> {}, normalizeFireballSpeedTask, 5);

        levelHandler.getPausableThreads().add(pausableThread);
    }


    


    public void overwhelmingSpell(Level level) {

        FireBall fireBall = level.getFireBall();

        fireBall.setOverwhelming(true);
        

    }

    public void endOverwhelmingBall(Level level){

        FireBall fireBall = level.getFireBall();

        fireBall.setOverwhelming(false);
        
    }

       

    public void handleYmir(Level level) {

        if (ymirTime < 10) {
            if (System.currentTimeMillis() - previousTime > 1000) {
                previousTime = System.currentTimeMillis();
                ymirTime++;
                
                System.out.println("fireball speed: " + level.getFireBall().getSpeed());
            }
        }
        else{
            applyNewSpell(level);
            previousTime = System.currentTimeMillis();
            ymirTime = 0;
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
                freezeEightRandomBarriers(level);
                break;
            case HOLLOW_BARRIER:
                generateHollowBarriers(level);
                break;
            case DOUBLE_ACCEL:
                doubleAccel(level);
                break;
            default:
                break;
        }
        
    }



    /**
     * Generates 8 random hollow barriers in the game level
     *
     * @param level the game level
     */
    public void generateHollowBarriers(Level level) {
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 8; i++) {
            double barrierWidth = level.getBarriers().get(0).getLength();
            int buildSectionWidth = LanceOfDestiny.getScreenWidth() / 2;
            int buildSectionHeight = LanceOfDestiny.getScreenHeight() / 2; // TODO: Change ratio later to a constant value

            // Generate random barrier position within the build area
            int x = secureRandom.nextInt(buildSectionWidth - (int) barrierWidth - (int) barrierWidth / 2) + (int) barrierWidth / 2;
            int y = secureRandom.nextInt(buildSectionHeight - 40) + 20; // -20 barrier -20 padding

            // Create new barrier
            Barrier randomBarrier = new HollowBarrier(x, y);
            randomBarrier.setLength(barrierWidth);

            // Check for collision
            if (!hasCollision(level.getBarriers(), randomBarrier)) {
                level.getBarriers().add(randomBarrier);
            }
        }
    }

    /**
     * Checks if a barrier collides with any other barrier in the list
     *
     * @param barriers the list of barriers
     * @param barrier  the barrier to check
     * @return true if there is a collision, false if not
     */
    private boolean hasCollision(List<Barrier> barriers, Barrier barrier) {
        for (Barrier b : barriers) {
            if (b != barrier && b.getBoundingBox().intersects(barrier.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public void handleFrozenBarriers(Level level) {


        freezeEightRandomBarriers(level);
        
        Runnable normalizeBarriersTast = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                
                normalizeBarriers(level);

            }
        };

        PausableThread pausableThread = new PausableThread(() -> {}, normalizeBarriersTast, 5);
        levelHandler.getPausableThreads().add(pausableThread);
    }

    public List<Barrier> freezeEightRandomBarriers(Level level) {
        List<Barrier> allBarriers = Collections.synchronizedList(new ArrayList<>(level.getBarriers() ));
        int barrierCount = allBarriers.size();

        if (barrierCount < 8) {
            for (Barrier barrier : allBarriers) {
                barrier.setFrozen(true);
            }
            return new ArrayList<>(allBarriers);
        }

        Set<Integer> chosenIndices = new HashSet<>();
        List<Barrier> chosen = new ArrayList<>();
        Random random = new Random();
        while (chosen.size() < 8) {
            int index = random.nextInt(barrierCount);
            if (!chosenIndices.contains(index)) {
                chosenIndices.add(index);
                chosen.add(allBarriers.get(index));
                chosen.get(chosen.size() - 1).setFrozen(true);
            }
        }

        return chosen;
    }

    public void normalizeBarriers(Level level) {
        for (Barrier barrier : level.getBarriers()) {
            barrier.setFrozen(false);
        }
    }
}
