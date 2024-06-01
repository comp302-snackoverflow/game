package tr.edu.ku.comp302.ui.panel;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class HelpPanel extends JPanel {
    public HelpPanel() {
        // Set the size of the HelpPanel
        int panelWidth = LanceOfDestiny.getScreenWidth() / 2;
        int panelHeight = LanceOfDestiny.getScreenHeight() / 2;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setLayout(new BorderLayout());

        // Create a JTextPane to display the help content
        JTextPane helpTextPane = new JTextPane();
        helpTextPane.setFocusable(false);
        helpTextPane.setContentType("text/html");
        helpTextPane.setText(getHelpContent());
        helpTextPane.setEditable(false); // Make the text pane non-editable

        // Center text alignment
        StyledDocument doc = helpTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Create a JScrollPane to make the content scrollable
        JScrollPane scrollPane = new JScrollPane(helpTextPane);
        add(scrollPane, BorderLayout.CENTER);

        // Scroll to the top
        SwingUtilities.invokeLater(() -> helpTextPane.setCaretPosition(0));
    }

    private String getHelpContent() {
        // Return the HTML content with image paths corrected and sizes set
        return "<html><body style='text-align: center;'>" +
                "<h1>Welcome Brave Warrior!</h1>" +
                "<p>Prepare yourself for an epic journey to claim the Lance of Destiny! In this mystical world your goal is simple: destroy all the barriers in your path as quickly as possible without losing all your lifelines. Sounds easy? Think again!</p>" +
                "<h2>~ Types of Barriers ~</h2>" +
                "<h3>Simple Barriers</h3>" +
                "<img src='" + getClass().getResource("/assets/simple_barrier.png") + "' alt='Simple Barrier Image' width='50' height='20'>" +
                "<p>These barriers can be shattered with a single hit. Piece of cake!</p>" +
                "<h3>Firm Barriers</h3>" +
                "<img src='" + getClass().getResource("/assets/firm_barrier.png") + "' alt='Firm Barrier Image' width='50' height='20'>" +
                "<p>Tougher than they look! These require multiple hits to break. Stay persistent!</p>" +
                "<h3>Explosive Barriers</h3>" +
                "<img src='" + getClass().getResource("/assets/explosive_barrier.png") + "' alt='Explosive Barrier Image' width='50' height='20'>" +
                "<p>Hit them and BOOM! Watch out for the falling debris—they can cost you a lifeline!</p>" +
                "<h3>Gift Barriers</h3>" +
                "<img src='" + getClass().getResource("/assets/gift_barrier.png") + "' alt='Gift Barrier Image' width='50' height='20'>" +
                "<p>Break these to unleash magical spells. Use them wisely!</p>" +
                "<h2>Spells You Can Acquire</h2>" +
                "<p><b>Lance Extension (Press T):</b> Make your Magical Staff longer to deflect the Fire Ball with ease.</p>" +
                "<p><b>Hex Spell (Press H):</b> Equip your Magical Staff with hex-casting canons. Fire away!</p>" +
                "<p><b>Overwhelming Fireball:</b> Turns your Fire Ball into an unstoppable force plowing through barriers like a hot knife through butter!</p>" +
                "<h2>Beware of Ymir!</h2>" +
                "<img src='" + getClass().getResource("/assets/ymir.png") + "' alt='Ymir Image' width='70' height='70'>" +
                "<p>The evil sorcerer Ymir won't make your quest easy. Every 30 seconds a coin flip determines whether Ymir will cast one of her devious spells to slow you down:</p>" +
                "<p><b>Infinite Void:</b> Freezes 8 of your barriers making them impervious to most spells for 15 seconds.</p>" +
                "<p><b>Double Accel:</b> Slows down your Fire Ball by half for 15 seconds.</p>" +
                "<p><b>Hollow Purple:</b> Adds 8 empty barriers to your map that give you no points when destroyed. Annoying right?</p>" +
                "<h2>Tips to Win:</h2>" +
                "<p>1) Destroy Barriers Quickly: The faster you break them the closer you are to victory!</p>" +
                "<p>2) Use Spells Strategically: Activate spells at the right moment to maximize their impact.</p>" +
                "<p>3) Avoid Ymir’s Traps: Stay alert and adapt quickly to his spells.</p>" +
                "<h2>Remember!</h2>" +
                "<p>You have only three chances. If the Fire Ball falls below your Magical Staff three times you lose!</p>" +
                "<p>You may save your progress anytime and load it later to continue your quest.</p>" +
                "<p>Feeling confident? Challenge a friend in dual-player mode and see who can claim the Lance of Destiny first!</p>" +
                "<h1>Good luck mighty warrior! May your courage and skill guide you to victory!</h1>" +
                "</body></html>";
    }
}