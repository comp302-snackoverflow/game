package tr.edu.ku.comp302.domain.handler;


import java.util.Iterator;
import java.util.List;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;

public class SpellHandler {

    

    public SpellHandler() {
        
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
        level.getFireBall().halfSpeed();
    }


    public void overwhelmingSpell(Level level) {

        FireBall fireBall = level.getFireBall();

        fireBall.setOverwhelming(true);
        

    }

    public void endOverwhelmingBall(Level level){

        FireBall fireBall = level.getFireBall();

        fireBall.setOverwhelming(false);
        
    }


    
}
