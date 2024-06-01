package tr.edu.ku.comp302;

import tr.edu.ku.comp302.domain.handler.DatabaseHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;

public class App {
    public static void main(String[] args) {
        DatabaseHandler.init();
        MainFrame frame = MainFrame.createMainFrame();
        frame.showLoginPanel();
        frame.setVisible(true);
    }
}