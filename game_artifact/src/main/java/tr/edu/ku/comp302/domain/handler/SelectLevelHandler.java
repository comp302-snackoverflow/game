package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.services.save.LoadService;
import tr.edu.ku.comp302.ui.panel.SelectLevelPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SelectLevelHandler {
    private static SelectLevelHandler instance;

    private SelectLevelHandler() {
    }

    public static SelectLevelHandler getInstance() {
        if (instance == null) {
            instance = new SelectLevelHandler();
        }
        return instance;
    }

    // FIXME: This is bad practice and passes a Level object to a UI object. Find out how to set the level here.
    //  "WITHOUT SINGLETONS OR MODE STATIC VARIABLES"
    public Level getLevel(int levelId) {
        return LoadService.getInstance().loadMap(levelId);
    }

    public List<Integer> getLevels(int uid) {
        return DatabaseHandler.getInstance().getMaps(uid);
    }

    public JPanel generateSelectLevelButton(int buttonIndex, int levelId, Consumer<Level> onSuccess) {
        JPanel button = new JPanel(new BorderLayout());
        Border raised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border lowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        button.setBorder(raised);

        JLabel levelLabel = new JLabel("Level " + buttonIndex);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        button.add(levelLabel, BorderLayout.CENTER);

        Level level = getLevel(levelId);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBorder(lowered);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.getBorder() == lowered) {
                    if (level != null) {
                        onSuccess.accept(getLevel(levelId));
                        button.setBorder(raised);
                    } else {
                        JOptionPane.showMessageDialog(button, "Level could not be loaded", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        return button;
    }
}
