package tr.edu.ku.comp302.domain.event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class KeyPressEventFactory {
    public static void createKeyReleasedHandler(JComponent component, String keyType, Consumer<ActionEvent> consumer) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("pressed " + keyType), keyType + "Pressed");
        inputMap.put(KeyStroke.getKeyStroke("released " + keyType), keyType + "Released");

        actionMap.put(keyType + "Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                component.putClientProperty(keyType + "Pressed", true);
            }
        });

        actionMap.put(keyType + "Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean isPressed = (Boolean) component.getClientProperty(keyType + "Pressed");
                if (isPressed != null && isPressed) {
                    consumer.accept(e);
                    component.putClientProperty(keyType + "Pressed", false);
                }
            }
        });
    }
}
