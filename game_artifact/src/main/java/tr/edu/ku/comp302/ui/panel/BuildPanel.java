package tr.edu.ku.comp302.ui.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.util.ArrayList;


import javax.swing.JPanel;

import tr.edu.ku.comp302.domain.handler.ImageHandler;

public class BuildPanel extends JPanel {
    double height;
    double width;
    Graphics g;
    ArrayList<Double> x_indexes = new ArrayList<>();
    ArrayList<Double> y_indexes = new ArrayList<>();
   

    public BuildPanel(double height, double width) {
        this.height = height;
        this.width = width;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        displayGridLines(g, width, height);
        ArrayList<ArrayList<Double>> list = new ArrayList<>();
        list.add( x_indexes);
        list.add( y_indexes);
        double currentBarrierX = returnStart(list)[0];
        double currentBarrierY = returnStart(list)[1];
        
        g.drawImage(ImageHandler.getImageFromPath("/assets/barrier_image.png")
                    , (int) (currentBarrierX + width/208)
                    , (int) (currentBarrierY + ((height / 2) - 80) / 10)
                    , null);
        
        

        
        //lanceView.render(g);
        
        //IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g));
        
        
    }
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        
    }

    

 
    public void displayGridLines(Graphics g, double width, double height) {
    double x_interval = width / 52;
    double y_interval = ((height / 2) - 80) / 5;
    double final_vertical = 0;
   
  

    for (double x = x_interval- x_interval/2; x < width; x += x_interval + x_interval/2 + width / 50) {
        g.drawLine((int)x, 0 , (int)x, (int)height/2);
        x_indexes.add(x);
        final_vertical=x;

    }
    

    for (double y = y_interval; y <= height / 2; y += 20 + y_interval) {
        g.drawLine((int)(x_interval-x_interval/2),(int) y, (int)final_vertical, (int)y); 
        y_indexes.add(y);
        
    }
   
    
}

public double[] returnStart(ArrayList<ArrayList<Double>> list){

    double currentX = MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX();
    double previousX = list.get(0).get(0);

    double currentY = MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY();
    double previousY = list.get(0).get(0);

    for(int i = 0; i < list.get(0).size(); i++){
        if(currentX > list.get(0).get(i) && currentX < width){
           previousX = list.get(0).get(i);
        }
    }
    for(int j = 0; j < list.get(1).size(); j++){
        if(currentY > list.get(1).get(j) && currentY < height/2){
           previousY = list.get(1).get(j);
        }
    }

    double[] output = {previousX, previousY};
    return output;
    
}


}