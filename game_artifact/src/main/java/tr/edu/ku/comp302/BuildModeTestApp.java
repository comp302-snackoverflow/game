package tr.edu.ku.comp302;

import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.BuildPanel;

public class BuildModeTestApp {
    public static void main(String[] args)
    {
        double w = 1280,
               h = 800;
        BuildPanel buildPanel = new BuildPanel();
        MainFrame mainFrame = new MainFrame(buildPanel);
        while(true) {
            buildPanel.repaint();
            buildPanel.revalidate();
        }
    }
}
