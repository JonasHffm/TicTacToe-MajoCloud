package client.util;

public class Server {

    private String motd;
    private long playerAmount;
    private int port;

    public Server(String motd, long playerAmount, int port) {
        this.motd = motd;
        this.playerAmount = playerAmount;
        this.port = port;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public long getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(long playerAmount) {
        this.playerAmount = playerAmount;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Server{" +
                "motd='" + motd + '\'' +
                ", playerAmount=" + playerAmount +
                ", port=" + port +
                '}';
    }
}
