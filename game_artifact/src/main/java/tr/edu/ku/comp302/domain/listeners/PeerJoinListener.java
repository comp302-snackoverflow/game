package tr.edu.ku.comp302.domain.listeners;

import tr.edu.ku.comp302.client.P2PConnection;

public interface PeerJoinListener {
    void onJoin(P2PConnection conn);
}
