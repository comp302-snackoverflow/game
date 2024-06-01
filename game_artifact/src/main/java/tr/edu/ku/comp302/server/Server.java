package tr.edu.ku.comp302.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 3131;
    private static final Map<String, PlayerInfo> gameSessions = new HashMap<>();

    public static void main(String[] args) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        }
    }

    protected static void createGameSession(String gameCode, String address, int port) {
        gameSessions.put(gameCode, new PlayerInfo(address, port));
    }

    protected static void removeGameSession(String gameCode) {
        gameSessions.remove(gameCode);
    }

    protected static PlayerInfo getGameSession(String address) {
        return gameSessions.get(address);
    }
}
