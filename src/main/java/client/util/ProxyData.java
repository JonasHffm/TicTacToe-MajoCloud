package client.util;

import java.util.ArrayList;

public class ProxyData {
    private String motd;
    private int port;
    private long players;

    private ArrayList<String> playServersList;

    public ProxyData(String motd, int port, int players, ArrayList<String> playServersList) {
        this.motd = motd;
        this.port = port;
        this.players = players;
        this.playServersList = playServersList;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getPlayers() {
        return players;
    }

    public void setPlayers(long players) {
        this.players = players;
    }

    public ArrayList<String> getPlayServersList() {
        return playServersList;
    }

    public void setPlayServersList(ArrayList<String> playServersList) {
        this.playServersList = playServersList;
    }
}
