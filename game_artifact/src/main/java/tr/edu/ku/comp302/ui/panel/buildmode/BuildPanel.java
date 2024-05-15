package tr.edu.ku.comp302.ui.panel.buildmode;

import tr.edu.ku.comp302.domain.handler.BuildHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BuildPanel extends JPanel {
    private MainFrame mainFrame;
    private BuildHandler buildHandler;
    private JPanel buildSection;
    private JPanel controlsSection;
    private BarriersPanel barriersPanel;
    private GeneratePanel generatePanel;
    private ButtonsPanel buttonsPanel;

    private BuildPanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
    }

    public static BuildPanel createPanel(MainFrame mainFrame) {
        BuildPanel self = new BuildPanel(mainFrame);
        self.setMinimumSize(new Dimension(720, 540));
        self.buildHandler = new BuildHandler(self);
        self.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        self.buildSection = new JPanel();
        self.buildSection.setBackground(new Color(0xA34E4E4E, true));
        self.buildSection.setLayout(null); // TODO: see if a grid layout can be used and barriers can be put into it directly
        //  If it can be used, just a simple mouse listener can be used to
        //  add/remove barriers. Also would make drawing gridlines easier.
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        self.add(self.buildSection, gbc);


        self.controlsSection = new JPanel();
        self.controlsSection.setLayout(new BorderLayout());
        self.barriersPanel = new BarriersPanel();
        self.generatePanel = new GeneratePanel();
        self.buttonsPanel = new ButtonsPanel();
        self.controlsSection.add(self.barriersPanel, BorderLayout.NORTH);
        self.controlsSection.add(self.generatePanel, BorderLayout.CENTER);
        self.controlsSection.add(self.buttonsPanel, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        self.add(self.controlsSection, gbc);
        self.addBuildListeners();
        self.addSelectBarrierActions();
        self.addGenerateAction();
        self.addButtonsActions();

        return self;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        buildHandler.paintPanel(g, buildSection.getWidth(), buildSection.getHeight());
    }

    private void addGenerateAction() {
        generatePanel.generateButton.addActionListener(e -> {
            int simpleCount = Integer.parseInt(generatePanel.simpleBarrierCount.getText());
            int firmCount = Integer.parseInt(generatePanel.firmBarrierCount.getText());
            int explosiveCount = Integer.parseInt(generatePanel.explosiveBarrierCount.getText());
            int giftingCount = Integer.parseInt(generatePanel.giftingBarrierCount.getText());
            buildHandler.generateRandomMap(simpleCount, firmCount, explosiveCount, giftingCount);
        });
    }

    private void addSelectBarrierActions() {
        barriersPanel.simpleBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.SIMPLE_MODE));
        barriersPanel.firmBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.FIRM_MODE));
        barriersPanel.explosiveBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.EXPLOSIVE_MODE));
        barriersPanel.giftingBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.GIFT_MODE));
    }

    private void addButtonsActions() {
        buttonsPanel.saveButton.addActionListener(e -> buildHandler.saveMap());
        buttonsPanel.playButton.addActionListener(e -> {}); // TODO: start game
        buttonsPanel.clearButton.addActionListener(e -> buildHandler.clearMap());
        buttonsPanel.exitButton.addActionListener(e -> mainFrame.showMainMenuPanel());
    }

    public void addBuildListeners() {
        buildSection.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                buildHandler.handlePress(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO: is this needed?
            }
        });

        buildSection.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                buildHandler.handleMouseDrag(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                buildHandler.handleMouseMove(e.getX(), e.getY());
            }
        });
    }

    public JPanel getBuildSection() {
        return buildSection;
    }
}
