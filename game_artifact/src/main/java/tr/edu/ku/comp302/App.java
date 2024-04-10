package tr.edu.ku.comp302;


import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.view.LanceView;

/**
 * Hello world!
 */
public class App 
{
    public static void main(String[] args)
    {
        Lance lance = new Lance(576, 600);
        LanceView lanceView = new LanceView(lance);
        Level level = new Level();
        LevelPanel levelPanel = new LevelPanel(level, lanceView);
        new LanceOfDestiny(levelPanel);

    }
}
