package tr.edu.ku.comp302;

import tr.edu.ku.comp302.ui.frame.MainFrame;

public class BuildModeTestApp {
    public static void main(String[] args) {
        double w = 1280, h = 800;
        MainFrame frame = MainFrame.createMainFrame();
        frame.showBuildPanel();
        frame.setVisible(true);
        while (true) {
            frame.getBuildPanel().repaint();
        }
    }
}
