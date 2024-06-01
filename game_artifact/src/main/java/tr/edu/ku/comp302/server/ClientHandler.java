package tr.edu.ku.comp302.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
//    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String request = in.readLine();
            String[] parts = request.split(":");

            switch (parts[0]) {
                case "CREATE" -> {
                    String gameCode = parts[1];
                    String address = socket.getInetAddress().getHostAddress();
                    String port = parts[2];
                    Server.createGameSession(gameCode, address, Integer.parseInt(port));
                    out.println("SUCCESS");
                }
                case "REMOVE" -> {
                    String gameCode = parts[1];
                    Server.removeGameSession(gameCode);
                    out.println("SUCCESS");
                }
                case "JOIN" -> {
                    String gameCode = parts[1];
                    PlayerInfo playerInfo = Server.getGameSession(gameCode);
                    if (playerInfo != null) {
                        out.printf("SUCCESS:%s:%d\n", playerInfo.address(), playerInfo.port());
                    } else {
                        out.println("FAILED");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
//            logger.error("An error occurred while handling the client", e);
        }
    }
}