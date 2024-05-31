package tr.edu.ku.comp302.domain.lanceofdestiny.state;

import tr.edu.ku.comp302.client.GameInfo;
import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

public class PlayingMPState extends PlayingState implements MultiplayerState {
    public PlayingMPState(LanceOfDestiny lanceOfDestiny) {
        super(lanceOfDestiny);
    }
    @Override
    public void update(P2PConnection conn) {
        super.update(conn);

        GameInfo info = lanceOfDestiny.getGameInfo();

        try {
            if (lanceOfDestiny.getChronometer().canReceive()) {
                conn.sendMessage(String.format("SCORE:%d:CHANCES:%d:BARRIERS:%d", info.score(), info.chances(), info.barrierCount()));

                String s = conn.receiveMessage();
                if (conn.receiveMessage() != null) {
                    System.out.println(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isMultiplayer() {
        return true;
    }
}
