package tr.edu.ku.comp302.domain.spells;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;

public class Spell{
    static Timer timer;

    /**
     * Extends the length of the lance.
     *
     * @param lanceView The view of the lance.
     */
    public static void extendLance(LanceView lanceView) {
    
        //Gets the lance object from the view.
        Lance lance = lanceView.getLance();
        //Doubles the length of the lance.
        lance.setL(lance.getL() * 2);
        //Sets the lance image according to the new length.
        lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
                (int) lanceView.getLance().getLength(),
                (int) lanceView.getLance().getThickness()));

        //Initializes a timer that will reduce the length of the lance after 10 seconds.
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Enables the button after the timer finishes.
                System.out.println("10 seconds elapsed");

                
                //Halves the length of the lance.
                lance.setL(lance.getL() / 2);

                //Sets the lance image according to the new length.
                lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
                        (int) lanceView.getLance().getLength(),
                        (int) lanceView.getLance().getThickness()));

                // Stops the timer.
                timer.stop();
            }
        });

        //Sets the repeats of the timer to false, meaning it will only execute the action once.
        timer.setRepeats(false);

        
        //Starts the timer.
        timer.start();
    }



    public static void overWhelmingFireBall(FireBallView fireballView){
        FireBall fireBall = fireballView.getFireBall();

        fireBall.setOverwhelmed(true);


        //Initializes a timer that will turn fireball into an overwhelming
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Enables the button after the timer finishes.
                System.out.println("10 seconds elapsed");

                
                //set fireball to nromal mode
                fireBall.setOverwhelmed(false);

                // Stops the timer.
                timer.stop();
            }
        });

        //Sets the repeats of the timer to false, meaning it will only execute the action once.
        timer.setRepeats(false);

        
        //Starts the timer.
        timer.start();
    }


   


}