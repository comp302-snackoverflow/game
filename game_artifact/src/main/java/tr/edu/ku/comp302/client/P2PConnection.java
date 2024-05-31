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
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ConcurrentLinkedQueue<String> queuedMessages = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> receivedMessages = new ConcurrentLinkedQueue<>();

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
        startHeartBeat();
    }

    public void connectToPeer() throws IOException {
        if (socket != null) {
            close();
        }
        socket = new Socket(peerAddress, peerPort);
        socket.setKeepAlive(true);

        startHeartBeat();
    }

    private void startHeartBeat() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (queuedMessages.isEmpty()) {
                    sendMessage("HEARTBEAT");
                } else {
                    while (!queuedMessages.isEmpty()) {
                        sendMessage(queuedMessages.poll());
                    }
                }
                while (socket.getInputStream().available() > 0) {
                    receivedMessages.add(receiveMessage());
                }
            } catch (Exception e) {
                logger.error("Heartbeat failed", e);
                reconnect();
            }
        }, 0, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
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
        queuedMessages.add(message);
    }

    public String receive() {
        if (receivedMessages.isEmpty()) {
            return null;
        }
        return receivedMessages.poll();
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

    private void sendMessage(String message) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(message);
    }

    private String receiveMessage() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return in.readLine();
    }

    public PlayerInfo getPeer() {
        return new PlayerInfo(peerAddress, peerPort);
    }
}