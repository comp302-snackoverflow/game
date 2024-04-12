package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.view.BarrierView;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class LevelPanel extends JPanel {
    private Level level;
    private LanceView lanceView;
    private FireBallView fireBallView;
    private List<BarrierView> barriers;

    public LevelPanel(Level level, LanceView lanceView, FireBallView fireBallView, List<BarrierView> barriers) {
        this.level = level;
        this.lanceView = lanceView;
        this.fireBallView = fireBallView;
        this.barriers = barriers;
        addKeyListener(new KeyboardHandler());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        lanceView.render(g);
        fireBallView.render(g);
        IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g)); // Below throws an exception.
        // barriers.forEach(barrier -> barrier.render(g));
    }
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        lanceView.getLance().setL(size.getWidth() / 10.0);
        lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
                (int) lanceView.getLance().getLength(),
                (int) lanceView.getLance().getThickness())) ;
        fireBallView.setFireBallImage(ImageHandler.resizeImage(fireBallView.getFireBallImage(),
                fireBallView.getFireBall().getSize(),
                fireBallView.getFireBall().getSize()));
        barriers.forEach(barrierView -> {
            barrierView.getBarrier().setL(size.getWidth() / 10.0);
            barrierView.setBarrierImage(ImageHandler.resizeImage(
                    barrierView.getBarrierImage(),
                    (int) barrierView.getBarrier().getLength(),
                    (int) barrierView.getBarrier().getThickness()
            ));
        });
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LanceView getLanceView() {
        return lanceView;
    }

    public void setLanceView(LanceView lanceView) {
        this.lanceView = lanceView;
    }
    public FireBallView getFireBallView() {
        return fireBallView;
    }

    public void setFireBallView(FireBallView fireBallView) {
        this.fireBallView = fireBallView;
    }
    public List<BarrierView> getBarrierViews() {
        return barriers;
    }
}
