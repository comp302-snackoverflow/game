package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.client.GameClient;
import tr.edu.ku.comp302.domain.listeners.MessageListener;
import tr.edu.ku.comp302.domain.listeners.PeerJoinListener;

import java.io.IOException;
import java.util.Base64;

public class CreateGameHandler {
    private static final Logger logger = LogManager.getLogger(CreateGameHandler.class);
    private Thread listenerThread;
    private String generateGameCode(int levelId) {
        // generates a game code by combining the level id and a random number
        return Base64.getEncoder().encodeToString((levelId + "." + System.nanoTime()).getBytes());
    }
    public String createGame(int levelId, PeerJoinListener listener) {
        String gameCode = generateGameCode(levelId);
        try {
            if (GameClient.createGame(gameCode)) {
                listenerThread = GameClient.listenForPeer(listener);
                return gameCode;
            } else {
                return null;
            }
        } catch (IOException e) {
            logger.error("An error occurred while creating the game. Level ID: " + levelId, e);
            return null;
        }
    }

    public boolean removeGame(String gameCode) {
        try {
            if (GameClient.removeGame(gameCode)) {
                if (listenerThread != null) {
                    listenerThread.interrupt();
                    listenerThread = null;
                }
                return true;
            }
            return false;
        } catch (IOException e) {
            logger.error("An error occurred while removing the game. Game code: {}", gameCode, e);
            return false;
        }
    }
}
