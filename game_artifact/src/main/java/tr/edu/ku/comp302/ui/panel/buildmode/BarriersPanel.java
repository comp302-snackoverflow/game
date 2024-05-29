package tr.edu.ku.comp302.ui.panel.buildmode;

import javax.swing.*;
import java.awt.*;

public class BarriersPanel extends JPanel {
    protected final JButton simpleBarrierButton;
    protected final JButton firmBarrierButton;
    protected final JButton explosiveBarrierButton;
    protected final JButton giftingBarrierButton;
    protected final JButton deleteButton;
    public BarriersPanel() {
        setLayout(new GridBagLayout());
        simpleBarrierButton = new JButton("Simple Barrier");
        firmBarrierButton = new JButton("Firm Barrier");
        explosiveBarrierButton = new JButton("Explosive Barrier");
        giftingBarrierButton = new JButton("Gifting Barrier");
        deleteButton = new JButton("Delete");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(simpleBarrierButton, gbc);
        gbc.gridy = 1;
        add(firmBarrierButton, gbc);
        gbc.gridy = 2;
        add(explosiveBarrierButton, gbc);
        gbc.gridy = 3;
        add(giftingBarrierButton, gbc);
        gbc.gridy = 4;
        add(deleteButton, gbc);
    }
}
