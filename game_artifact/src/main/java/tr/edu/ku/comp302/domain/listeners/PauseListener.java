package tr.edu.ku.comp302.domain.listeners;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public interface PauseListener {
    void handlePauseRequest(Pausable eventConsumer);
}
