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
    private static final int HEARTBEAT_INTERVAL = 5; // seconds
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public P2PConnection(String peerAddress, int peerPort) {
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
    }

    public P2PConnection() {
        this.peerAddress = null;
        this.peerPort = 0;
    }

    public void startServer() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            close();
        }
        serverSocket = new ServerSocket(PORT);
        socket = serverSocket.accept();
        socket.setKeepAlive(true);
//        this.peerAddress = socket.getInetAddress().getHostAddress();
//        this.peerPort = socket.getPort();
    }

    public void connectToPeer() throws IOException {
        if (socket != null) {
            close();
        }
        socket = new Socket(peerAddress, peerPort);
        socket.setKeepAlive(true);
    }

    private void reconnect() {
        close();
        try {
            if (peerAddress != null && peerPort != 0) {
                connectToPeer();
            } else {
                startServer();
            }
        } catch (IOException e) {
            logger.error("Reconnection failed", e);
        }
    }

    public void send(String message) {
        if (socket == null) {
            throw new IllegalStateException("Connection is not established");
        } else if (socket.isClosed()) {
            try {
                if (peerAddress != null && peerPort != 0) {
                    connectToPeer();
                } else {
                    startServer();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
        } catch (IOException e) {
            logger.error("An error occurred while sending the message", e);
        }
    }

    public String receive() {
        if (socket == null) {
            throw new IllegalStateException("Connection is not established");
        } else if (socket.isClosed()) {
            try {
                if (peerAddress != null && peerPort != 0) {
                    connectToPeer();
                } else {
                    startServer();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return in.readLine();
        } catch (IOException e) {
            logger.error("An error occurred while receiving the message", e);
            return null;
        }
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
        } finally {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        }
    }

    public PlayerInfo getPeer() {
        return new PlayerInfo(peerAddress, peerPort);
    }
}