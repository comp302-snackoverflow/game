package tr.edu.ku.comp302;


import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.BuildPanelModel;
import tr.edu.ku.comp302.ui.view.BarrierView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App 
{
  
    public static void main(String[] args)
    {
        /*
        double w = 1280,
               h = 800;
        Lance lance = new Lance(576, 600, w, h);
        LanceView lanceView = new LanceView(lance);
        FireBall fireBall = new FireBall(630, 500, w, h);
        FireBallView fireBallView = new FireBallView(fireBall);
        Level level = new Level();
        LevelPanel levelPanel = new LevelPanel(level, lanceView, fireBallView, generateBarriers(w, h));
        new LanceOfDestiny(levelPanel);
         */

        MainFrame frame = MainFrame.createMainFrame();
        frame.showLevelPanel();
        frame.setVisible(true);

        
    }

   
    /* 
        public static List<BarrierView> generateBarriers(double w, double h) {
        // TODO: in MainFrame class, prepareLevelPanel method,
        //  the constructor of levelPanel needs List<BarrierView>
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
        for(BarrierView bar: barriers){
            System.out.println(bar);
        }
        return barriers;
    }*/

    public static List<BarrierView> generateBarriers(double w, double h, BuildPanelModel randomModel ) {
        // TODO: in MainFrame class, prepareLevelPanel method,
        //  the constructor of levelPanel needs List<BarrierView>
        List<BarrierView> barriers = new ArrayList<>();
        int x_interval = (int) w/52; // interval between the barriers
        ArrayList<Double> x_intervals = new ArrayList<>();
        ArrayList<Double> y_intervals = new ArrayList<>();
        HashMap<List<Double>, BarrierView> newMap = new HashMap<>();

        for (double r = x_interval; r < w ; r += x_interval + w/50) { // x position
            x_intervals.add(r);
        }
        for (double c = 0; c < h/2; c += 20+ ((h / 2)-80)/5)  {//y position
            y_intervals.add(c);
        } // TODO: space between the barriers depend on the screen size.
        x_intervals.removeLast();

        //this map takes 125 barriers fix this issue it is not compatible with the preivous game logic 

        newMap = randomModel.generateRandomMap(x_intervals, y_intervals, 85,15,0 ,25); 
        //the number of gift barriers at the start were chosen to be 0 for now since they were not implemented 
        //Numbers will be changed 
        int barrierCount= 0;
        for (Map.Entry<List<Double>, BarrierView> entry : newMap.entrySet()) {
            barriers.add(entry.getValue());
            barrierCount++;
        }
        System.out.println(x_intervals);
        System.out.println("//////////");
        System.out.println(y_intervals);
        System.out.println(barrierCount);
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