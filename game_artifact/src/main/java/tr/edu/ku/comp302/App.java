package tr.edu.ku.comp302;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.BuildPanelModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        MainFrame frame = MainFrame.createMainFrame();
        frame.showMainMenuPanel();
        frame.setVisible(true);
    }

    public static List<Barrier> generateBarriers(double w, double h, BuildPanelModel randomModel) {
        // TODO: in MainFrame class, prepareLevelPanel method,
        //  the constructor of levelPanel needs List<BarrierView>
        List<Barrier> barriers = new ArrayList<>();
        ArrayList<Double> x_intervals = new ArrayList<>();
        ArrayList<Double> y_intervals = new ArrayList<>();

        int x_interval = (int) (w / 52);
        int y_interval = (int) ((h / 2 - 120) / 7);

        for (double r = x_interval >> 1; r <= w - (x_interval >> 1) + 1; r += x_interval + w / 50) {
            x_intervals.add(r);
        }

        for (double c = y_interval >> 1; c <= h / 2 - (y_interval >> 1); c += 20 + y_interval) {
            y_intervals.add(c);
        }
        x_intervals.removeLast();
        y_intervals.removeLast();

        //the number of gift barriers at the start were chosen to be 0 for now since they were not implemented
        HashMap<List<Double>, Barrier> newMap = randomModel.generateRandomMap(x_intervals, y_intervals, 90, 25, 0, 35);

        for (Map.Entry<List<Double>, Barrier> entry : newMap.entrySet()) {
            barriers.add(entry.getValue());
        }

        return barriers;
    }


}