package tr.edu.ku.comp302.server;

public record PlayerInfo(String address, int port) {
    public String toString() {
        return address + ":" + port;
    }
}