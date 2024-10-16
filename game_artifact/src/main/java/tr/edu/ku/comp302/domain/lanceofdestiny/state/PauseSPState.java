package tr.edu.ku.comp302.domain.lanceofdestiny.state;

import tr.edu.ku.comp302.chrono.Chronometer;
import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class PauseSPState implements State {
    protected LanceOfDestiny lanceOfDestiny;

    public PauseSPState(LanceOfDestiny lanceOfDestiny) {
        this.lanceOfDestiny = lanceOfDestiny;
    }

    @Override
    public void update(P2PConnection ignored) {
        Chronometer chronometer = lanceOfDestiny.getChronometer();
        if (chronometer.getPauseStartTime() == null) {
            chronometer.setPauseStartTime(chronometer.getCurrentTime());
        }
        try {
            Thread.sleep(1000 / lanceOfDestiny.getUPS_SET());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isMultiplayer() {
        return false;
    }
}
