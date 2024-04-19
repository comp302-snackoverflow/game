package tr.edu.ku.comp302;


import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Barriers.*;
import tr.edu.ku.comp302.domain.entity.Barriers.SimpleBarrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.view.BarrierView;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App 
{
    public static void main(String[] args)
    {
        double w = 1280,
               h = 800;
        Lance lance = new Lance(576, 600, w, h);
        LanceView lanceView = new LanceView(lance);
        FireBall fireBall = new FireBall(630, 500, w, h);
        FireBallView fireBallView = new FireBallView(fireBall);
        Level level = new Level();
        LevelPanel levelPanel = new LevelPanel(level, lanceView, fireBallView, generateBarriers(w, h));
        new LanceOfDestiny(levelPanel);
    }

    // xPositionStart : int r = (int) w/52
    //x positionInterval : (int) w/52

    
    public static List<BarrierView> generateBarriers(double w, double h) {
        List<BarrierView> barriers = new ArrayList<>();
        int x_interval = (int) w/52; // interval between the barriers
        int queue = 0;
        for (int r = x_interval; r < w ; r += x_interval + (int) w/50) { // x position
            for (int c = ( ((int)h / 2)-80)/5; c < (int)h/2; c += 20+ (((int)h / 2)-80)/5)  {//y position
                if (queue == 0) {
                    barriers.add(new BarrierView(new SimpleBarrier(r, c, w, h)));
                    queue = 1;
                }
                else if (queue == 1){
                    barriers.add(new BarrierView(new FirmBarrier(r, c, w, h)));
                    queue = 2;
                }

                else {
                    barriers.add(new BarrierView(new ExplosiveBarrier(r, c, w, h)));
                    queue = 0;
                }
            } // TODO: space between the barriers depend on the screen size.
        }
        return barriers;
    }

    //Generate single barrier for test purposes 


    /*public static List<BarrierView> generateBarriers(double w, double h) {
            List<BarrierView> barriers = new ArrayList<>();
            int x_interval = (int) w/52; // interval between the barriers
            int queue = 0;
        
            //barriers.add(new BarrierView(new ExplosiveBarrier(w/2, h/2, w, h)));
            barriers.add(new BarrierView(new SimpleBarrier(w/2, h/2, w, h)));
                    
            return barriers;
        }*/
 
    

    
}
