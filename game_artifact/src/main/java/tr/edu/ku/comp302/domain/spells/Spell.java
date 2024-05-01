package tr.edu.ku.comp302.domain.spells;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.ui.view.LanceView;

public class Spell{
    static Timer timer;

    public static void extendLance(LanceView lanceView){
        Lance lance = lanceView.getLance();
        System.out.println(lance.getLength());
        lance.setL(lance.getL() * 2);
        System.out.println(lance.getLength());
        //set lance Image according to new length
        
        lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
        (int) lanceView.getLance().getLength(),
        (int) lanceView.getLance().getThickness()));
    

        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Enable the button after the timer finishes
                System.out.println("10 seconds elapsed");
                lance.setL(lance.getL() / 2);
                lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
                (int) lanceView.getLance().getLength(),
                (int) lanceView.getLance().getThickness()));
                // Stop the timer
                timer.stop();
            }

           
        });
        timer.setRepeats(false); // Execute the action only once
        timer.start();
    }




   


}