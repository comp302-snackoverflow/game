package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.BuildPanel;
import tr.edu.ku.comp302.ui.panel.LevelPanel;

public class BuildMode {
    private MainFrame mainFrame;
    private BuildPanel buildPanel;  // TODO: change this when we implement more than one level
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Thread gameThread;

    public BuildMode(BuildPanel buildPanel, MainFrame mainFrame) {
        this.buildPanel = buildPanel;
        this.mainFrame = mainFrame;
        buildPanel.requestFocusInWindow();
        
    }

    
}
