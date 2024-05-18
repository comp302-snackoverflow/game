package tr.edu.ku.comp302.domain.handler;


import java.util.Iterator;
import java.util.List;


import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class SpellHandler {

    LanceOfDestiny lanceOfDestiny;

    public SpellHandler(LanceOfDestiny lanceOfDestiny) {
        this.lanceOfDestiny = lanceOfDestiny;
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

    public void handleHexCollision(List<Hex> hexes, List<Barrier> barriers) {

        if(hexes == null || barriers == null) {
            throw new NullPointerException();
            
        }

        
        Iterator<Hex> hexIterator = hexes.iterator();
        while (hexIterator.hasNext()) {
            Hex currentHex = hexIterator.next();
            for (Barrier barrier : barriers) {
                if (currentHex.isCollidingWith(barrier)) {
                    hexIterator.remove();
                    barrier.decreaseHealth();

                    System.out.println("Hex collided with barrier");
                    break;
                }
            }
        }
    }


    
}
