package tr.edu.ku.comp302.ui.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import tr.edu.ku.comp302.domain.entity.Barriers.Barrier;
import tr.edu.ku.comp302.domain.entity.Barriers.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.Barriers.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.Barriers.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.MouseHandler;
import tr.edu.ku.comp302.ui.view.BarrierView;

public class BuildPanel extends JPanel {
    double height;
    double width;
    Graphics g;
    ArrayList<Double> x_indexes = new ArrayList<>();
    ArrayList<Double> y_indexes = new ArrayList<>();
   String displayImagePath = "/assets/barrier_image.png";

   private List<BarrierView> barriers = new ArrayList<>();

    public BuildPanel(double height, double width) {
        this.height = height;
        this.width = width;
        this.setLayout(null);
        displayBarrierSelections();
        addMouseListener(new MouseHandler());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        displayGridLines(g, width, height);
        ArrayList<ArrayList<Double>> list = new ArrayList<>();
        list.add( x_indexes);
        list.add( y_indexes);
        double currentBarrierX = returnStart(list)[0];
        double currentBarrierY = returnStart(list)[1];
        
        g.drawImage(ImageHandler.createCustomImage(displayImagePath, (int)width/50, 20)
                    , (int) (currentBarrierX + width/104)
                    , (int) (currentBarrierY + ((height / 2) - 80) / 10)
                    , null);
        
        if (MouseHandler.mouseClicked) {
            switch (displayImagePath) {
                case "/assets/barrier_image.png":
                    SimpleBarrier simpleBarrier = new SimpleBarrier(currentBarrierX + width/104, currentBarrierY + ((height / 2) - 80) / 10, width, height);
                    BarrierView simpleView = new BarrierView(simpleBarrier);
                    barriers.add(simpleView);
                    break;
                case "/assets/explosive_barrier.png":
                    Barrier explosiveBarrier = new ExplosiveBarrier(currentBarrierX + width/104, currentBarrierY + ((height / 2) - 80) / 10, width, height);
                    BarrierView explosiveView = new BarrierView(explosiveBarrier);
                    barriers.add(explosiveView);
                    break;
                case "/assets/firm_barrier.png":
                    Barrier firmBarrier = new FirmBarrier(currentBarrierX + width/104, currentBarrierY + ((height / 2) - 80) / 10, width, height);
                    BarrierView firmView = new BarrierView(firmBarrier);
                    barriers.add(firmView);
                    break;
            }

            //TODO: Add a gift barrier.

        }

        IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g));


        
        //lanceView.render(g);
        
        //IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g));
        
        
    }
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        barriers.forEach(barrierView -> {
            barrierView.getBarrier().setL(width / 10.0);
            barrierView.setBarrierImage(ImageHandler.resizeImage(
                    barrierView.getBarrierImage(),
                    (int) barrierView.getBarrier().getLength(),
                    (int) barrierView.getBarrier().getThickness()
            ));
        });
        
    }

    

 
    public void displayGridLines(Graphics g, double width, double height) {
        double x_interval = width / 52;
        double y_interval = ((height / 2) - 80) / 5 ;
        double final_vertical = 0;
    
    

        for (double x = 0; x < width; x += x_interval + width / 50) {
            g.drawLine((int)x, 0 , (int)x, (int)height/2);
            x_indexes.add(x);
            final_vertical=x;

        }
        

        for (double y = 0; y <= height / 2; y += y_interval) {
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

    /**
    * This method is responsible for displaying the barrier selection options on the panel.
    * It creates a custom image for the barrier, positions a button with the barrier image,
    * and adds it to the panel.
    *
    * @param height The height of the panel.
    * @param width The width of the panel.
    */
    public void displayBarrierSelections() {
       
        // This method creates a custom image for the barrier using the provided image path,
        

        // Add the barrier button to the panel.
        JButton simpleBarrierButton = createButton(20, (int)(height*0.7), "/assets/barrier_image.png");
        JButton explosiveBarrierButton = createButton(20, (int)(height*0.7) + 30, "/assets/explosive_barrier.png");
        JButton firmBarrierButton = createButton(20, (int)(height*0.7) + 60, "/assets/firm_barrier.png");
        JButton giftBarrierButton = createButton(20, (int)(height*0.7) + 90, "/assets/barrier_image.png");
        add(simpleBarrierButton);
        add(explosiveBarrierButton);
        add(firmBarrierButton);
        add(giftBarrierButton);
    }


    public JButton createButton(int xPoisition, int yPosition, String path){
            // button spacing, and dimensions.
        BufferedImage ImageBuffer = ImageHandler.createCustomImage(
            path,
            (int)width/10,
            30);

    // This method creates an ImageIcon object using the custom image buffer.
    ImageIcon BarrierIcon = new ImageIcon(ImageBuffer);

    // Get the width and height of the barrier image.
    int buttonWidth = BarrierIcon.getIconWidth();
    int buttonHeight = BarrierIcon.getIconHeight();
    
    // Calculate the starting x-coordinate for the barrier button.
    

    // Create a JButton object with the barrier image and set its position on the panel.
    JButton BarrierButton = new JButton(BarrierIcon);
    BarrierButton.setBounds(xPoisition, yPosition, buttonWidth, buttonHeight);

    BarrierButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Action to perform when the button is clicked
            displayImagePath = path;
        }
    });
    return BarrierButton;
    }


}