package tr.edu.ku.comp302.ui.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;

import javax.swing.JPanel;

import tr.edu.ku.comp302.domain.handler.ImageHandler;

public class BuildPanel extends JPanel {
   ;

    public BuildPanel() {
        
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(ImageHandler.getImageFromPath("/assets/barrier_image.png")
                    , (int) MouseInfo.getPointerInfo().getLocation().getX() - (int) this.getLocationOnScreen().getX()
                    , (int) MouseInfo.getPointerInfo().getLocation().getY() - (int) this.getLocationOnScreen().getY()
                    , null);
        //lanceView.render(g);
        
        //IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g));
        
        
    }
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        

        
    }

}