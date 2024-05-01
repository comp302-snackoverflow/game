package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.spells.Spell;
import tr.edu.ku.comp302.ui.view.BarrierView;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;
import tr.edu.ku.comp302.ui.view.RemainsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class LevelPanel extends JPanel {
    private Level level;
    private LanceView lanceView;
    private FireBallView fireBallView;
    private List<BarrierView> barriers;

    private List <RemainsView> remains = new ArrayList<>();

    public LevelPanel(Level level, LanceView lanceView, FireBallView fireBallView, List<BarrierView> barriers) {
        this.level = level;
        this.lanceView = lanceView;
        this.fireBallView = fireBallView;
        this.barriers = barriers;
        initializeSpellButtons();
        addKeyListener(new KeyboardHandler());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        lanceView.render(g);
        fireBallView.render(g);
        IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g)); // Below throws an exception.
        IntStream.range(0, remains.size()).forEach(i-> remains.get(i).render(g));
        // barriers.forEach(barrier -> barrier.render(g));
    }
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        lanceView.getLance().setL(size.getWidth() * 2  / 10.0);
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

    public List<RemainsView> getRemainViews() {
        return remains;
    }

    public void setRemainView(List<RemainsView> remains) {
        this.remains = remains;
    }


    private void initializeSpellButtons() {
        JButton lanceExtSpellButton = new JButton("Extension Spell");

        // Set the bounds for the button
        lanceExtSpellButton.setBounds(   (getSize().width / 2 ), (int) (getSize().height * 0.875) , 150, 30); // Adjust x, y, width, and height as needed

        // Add the button to the panel
        this.add(lanceExtSpellButton);

        lanceExtSpellButton.addActionListener(e -> {
            
            // Your existing action when the button is pressed
            Spell.extendLance(lanceView);
           
            //turn of the button until next spell collected
            lanceExtSpellButton.setEnabled(false);


            JLabel timerLabel = new JLabel("10");
            timerLabel.setBounds((getSize().width / 2) + 160, (int) (getSize().height * 0.875), 50, 30); // Adjust position as needed
            this.add(timerLabel);

            Timer timer = new Timer(1000, new ActionListener() {
                int count = 10;

                @Override
                public void actionPerformed(ActionEvent e) {
                    count--;
                    timerLabel.setText(String.valueOf(count));
                    if (count == 0) {
                        ((Timer) e.getSource()).stop();
                        lanceExtSpellButton.setEnabled(true); // Enable button after countdown finishes
                        remove(timerLabel); // Remove timer label after countdown finishes
                    }
                }
            });
            timer.start();

            this.requestFocusInWindow();



        });
    }

}


