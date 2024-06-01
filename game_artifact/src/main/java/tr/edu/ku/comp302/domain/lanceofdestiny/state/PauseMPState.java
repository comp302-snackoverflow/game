package tr.edu.ku.comp302.domain.lanceofdestiny.state;

import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class PauseMPState extends PauseSPState implements MultiplayerState {
    public PauseMPState(LanceOfDestiny lanceOfDestiny) {
        super(lanceOfDestiny);
    }

    public void update(P2PConnection conn) {
        super.update(conn);
    }

    public boolean isMultiplayer() {
        return true;
    }
}
