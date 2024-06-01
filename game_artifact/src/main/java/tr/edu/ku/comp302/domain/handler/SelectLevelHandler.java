package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.services.save.LoadService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

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

    public Level getLevel(int levelId) {
        return LoadService.getInstance().loadMap(levelId);
    }

    public List<Integer> getLevels(int uid) {
        return DatabaseHandler.getInstance().getMaps(uid);
    }

    public JPanel generateSelectLevelButton(int buttonIndex, int levelId, Consumer<Integer> onClick) {
        JPanel button = new JPanel(new BorderLayout());
        Border raised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border lowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        button.setBorder(raised);

        JLabel levelLabel = new JLabel("Level " + buttonIndex);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        button.add(levelLabel, BorderLayout.CENTER);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBorder(lowered);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.getBorder() == lowered) {
                    onClick.accept(levelId);
                    button.setBorder(raised);
                }
            }
        });
        return button;
    }
}
