package tr.edu.ku.comp302.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class CircularButton extends JButton {
    private BufferedImage icon;

    public CircularButton(BufferedImage icon) {
        this.icon = icon;
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape circle = new Ellipse2D.Float(0, 0, getWidth() - 1, getHeight() - 1);
        g2.setColor(getBackground());
        g2.fill(circle);
        if (icon != null) {
            int x = (getWidth() - icon.getWidth()) / 2;
            int y = (getHeight() - icon.getHeight()) / 2;
            g2.drawImage(icon, x, y, this);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50); // Adjust size as needed
    }

    @Override
    public boolean contains(int x, int y) {
        Shape circle = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return circle.contains(x, y);
    }
}
