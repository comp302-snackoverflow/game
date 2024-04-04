package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.handler.LevelInitializer;

/**
 * LanceOfDestiny
 */
public class LanceOfDestiny {


    //declaring the instances of this class
    Level level;
    Player player;

    public LanceOfDestiny(){
        
        level = LevelInitializer.initilizeEmtpyLevel();
        
    }





    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    
}