package tr.edu.ku.comp302.domain.lanceofdestiny.state;

import tr.edu.ku.comp302.client.GameInfo;
import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class PlayingMPState extends PlayingState implements MultiplayerState {
    public PlayingMPState(LanceOfDestiny lanceOfDestiny) {
        super(lanceOfDestiny);
    }

    @Override
    public void update(P2PConnection conn) {
        super.update(conn);

        GameInfo info = lanceOfDestiny.getGameInfo();

        conn.send(String.format("SCORE:%d:CHANCES:%d:BARRIERS:%d", info.score(), info.chances(), info.barrierCount()));

        System.out.println(conn.receive());
    }

    public boolean isMultiplayer() {
        return true;
    }
}
