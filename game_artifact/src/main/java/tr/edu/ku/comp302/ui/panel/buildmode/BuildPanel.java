package tr.edu.ku.comp302.ui.panel.buildmode;

import tr.edu.ku.comp302.domain.handler.BuildHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BuildPanel extends JPanel {
    private final MainFrame mainFrame;
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
        self.buildSection.setBackground(new Color(0x534E4E4E, true));
        self.buildSection.setLayout(null);
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
            try{
                int simpleCount = Integer.parseInt(generatePanel.simpleBarrierCount.getText());
                int firmCount = Integer.parseInt(generatePanel.firmBarrierCount.getText());
                int explosiveCount = Integer.parseInt(generatePanel.explosiveBarrierCount.getText());
                int giftingCount = Integer.parseInt(generatePanel.giftingBarrierCount.getText());
                if (!buildHandler.countsSatisfied(simpleCount, firmCount, explosiveCount, giftingCount)) {
                    JOptionPane.showMessageDialog(null, "Invalid number of barriers! Make sure that your simple" +
                            "barrier count is over 75, your firm barrier count is over 10, your explosive barrier count is over 5, your gift barrier count " +
                            "is over 10, and that your overall barrier count does not exceed 200!");
                }
                buildHandler.generateRandomMap(simpleCount, firmCount, explosiveCount, giftingCount);
            }catch (NumberFormatException nfe){
                alertBarrierInputs();
            }
        });
    }

    private void addSelectBarrierActions() {
        barriersPanel.simpleBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.SIMPLE_MODE));
        barriersPanel.firmBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.FIRM_MODE));
        barriersPanel.explosiveBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.EXPLOSIVE_MODE));
        barriersPanel.giftingBarrierButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.GIFT_MODE));
        barriersPanel.deleteButton.addActionListener(e -> buildHandler.setSelection(BuildHandler.DELETE_MODE));
    }

    

    private void addButtonsActions() {
        buttonsPanel.saveButton.addActionListener(e -> buildHandler.saveMap(buildSection.getWidth(), buildSection.getHeight()));
        buttonsPanel.playButton.addActionListener(e -> {
            if (buildHandler.countsSatisfied()){
                mainFrame.setCurrentLevel(buildHandler.getLevel());
                mainFrame.showLevelPanel();
                buildHandler.clearMap();
            }else{
                alertBarrierInputs();
            }
        });
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
            public void mouseDragged(MouseEvent e) {
                buildHandler.handlePress(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                buildHandler.handlePress(e.getX(), e.getY());
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

    public void alertSaveSuccess() {
        JOptionPane.showMessageDialog(this, "Map saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void alertSaveFailure() {
        JOptionPane.showMessageDialog(this, "Map could not be saved.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void alertFullMap() {
        JOptionPane.showMessageDialog(this, "Map is full. Please clear some barriers before inserting more barriers.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void alertBarrierInputs(){
        JOptionPane.showMessageDialog(this, "Invalid input. Please enter non-negative integer.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
