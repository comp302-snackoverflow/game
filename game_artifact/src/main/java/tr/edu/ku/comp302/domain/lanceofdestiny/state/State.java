package tr.edu.ku.comp302.domain.lanceofdestiny.state;

import tr.edu.ku.comp302.client.P2PConnection;

public interface State {
    void update(P2PConnection conn);
    boolean isMultiplayer();
}
