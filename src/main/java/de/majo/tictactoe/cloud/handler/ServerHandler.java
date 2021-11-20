package de.majo.tictactoe.cloud.handler;

import de.majo.tictactoe.cloud.handler.singleserver.CloudServer;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler {

    private List<String> openPortList;
    private CloudServer proxyServer;
    private List<CloudServer> serverList;
    private int port = 8000;

    public ServerHandler() {
        serverList = new ArrayList<>();

        openPortList = new ArrayList<>();
        for(int i = 8000; i < 9998; i++) {
            openPortList.add(String.valueOf(i));
        }

        //starting proxy
        this.proxyServer = new CloudServer(9999, this);
        this.proxyServer.enableProxyMode();
        this.proxyServer.setMotd("Proxy-Server - Port: " + this.proxyServer.getPort());
        this.proxyServer.start();
        serverList.add(this.proxyServer);
    }

    public void createNewServer() {
        int port = -1;
        for(int i = 8000; i <= 8100; i++) {
            if(openPortList.contains(String.valueOf(i))) {
                port = i;
            }
        }

        if(port != -1) {
            CloudServer cloudServer = new CloudServer(port, this);
            cloudServer.start();
            serverList.add(cloudServer);
            openPortList.remove(String.valueOf(port));
        }else {
            ConsoleHandler.log(" > Warning: Max Server Amount is reached!");
        }
    }
    public void createNewServer(String motd) {
        int port = -1;
        for(int i = 8000; i <= 8100; i++) {
            if(openPortList.contains(String.valueOf(i))) {
                port = i;
            }
        }

        if(port != -1) {
            CloudServer cloudServer = new CloudServer(port, this);
            cloudServer.setMotd(motd);
            cloudServer.start();
            serverList.add(cloudServer);
            openPortList.remove(String.valueOf(port));
        }else {
            ConsoleHandler.log(" > Warning: Max Server Amount is reached!");
        }
    }

    public List<CloudServer> getServerList() {
        return serverList;
    }

    public List<String> getOpenPortList() {
        return openPortList;
    }
}
