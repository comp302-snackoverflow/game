package tr.edu.ku.comp302.domain.handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {
    public static boolean leftArrowPressed = false;
    public static boolean rightArrowPressed = false;

    public static boolean buttonAPressed = false;
    public static boolean buttonDPressed = false;
    public static boolean buttonWPressed = false;

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO: Implement it later
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                rightArrowPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftArrowPressed = true;
                break;
            case KeyEvent.VK_A:
                buttonAPressed = true;
                break;
            case KeyEvent.VK_D:
                buttonDPressed = true;
                break;
            case KeyEvent.VK_W:
                buttonWPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                rightArrowPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftArrowPressed = false;
                break;
            case KeyEvent.VK_A:
                buttonAPressed = false;
                break;
            case KeyEvent.VK_D:
                buttonDPressed = false;
                break;
            case KeyEvent.VK_W:
                buttonWPressed = false;
                break;
        }
    }
}
