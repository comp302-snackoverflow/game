package tr.edu.ku.comp302;


import tr.edu.ku.comp302.domain.entity.Barrier;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
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

    public static List<BarrierView> generateBarriers(double w, double h) {
        List<BarrierView> barriers = new ArrayList<>();
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 30; c++) {
                barriers.add(new BarrierView(new Barrier(10 + c * 30, 10 + r * 25, w, h)));
            } // TODO: space between the barriers depend on the screen size.
        }
        return barriers;
    }
}
