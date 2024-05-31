package tr.edu.ku.comp302;

import tr.edu.ku.comp302.domain.handler.DatabaseHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.LevelPanel;

public class App {
    public static void main(String[] args) {
        DatabaseHandler.init();
        MainFrame frame = MainFrame.createMainFrame();
        frame.showLoginPanel();
        frame.setVisible(true);
    }
}