package tr.edu.ku.comp302.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.server.PlayerInfo;
import tr.edu.ku.comp302.domain.listeners.PeerJoinListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private static final Logger logger = LogManager.getLogger(GameClient.class);
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 3131;
    private static final int PORT = 3132;

    public static boolean createGame(String gameCode) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("CREATE:" + gameCode + ":" + PORT);

            String response = in.readLine();
            return response.equals("SUCCESS");
        }
    }

    public static boolean removeGame(String gameCode) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("REMOVE:" + gameCode);
            String response = in.readLine();
            return response.equals("SUCCESS");
        }
    }

    public static PlayerInfo joinGame(String gameCode) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("JOIN:" + gameCode);

            String response = in.readLine();
            if (response.equals("FAILED")) {
                return null;
            } else {
                String[] parts = response.split(":");
                return new PlayerInfo(parts[1], Integer.parseInt(parts[2]));
            }
        }
    }

    public static Thread listenForPeer(PeerJoinListener listener) {
        Thread thread = new Thread(
                () -> {
                    P2PConnection conn = new P2PConnection();
                    conn.setPeerJoinListener(listener);
                    try {
                        conn.startServer();
                    } catch (IOException e) {
                        logger.error("An error occurred while listening for peer", e);
                    }
                }
        );
        thread.start();
        return thread;

    }
}
