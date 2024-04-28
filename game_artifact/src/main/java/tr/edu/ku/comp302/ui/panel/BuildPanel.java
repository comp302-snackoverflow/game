package tr.edu.ku.comp302.ui.panel;


import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.swing.*;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.MouseHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.services.save.SaveService;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.view.BarrierView;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;

public class BuildPanel extends JPanel {
    ArrayList<Double> x_indexes = new ArrayList<>();
    ArrayList<Double> y_indexes = new ArrayList<>();
    String displayImagePath = "/assets/barrier_image.png";
    boolean gridPrinted = false;
    MainFrame mainFrame;

    BuildPanelModel viewModel;

    HashMap<List<Double>, BarrierView> putBarriersView = new HashMap<>();


    public BuildPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(null);
        displayBarrierSelections();
        setUpUserInputs();
        addMouseListener(new MouseHandler());
        this.viewModel = new BuildPanelModel();

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                refresh();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                refresh();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                refresh();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                refresh();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                refresh();
            }
        });


        /* (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                refresh();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                refresh();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                refresh();
            }
        }); */


    }

    public void paintComponent(Graphics g) {
        double width = LanceOfDestiny.getScreenWidth();
        double height = LanceOfDestiny.getScreenHeight();
        super.paintComponent(g);
        //while(!gridPrinted){
        displayGridLines(g, width, height);
        //    gridPrinted = true;
        //}

        ArrayList<ArrayList<Double>> gridCoordinateList = new ArrayList<>();
        gridCoordinateList.add(x_indexes);
        gridCoordinateList.add(y_indexes);

        if (!((MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY() > height / 2)
                || MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY() < (((height/2)-120)/7)/2)) {
            double currentBarrierX = returnStart(gridCoordinateList)[0];
            double currentBarrierY = returnStart(gridCoordinateList)[1];
            ArrayList<Double> coordinateList = new ArrayList<>();
            coordinateList.add(currentBarrierX);
            coordinateList.add(currentBarrierY);

            if (!putBarriersView.containsKey(coordinateList)) {
                g.drawImage(ImageHandler.createCustomImage(displayImagePath, (int) width / 50, 20)
                        , (int) (currentBarrierX + width / 104)
                        , (int) (currentBarrierY + (((height/2)-120)/7)/2)
                        , null);

            }

            if (MouseHandler.mouseClicked) {
                switch (displayImagePath) {
                    case "/assets/barrier_image.png":
                        SimpleBarrier simpleBarrier = new SimpleBarrier(currentBarrierX + width / 104, currentBarrierY + (((height/2)-120)/7)/2);
                        BarrierView simpleView = new BarrierView(simpleBarrier);
                        viewModel.scaleBarrierImages(simpleView);
                        putBarriersView.put(coordinateList, simpleView);
                        break;
                    case "/assets/explosive_barrier.png":
                        Barrier explosiveBarrier = new ExplosiveBarrier(currentBarrierX + width / 104, currentBarrierY + (((height/2)-120)/7)/2);
                        BarrierView explosiveView = new BarrierView(explosiveBarrier);
                        viewModel.scaleBarrierImages(explosiveView);
                        putBarriersView.put(coordinateList, explosiveView);
                        break;
                    case "/assets/firm_barrier.png":
                        Barrier firmBarrier = new FirmBarrier(currentBarrierX + width / 104, currentBarrierY + (((height/2)-120)/7)/2);
                        BarrierView firmView = new BarrierView(firmBarrier);
                        viewModel.scaleBarrierImages(firmView);
                        putBarriersView.put(coordinateList, firmView);
                        break;

                    case "/assets/bin.png":
                        // TODO: remove this?
                        BarrierView deletedView = putBarriersView.remove(coordinateList);
                        break;

                }
                viewModel.countBarriers(putBarriersView);

                //TODO: Add a gift barrier.

            }
        }

        putBarriersView.values().forEach(barriersView -> barriersView.render(g));

        //lanceView.render(g);

        //IntStream.range(0, barriers.size()).forEach(i -> barriers.get(i).render(g));


    }

    public void displayGridLines(Graphics g, double width, double height) {
        double x_start= width/52;
        double y_start = ((height/2)-120)/7;

        for (double x= x_start/2; x <= width-(x_start/2) +1 ; x += width/50 + x_start ){
            g.drawLine((int) x, (int) y_start/2, (int) x, (int) ((height/2)- y_start/2));
            x_indexes.add(x);
        }

        for(double y = y_start/2; y <= (height/2)- y_start/2; y += 20+y_start){
            g.drawLine((int)(x_start / 2), (int)y, (int) (width-(x_start/2)), (int) y);
            y_indexes.add(y);
        }
        // removed the last indices so that the generated barriers do not go out of bounds.
        x_indexes.removeLast();
        y_indexes.removeLast();

    }

    public double[] returnStart(ArrayList<ArrayList<Double>> list) {
        double width = LanceOfDestiny.getScreenWidth();
        double height = LanceOfDestiny.getScreenHeight();

        double currentX = MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX();
        double previousX = list.get(0).get(0);

        double currentY = MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY();
        double previousY = list.get(0).get(0);

        for (int i = 0; i < list.get(0).size(); i++) {
            if (currentX > list.get(0).get(i) && currentX < width) {
                previousX = list.get(0).get(i);
            }
        }
        for (int j = 0; j < list.get(1).size(); j++) {
            if (currentY > list.get(1).get(j) && currentY < height / 2) {
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
     */
    public void displayBarrierSelections() {
        double width = LanceOfDestiny.getScreenWidth();
        double height = LanceOfDestiny.getScreenHeight();

        // This method creates a custom image for the barrier using the provided image path,
        // Add the barrier button to the panel.
        JButton simpleBarrierButton = createButton(20, (int) (height * 0.6), "/assets/barrier_image.png");
        JButton explosiveBarrierButton = createButton(20, (int) (height * 0.6) + 30, "/assets/explosive_barrier.png");
        JButton firmBarrierButton = createButton(20, (int) (height * 0.6) + 60, "/assets/firm_barrier.png");
        JButton giftBarrierButton = createButton(20, (int) (height * 0.6) + 90, "/assets/barrier_image.png");
        JButton deleteButton = createButton(20, (int) (height * 0.6) + 120, "/assets/bin.png");
        add(simpleBarrierButton);
        add(explosiveBarrierButton);
        add(firmBarrierButton);
        add(giftBarrierButton);
        add(deleteButton);
    }


    public JButton createButton(int xPosition, int yPosition, String path) {
        double width = LanceOfDestiny.getScreenWidth();
        // button spacing, and dimensions.
        BufferedImage ImageBuffer = ImageHandler.createCustomImage(
                path,
                (int) width / 10,
                30);

        // This method creates an ImageIcon object using the custom image buffer.
        ImageIcon BarrierIcon = new ImageIcon(ImageBuffer);

        // Get the width and height of the barrier image.
        int buttonWidth = BarrierIcon.getIconWidth();
        int buttonHeight = BarrierIcon.getIconHeight();

        // Calculate the starting x-coordinate for the barrier button.


        // Create a JButton object with the barrier image and set its position on the panel.
        JButton BarrierButton = new JButton(BarrierIcon);
        BarrierButton.setBounds(xPosition, yPosition, buttonWidth, buttonHeight);

        BarrierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when the button is clicked
                displayImagePath = path;
            }
        });
        return BarrierButton;
    }

    public void setUpUserInputs() {
        double width = LanceOfDestiny.getScreenWidth();
        double height = LanceOfDestiny.getScreenHeight();

        int baseY = (int) (height * 0.6);
        JTextField simpleBarrierCountField = new JTextField("0");
        JTextField explosiveBarrierCountField = new JTextField("0");
        JTextField firmBarrierCountField = new JTextField("0");
        JTextField giftBarrierCountField = new JTextField("0");
        JButton generateMapButton = new JButton("Generate Map");

        int verticalSpacing = 30;

        simpleBarrierCountField.setBounds((int) width - 120, baseY, 100, 25);
        explosiveBarrierCountField.setBounds((int) width - 120, baseY + verticalSpacing, 100, 25);
        firmBarrierCountField.setBounds((int) width - 120, baseY + 2 * verticalSpacing, 100, 25);
        giftBarrierCountField.setBounds((int) width - 120, baseY + 3 * verticalSpacing, 100, 25);
        generateMapButton.setBounds((int) width - 120, baseY + 4 * verticalSpacing, 100, 25);

        add(simpleBarrierCountField);
        add(explosiveBarrierCountField);
        add(firmBarrierCountField);
        add(giftBarrierCountField);
        add(generateMapButton);

        JButton playButton = new JButton("Play");
        int buttonWidth = 100; 
        int buttonHeight = 50; 
        int buttonX = (int) (width / 2 - buttonWidth / 2);
        int buttonY = (int) (height*0.6 + 120); 
        playButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        add(playButton);

        JButton saveButton = new JButton("Save");
        int saveButtonX = (int) (width / 2 - buttonWidth / 2);
        int saveButtonY = (int) (height*0.6 + 60); 
        saveButton.setBounds(saveButtonX, saveButtonY, buttonWidth, buttonHeight);
        add(saveButton);


        generateMapButton.addActionListener(new ActionListener() {
            @Override
            // Note that this action CURRENTLY empties the whole map. So when the user decides to randomize
            // the map, their previous additions will be removed. This is done because the PDF specifies
            // that the map is randomly generated FIRST.

            public void actionPerformed(ActionEvent e) {
                //TODO: Implement a function so that the user is WARNED
                putBarriersView.clear();
                int simpleCount = Integer.parseInt(simpleBarrierCountField.getText());
                int explosiveCount = Integer.parseInt(explosiveBarrierCountField.getText());
                int firmCount = Integer.parseInt(firmBarrierCountField.getText());
                int giftCount = Integer.parseInt(giftBarrierCountField.getText());
                if (viewModel.barrierConditionsSatisfied(simpleCount, firmCount, explosiveCount, giftCount)) {
                    putBarriersView = viewModel.generateRandomMap(x_indexes, y_indexes, simpleCount, explosiveCount, giftCount, firmCount);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid number of barriers! To start a game, you must have" +
                            " at least 75 simple barriers, 10 firm barriers, 5 explosive barriers, and 10 gift barriers.");
                }
                refresh();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewModel.countBarriers(putBarriersView);
                // giftBarrierCount is given 10 for now since they don't exist!
                if (viewModel.barrierConditionsSatisfied(viewModel.getSimpleBarrierCount(), viewModel.getFirmBarrierCount(), viewModel.getExplosiveBarrierCount(), 10)) {
                    ArrayList<BarrierView> barriersList = new ArrayList<>(putBarriersView.values());
                    // note to meriç: you can call the get barrier function here to get the barriers if you need them, but I think
                    // this list is enough to generate a new level.
                    LevelPanel levelPanel = new LevelPanel(new Level(), new LanceView(new Lance(576,600)), new FireBallView(new FireBall(632,560)), barriersList, mainFrame);
                    levelPanel.setPanelSize(new Dimension(1280, 800));
                    levelPanel.repaint();
                    levelPanel.setFocusable(true);
                    levelPanel.requestFocusInWindow();
                    mainFrame.setLevelPanel(levelPanel);
                    mainFrame.showLevelPanel();

                    //TODO: Create a level here and open the running mode.

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid number of barriers! To start a game, you must have" +
                            " at least 75 simple barriers, 10 firm barriers, 5 explosive barriers, and 10 gift barriers.");
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setResizable(false); // must prevent resizing while saving to avoid issues while resizing during an ongoing save
                viewModel.countBarriers(putBarriersView);
                // giftBarrierCount is given 10 for now since they don't exist!
                if (viewModel.barrierConditionsSatisfied(viewModel.getSimpleBarrierCount(), viewModel.getFirmBarrierCount(), viewModel.getExplosiveBarrierCount(), 10)) {
                    ArrayList<BarrierView> barriersList = new ArrayList<>(putBarriersView.values());
                    // note to Mert: you can call the get barrier function here to get the barriers if you need them, but I think
                    // this list is enough to generate a new level.
                    SaveService saveService = SaveService.getInstance();
                    if (saveService.saveMap(barriersList.stream().map(BarrierView::getBarrier).toList(), width, height)) {
                        JOptionPane.showMessageDialog(null, "Map saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        putBarriersView.clear();
                    } else {
                        JOptionPane.showMessageDialog(null, "Map could not be saved!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    mainFrame.setResizable(true); // after save is complete, the user is free to resize

                    //TODO: Connect with the save logic 

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid number of barriers! To save a game, you must have" +
                            " at least 75 simple barriers, 10 firm barriers, 5 explosive barriers, and 10 gift barriers.");
                }
            }
        });
        
    }


    public HashMap<List<Double>, BarrierView> getPutBarriersView() {
        return putBarriersView;
    }

    private void refresh() {
        this.revalidate();
        this.repaint();
    }
}