package tr.edu.ku.comp302.ui.handler;

import javax.swing.JFrame;

import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.GamePanel;

public class FrameHandler {

    //declaring instances

    JFrame mainFrame; //the frame to be displayed
    PanelHandler panelHandler; //using panel handler class in order to add panels to the frame
    LanceOfDestiny lanceOfDestiny; // reaching the objects of the game or system
    

    /**
    * Initializes the main frame and panel handler.
    * This object is responsible for handling operations of UI in general.
    */
    public FrameHandler(LanceOfDestiny lanceOfDestiny){
        mainFrame = new  MainFrame();
        panelHandler = new PanelHandler();
        this.lanceOfDestiny = lanceOfDestiny;

        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }


    /**
     * 
     * This function takes the level info and initilize the level to play the game
     */
    public void startGame(){
        GamePanel gamePanel = new GamePanel(lanceOfDestiny.getLevel());
        panelHandler.displayPanel(mainFrame, gamePanel);
    }
    
}
