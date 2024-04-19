package tr.edu.ku.comp302;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;

/**
 * Hello world!
 */
public class App
{
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args)
    {
        logger.warn("Hello World!");
        Lance lance = new Lance(576, 600);
        LanceView lanceView = new LanceView(lance);
        FireBall fireBall = new FireBall(630, 570);
        FireBallView fireBallView = new FireBallView(fireBall);
        Level level = new Level();
        LevelPanel levelPanel = new LevelPanel(level, lanceView, fireBallView);
        new LanceOfDestiny(levelPanel);

    }
}
