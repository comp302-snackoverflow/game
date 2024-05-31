package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.client.GameClient;
import tr.edu.ku.comp302.client.JoinInfo;
import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.server.PlayerInfo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Base64;

public class JoinGameHandler {
    private static final Logger logger = LogManager.getLogger(JoinGameHandler.class);

    public static String getClipboardText() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                logger.warn("An error occurred while getting the clipboard text", e);
            }
        }
        return null;
    }

    public JoinInfo joinGame(String gameCode) {
        try {
            PlayerInfo peer = GameClient.joinGame(gameCode);
            if (peer == null) {
                return null;
            }
            String decoded = new String(Base64.getDecoder().decode(gameCode));
            String[] parts = decoded.split("\\.");
            int levelId = Integer.parseInt(parts[0]);
            String peerAddress = peer.address();
            int peerPort = peer.port();
            P2PConnection connection = new P2PConnection(peerAddress, peerPort);
            while (true) {
                try {
                    connection.connectToPeer();
                    System.out.println(connection.receiveMessage());
                    break;
                } catch (IOException e) {
                    logger.warn("Failed to connect to peer. Retrying in 5 seconds...");
                    Thread.sleep(500);
                }
            }
            return new JoinInfo(levelId, connection);
        } catch (IOException | InterruptedException e) {
            logger.error("An error occurred while joining the game. Game code: " + gameCode, e);
            return null;
        }
    }
}
