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
    private Thread senderThread;
    private Thread receiverThread;
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
        socket = serverSocket.accept();
        socket.setKeepAlive(true);

    }

    public void connectToPeer() throws IOException {
        socket = new Socket(peerAddress, peerPort);
        socket.setKeepAlive(true);
        senderThread = new Thread(() -> {
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                while (true) {
                    if (!messageQueue.isEmpty()) {
                        String message = messageQueue.poll();
                        out.println(message);
                    }
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                logger.error("An error occurred while sending a message to the peer", e);
            } catch (InterruptedException e) {
                this.senderThread.interrupt();
            }
        });

        receiverThread = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                while (true) {
                    String message = in.readLine();
                    if (message != null) {
                        receivedMessages.add(message);
                    }
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                logger.error("An error occurred while receiving a message from the peer", e);
            } catch (InterruptedException e) {
                this.receiverThread.interrupt();
            }
        });

        senderThread.start();
        receiverThread.start();
    }

    public void send(String msg) {
        messageQueue.add(msg);
    }

    public String receive() {
        return receivedMessages.poll();
    }
}