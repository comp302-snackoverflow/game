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

        MainFrame frame = MainFrame.createMainFrame();
        frame.showLevelPanel();
        frame.setVisible(true);
    }

    public static List<BarrierView> generateBarriers(double w, double h, BuildPanelModel randomModel ) {
        // TODO: in MainFrame class, prepareLevelPanel method,
        //  the constructor of levelPanel needs List<BarrierView>
        List<BarrierView> barriers = new ArrayList<>();
        ArrayList<Double> x_intervals = new ArrayList<>();
        ArrayList<Double> y_intervals = new ArrayList<>();
        HashMap<List<Double>, BarrierView> newMap = new HashMap<>();

        int x_interval = (int) w/52; 
        double y_interval = ((h/2)-120)/7;

        for (double r = x_interval/2; r <= w-x_interval/2 +1 ; r += x_interval + w/50) {
            x_intervals.add(r);
        }

        for (double c = y_interval/2; c <= h/2 - y_interval/2; c += 20 + y_interval)  {
            y_intervals.add(c);
        } 
        x_intervals.removeLast();
        y_intervals.removeLast();

        //the number of gift barriers at the start were chosen to be 0 for now since they were not implemented
        newMap = randomModel.generateRandomMap(x_intervals, y_intervals, 90,25,0 ,35);
        
        for (Map.Entry<List<Double>, BarrierView> entry : newMap.entrySet()) {
            barriers.add(entry.getValue());
        }

        return barriers;
    }


}