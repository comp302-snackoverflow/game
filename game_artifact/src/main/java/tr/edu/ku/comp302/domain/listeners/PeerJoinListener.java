package tr.edu.ku.comp302.domain.listeners;

import tr.edu.ku.comp302.server.PlayerInfo;

public interface PeerJoinListener {
    void onJoin(PlayerInfo playerInfo);
}
