package tr.edu.ku.comp302.domain.handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import tr.edu.ku.comp302.domain.entity.Lance;

public class LanceController implements KeyListener {
    private Lance lance;

    public LanceController(Lance lance) {
        this.lance = lance;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                lance.move(10);
                break;
            case KeyEvent.VK_LEFT:
                lance.move(-10);
                break;
            case KeyEvent.VK_A:
                lance.rotate(-10);
                break;
            case KeyEvent.VK_D:
                lance.rotate(10);
                break;
        }
    }    
    // THE FOLLOWING ARE NOT USED FOR NOW ///

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    
    }
}
