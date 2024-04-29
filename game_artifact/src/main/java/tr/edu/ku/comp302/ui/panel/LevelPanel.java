package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.GameState;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.view.BarrierView;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;
import tr.edu.ku.comp302.ui.view.RemainsView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class LevelPanel extends JPanel {
    private Level level;
    private LanceView lanceView;
    private FireBallView fireBallView;
    private List<BarrierView> barriers;

    private MainFrame mainFrame;
    private JButton pauseButton;

    private List <RemainsView> remains = new ArrayList<>();

    public LevelPanel(Level level, LanceView lanceView, FireBallView fireBallView, List<BarrierView> barriers, MainFrame mainFrame) {
        this.level = level;
        this.lanceView = lanceView;
        this.fireBallView = fireBallView;
        this.barriers = barriers;
        this.mainFrame = mainFrame;
        addKeyListener(new KeyboardHandler());
        setFocusable(true);
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(e -> {
                LanceOfDestiny.setCurrentGameState(GameState.PAUSE_MENU);
                mainFrame.showPausePanel();
        });
        pauseButton.setBounds(0,0, 10, 20);
        this.add(pauseButton);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        lanceView.render(g);
        fireBallView.render(g);
        for (int i = 0 ; i < barriers.size(); i++) {
            barriers.get(i).render(g);
        }

        for (int i = 0; i < remains.size(); i++) {
            remains.get(i).render(g);
        }
//        IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g)); // Below throws an exception.
//        IntStream.range(0, remains.size()).forEach(i-> remains.get(i).render(g));
        // barriers.forEach(barrier -> barrier.render(g));
    }

    // TODO: Handle this method later.
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        lanceView.getLance().adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                (int) size.getWidth(), (int) size.getHeight());
        lanceView.setLanceImage();      // reset image to its original sizes
        lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
                (int) lanceView.getLance().getLength(),
                (int) lanceView.getLance().getThickness()));
        // TODO: Add other entities
        fireBallView.getFireBall().updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                (int) size.getWidth(), (int) size.getHeight());
        fireBallView.setFireBallImage();
        fireBallView.setFireBallImage(ImageHandler.resizeImage(fireBallView.getFireBallImage(),
                fireBallView.getFireBall().getSize(),
                fireBallView.getFireBall().getSize()));

        /*
        barriers.forEach(barrierView -> {
            barrierView.getBarrier().setL(size.getWidth() / 10.0);
            barrierView.setBarrierImage(ImageHandler.resizeImage(
                    barrierView.getBarrierImage(),
                    (int) barrierView.getBarrier().getLength(),
                    (int) barrierView.getBarrier().getThickness()
            ));
        });
         */
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

    public List<RemainsView> getRemainViews() {
        return remains;
    }

    public void setRemainView(List<RemainsView> remains) {
        this.remains = remains;
    }
}
