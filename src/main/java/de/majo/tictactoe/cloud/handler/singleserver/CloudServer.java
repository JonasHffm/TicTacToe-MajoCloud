package de.majo.tictactoe.cloud.handler.singleserver;

import de.majo.tictactoe.cloud.handler.ConsoleHandler;
import de.majo.tictactoe.cloud.handler.ServerHandler;
import de.majo.tictactoe.cloud.handler.singleserver.client.CloudClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class CloudServer extends Thread{

    private ServerSocket server;
    private Map<CloudClient, String> client_list;
    private String UID = UUID.randomUUID().toString();
    private Timer timer;
    private boolean stoped = false;
    private ServerHandler serverHandler;
    private boolean proxyMode = false;
    private CloudServer instance;
    private final int MAX_CLIENT_ACCEPTION = 2;
    private int port;
    private String motd = "Welcome to the server!";

    private List<String> gameField;
    private boolean won;

    public CloudServer(int port, ServerHandler serverHandler) {
        instance = this;
        this.serverHandler = serverHandler;
        this.gameField = new ArrayList<String>(){
            {
                add("-");add("-");add("-");
                add("-");add("-");add("-");
                add("-");add("-");add("-");
            }
        };

        client_list = new HashMap<>();
        this.port = port;

        this.timer = new Timer();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(this.port);
            ConsoleHandler.log(" > Info: Server started! -> UID: " + UID);
            ConsoleHandler.log(" > Info: MOTD: " + motd);
        } catch (IOException e) {
            ConsoleHandler.log(" >> Error while starting server [" + port + "]");
        }

        clientacception();
    }

    public void clientacception() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                int client_acception_buffer = 0;
                if(proxyMode) {
                    client_acception_buffer = 998;
                }

                if(client_list.size() < MAX_CLIENT_ACCEPTION+client_acception_buffer) {
                    try {
                        ConsoleHandler.log(" > Info: Waiting for new client... [" + port + "] UID: " + UID);
                        Socket socket = server.accept();

                        if(!proxyMode) {
                            if (client_list.size() == 0) {
                                client_list.put(new CloudClient(socket, instance, "1", serverHandler), "1");
                            } else {
                                client_list.put(new CloudClient(socket, instance, "2", serverHandler), "2");
                            }
                        }else {
                            String id = UUID.randomUUID().toString();
                            client_list.put(new CloudClient(socket, instance, id, serverHandler), id);
                        }

                        ConsoleHandler.log(" > Info: New client connected to [" + port + "] UID: " + UID);

                        if(client_list.size() == MAX_CLIENT_ACCEPTION+client_acception_buffer) {
                            ConsoleHandler.log(" > Info: Server is full! [" + port + "] UID: " + UID);
                        }

                    } catch (IOException e) {
                        if(!stoped) {
                            ConsoleHandler.log(" >> Error while accepting new clients on server [" + port + "] UID:" + UID);
                        }
                    }
                }//else {
                    //spam
                    //if(!stoped) {
                    //    ConsoleHandler.log(" > Info: Server is full! [" + port + "] UID: " + UID);
                    //}
                //}
            }
        }, 0, 1000);

    }

    public void shutdown() {
        ConsoleHandler.log(" > Shutting down server... [" + port + "] UID: " + UID);

        timer.cancel();
        stoped = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                client_list.forEach((socket, s) -> {
                    try {
                        socket.getClient().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
        ConsoleHandler.log(" > Server is offline! [" + port + "] UID: " + UID);
    }


    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public Map<CloudClient, String> getClient_list() {
        return client_list;
    }

    public void setClient_list(Map<CloudClient, String> client_list) {
        this.client_list = client_list;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getMAX_CLIENT_ACCEPTION() {
        return MAX_CLIENT_ACCEPTION;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isProxyMode() {
        return proxyMode;
    }

    public void setProxyMode(boolean proxyMode) {
        this.proxyMode = proxyMode;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public String getMotd() {
        return motd;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public List<String> getGameField() {
        return gameField;
    }

    public boolean isWon() {
        return won;
    }

    public void enableProxyMode() {
        UID = "proxy";
        proxyMode = true;
    }

}
