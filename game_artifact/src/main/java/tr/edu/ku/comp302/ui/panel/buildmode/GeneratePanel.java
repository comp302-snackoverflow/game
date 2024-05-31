package tr.edu.ku.comp302.ui.panel.buildmode;

import javax.swing.*;
import java.awt.*;

public class GeneratePanel extends JPanel {
    protected final JTextArea simpleBarrierCount;
    protected final JTextArea firmBarrierCount;
    protected final JTextArea explosiveBarrierCount;
    protected final JTextArea giftingBarrierCount;
    protected final JButton generateButton;

    public GeneratePanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel simpleBarrierLabel = new JLabel("Simple Barriers:");
        simpleBarrierCount = new JTextArea("0");

        JLabel firmBarrierLabel = new JLabel("Firm Barriers:");
        firmBarrierCount = new JTextArea("0");

        JLabel explosiveBarrierLabel = new JLabel("Explosive Barriers:");
        explosiveBarrierCount = new JTextArea("0");

        JLabel giftingBarrierLabel = new JLabel("Gifting Barriers:");
        giftingBarrierCount = new JTextArea("0");

        JPanel simpleBarrier = new JPanel();
        simpleBarrier.setLayout(new FlowLayout(FlowLayout.LEFT));
        simpleBarrier.add(simpleBarrierLabel);
        simpleBarrier.add(simpleBarrierCount);

        JPanel firmBarrier = new JPanel();
        firmBarrier.setLayout(new FlowLayout(FlowLayout.LEFT));
        firmBarrier.add(firmBarrierLabel);
        firmBarrier.add(firmBarrierCount);

        JPanel explosiveBarrier = new JPanel();
        explosiveBarrier.setLayout(new FlowLayout(FlowLayout.LEFT));
        explosiveBarrier.add(explosiveBarrierLabel);
        explosiveBarrier.add(explosiveBarrierCount);

        JPanel giftingBarrier = new JPanel();
        giftingBarrier.setLayout(new FlowLayout(FlowLayout.LEFT));
        giftingBarrier.add(giftingBarrierLabel);
        giftingBarrier.add(giftingBarrierCount);

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(simpleBarrier, gbc);
        gbc.gridy = 1;
        add(firmBarrier, gbc);
        gbc.gridy = 2;
        add(explosiveBarrier, gbc);
        gbc.gridy = 3;
        add(giftingBarrier, gbc);

        generateButton = new JButton("Generate Random Map");
        gbc.gridy = 4;
        add(generateButton, gbc);
    }
}
