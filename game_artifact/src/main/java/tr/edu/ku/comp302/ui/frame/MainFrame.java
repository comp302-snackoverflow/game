package tr.edu.ku.comp302.ui.frame;

import javax.swing.JFrame;

import tr.edu.ku.comp302.ui.panel.GamePanel;


public class MainFrame extends JFrame{
    

    public void displayGamePanel(){
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Redrawing the panel to reflect rotation and movement 
        while (true) {
            gamePanel.repaint();
            try {
                Thread.sleep(16); // This should be around 60 Frames per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
