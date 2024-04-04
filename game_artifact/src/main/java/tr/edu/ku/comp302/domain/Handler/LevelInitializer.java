package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.lanceofdestiny.*;

/**
 * This class is to store the methods that can create level objects from builded maps or from the database
 */
public class LevelInitializer {


    public static Level initilizeEmtpyLevel(){

        
        Level output = new Level();
        GameMap gameMap = new GameMap();
        output.setCurrentMap(gameMap);

        Lance lance = new Lance(100, 100, 100, 20);
        gameMap.setCurrentLance(lance);
        return output;
    }
    
}
