package tr.edu.ku.comp302.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.listeners.MessageListener;
import tr.edu.ku.comp302.domain.listeners.MessageSender;
import tr.edu.ku.comp302.domain.listeners.PeerJoinListener;
import tr.edu.ku.comp302.server.PlayerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class P2PConnection {
    private static final Logger logger = LogManager.getLogger(P2PConnection.class);
    private static final int PORT = 3132;
    private String peerAddress;
    private int peerPort;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageListener messageListener;
    private MessageSender messageSender;
    private PeerJoinListener peerJoinListener;
    private Thread senderThread;
    private Thread receiverThread;

    public P2PConnection(String peerAddress, int peerPort) {
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
    }

    public P2PConnection() {
        this.peerAddress = null;
        this.peerPort = 0;
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public void setMessageSender(MessageSender sender) {
        this.messageSender = sender;
    }

    public void setPeerJoinListener(PeerJoinListener listener) {
        this.peerJoinListener = listener;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        socket = serverSocket.accept();
        peerJoinListener.onJoin(this);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        setup();
    }

    public void connectToPeer() throws IOException {
        socket = new Socket(peerAddress, peerPort);
        socket.setKeepAlive(true);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        setup();
    }

    public void setup() {
        senderThread = new Thread(() -> {
            try {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                        break;
                    }
                    String message = in.readLine();
                    if (message == null) {
                        continue;
                    }
                    if (messageListener != null) {
                        messageListener.onMessageReceived(message);
                    }
                }
            } catch (SocketTimeoutException ignored) {
                System.out.println("Connection timed out");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        receiverThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                    break;
                }
                if (messageSender != null) {
                    String msg = messageSender.next();
                    if (msg != null) {
                        out.println(msg);
                    }
                }
            }
        });

        senderThread.start();
        receiverThread.start();
    }

    public synchronized void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (senderThread != null) {
                senderThread.interrupt();
            }
            if (receiverThread != null) {
                receiverThread.interrupt();
            }
        } catch (IOException e) {
            logger.error("An error occurred while closing the connection", e);
        }
    }
}
