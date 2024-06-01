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

public class SelectLoadHandler {
    private static SelectLoadHandler instance;

    private SelectLoadHandler() {
    }

    public static SelectLoadHandler getInstance() {
        if (instance == null) {
            instance = new SelectLoadHandler();
        }
        return instance;
    }

    public Level getLevel(int savedId) {
        return LoadService.getInstance().loadGame(savedId);
    }

    public List<Integer> getLevels(int uid) {
        return DatabaseHandler.getInstance().getSavedLevels(uid);
    }

    public JPanel generateSelectLevelButton(int buttonIndex, int saveId, Consumer<Level> onSuccess) {
        JPanel button = new JPanel(new BorderLayout());
        Border raised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border lowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        button.setBorder(raised);

        JLabel levelLabel = new JLabel("Level " + buttonIndex);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        button.add(levelLabel, BorderLayout.CENTER);

        Level level = getLevel(saveId);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBorder(lowered);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.getBorder() == lowered) {
                    if (level != null) {
                        onSuccess.accept(getLevel(saveId));
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
