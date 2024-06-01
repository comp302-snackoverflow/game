package tr.edu.ku.comp302.domain.lanceofdestiny.state;

import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class PlayingMPState extends PlayingState implements MultiplayerState {
    public PlayingMPState(LanceOfDestiny lanceOfDestiny) {
        super(lanceOfDestiny);
    }
    @Override
    public void update(P2PConnection conn) {
        super.update(conn);
        LevelHandler h = lanceOfDestiny.getLevelHandler();
        lanceOfDestiny.tryAddMessage();
        if (h.isWon()) {
            lanceOfDestiny.addMessage("INFO:WIN");
        } else if (h.isFinished()) {
            lanceOfDestiny.addMessage("INFO:LOSE");
        }
    }

    public boolean isMultiplayer() {
        return true;
    }
}