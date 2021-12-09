package client.util;

import client.gui.MainGUI;

public class Data {

    private ConnectionHandler connectionHandler;
    private MainGUI mainGUI;

    public static String IP = "127.0.0.1";

    public static boolean alreadyConnected = false;

    public static String username = "";

    public static boolean gameQuit = false;

    public static ProxyData proxyData;
    public static ServerData serverData = new ServerData();

    public Data() {
        this.connectionHandler = new ConnectionHandler();
        this.mainGUI = new MainGUI(proxyData);
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

}
