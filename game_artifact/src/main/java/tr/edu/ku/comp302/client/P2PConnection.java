package tr.edu.ku.comp302.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.server.PlayerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class P2PConnection {
    private static final Logger logger = LogManager.getLogger(P2PConnection.class);
    private String peerAddress;
    private int peerPort;
    private ServerSocket serverSocket;
    private Socket socket;
    private static final int PORT = 3132;
    private static final int HEARTBEAT_INTERVAL = 3; // seconds

    public P2PConnection(String peerAddress, int peerPort) {
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
    }

    public P2PConnection() {
        this.peerAddress = null;
        this.peerPort = 0;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        socket = serverSocket.accept();
        socket.setKeepAlive(true);
    }

    public void connectToPeer() throws IOException {
        if (socket != null) {
            close();
        }
        socket = new Socket(peerAddress, peerPort);
        socket.setKeepAlive(true);
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                socket.close();
            }
        } catch (IOException e) {
            logger.error("An error occurred while closing the connection", e);
        }
    }

    public void sendMessage(String message) throws IOException {
        if (serverSocket != null) {
            socket = serverSocket.accept();
        } else {
            connectToPeer();
        }

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        if (serverSocket != null) {
            socket = serverSocket.accept();
        } else {
            connectToPeer();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return in.readLine();
    }

    public PlayerInfo getPeer() {
        return new PlayerInfo(peerAddress, peerPort);
    }
}