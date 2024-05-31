package tr.edu.ku.comp302.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private ScheduledExecutorService executorService;
    private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
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
        serverSocket = new ServerSocket(PORT);
        logger.info("Server started, waiting for connection...");
        socket = serverSocket.accept();
        socket.setKeepAlive(true);
        logger.info("Connection accepted from {}", socket.getRemoteSocketAddress());
        startCommunication();
    }

    public void connectToPeer() throws IOException {
        startCommunication();
    }

    private void startCommunication() throws IOException {
        if (serverSocket == null) {
            socket = new Socket(peerAddress, peerPort);
        } else {
            socket = serverSocket.accept();
        }
        executorService = Executors.newScheduledThreadPool(2);

        executorService.scheduleWithFixedDelay(() -> {
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                String message;
                while ((message = messageQueue.poll()) != null) {
                    out.println(message);
                    logger.info("Sent message: " + message);
                }
            } catch (IOException e) {
                logger.error("An error occurred while sending a message to the peer", e);
                close();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    logger.info("Received message: " + message);
                    receivedMessages.add(message);
                }
            } catch (IOException e) {
                logger.error("An error occurred while receiving a message from the peer", e);
                close();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void send(String msg) {
        messageQueue.add(msg);
    }

    public String receive() {
        return receivedMessages.poll();
    }

    public void close() {
        try {
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            logger.info("Connection closed");
        } catch (IOException | InterruptedException e) {
            logger.error("An error occurred while closing the connection", e);
        }
    }
}
