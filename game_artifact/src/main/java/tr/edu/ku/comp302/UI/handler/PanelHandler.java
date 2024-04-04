package tr.edu.ku.comp302.ui.handler;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class PanelHandler {


     public void displayPanel(JFrame frame, JPanel panel){

        resetPanels(frame);
        frame.add(panel);
        panel.requestFocus();

        // Redrawing the panel to reflect rotation and movement 
        while (true) {
            panel.repaint();
            try {
                Thread.sleep(16); // This should be around 60 Frames per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Resets all the JPanel components within a JFrame.
     * This method clears the JFrame of existing panels before adding new ones.
     * 
     * @param frame the JFrame object from which panels are to be removed
     */
    public static void resetPanels(JFrame frame) {
        // Get the content pane of the frame
        Container contentPane = frame.getContentPane();
        // Get an array of all components in the content pane
        Component[] components = contentPane.getComponents();
        // Iterate through each component
        for (Component component : components) {
            // Check if the component is an instance of JPanel
            if (component instanceof JPanel) {
                // Remove the panel from the content pane
                contentPane.remove(component);
            }
        }
        // Revalidate the frame to reflect the changes
        frame.revalidate();
        // Repaint the frame to ensure proper rendering
        frame.repaint();
    }


}
