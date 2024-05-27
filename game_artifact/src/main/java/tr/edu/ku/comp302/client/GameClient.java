package tr.edu.ku.comp302.client;

import tr.edu.ku.comp302.server.PlayerInfo;
import tr.edu.ku.comp302.domain.listeners.PeerJoinListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameClient {
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
                try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                    System.out.println("Listening for peer");
                    Socket socket = serverSocket.accept();
                    PlayerInfo playerInfo = new PlayerInfo(socket.getInetAddress().getHostAddress(), socket.getPort());
                    new P2PConnection(playerInfo.address(), playerInfo.port()).startServer();
                    listener.onJoin(playerInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
        thread.start();
        return thread;

    }
}
