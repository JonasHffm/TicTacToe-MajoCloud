package client.util;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionHandler {

    private Socket proxy_socket;
    private Socket play_socket;

    private Thread listeningProxy, listeningPlayServer;

    public ConnectionHandler() {
        try {
            Data.proxyData = new ProxyData("", 9999, 0, new ArrayList<>());

            System.out.println(" > Info: Connecting to Proxy-Server...");
            this.proxy_socket = new Socket("127.0.0.1", 9999);
            this.proxy_socket.setKeepAlive(true);
            this.proxy_socket.setSoTimeout(1000*10);
            System.out.println(" > Info: Connected to Proxy-Server!");

            listeningProxy = new Thread(onListenEventProxy);
            listeningProxy.start();

            listeningPlayServer = new Thread(onListenEventPlayServer);


        } catch (IOException e) {
            System.out.println(" > Error: Connection refused!");
        }
    }

    public Runnable onListenEventProxy = () -> {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(proxy_socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(proxy_socket.getInputStream()));
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(proxy_socket.isConnected()) {
                        try {
                            writer.write("ServerList\n");
                            writer.flush();

                            String message = reader.readLine();
                            JSONParser jsonParser = new JSONParser();
                            try {
                                JSONObject jsonObject = (JSONObject)jsonParser.parse(message);
                                JSONArray jsonArray = (JSONArray) jsonObject.get("onlineserver");

                                Data.proxyData.setPlayServersList(jsonArray);

                                JSONObject servermotd = (JSONObject) jsonObject.get("onlineservermotd");
                                JSONObject playeramount = (JSONObject) jsonObject.get("onlineservercount");

                                Data.proxyData.setMotd((String) servermotd.get("9999"));
                                Data.proxyData.setPlayers((long) playeramount.get("9999"));

                                Data.serverData.getServerCollection().clear();
                                jsonArray.forEach(serverPort -> {
                                    String port = String.valueOf(serverPort);
                                    if(!port.equals("9999")) {
                                        Server server = new Server((String) servermotd.get(port),
                                                (long) playeramount.get(port),
                                                Integer.valueOf(port));
                                        Data.serverData.serverCollection.add(server);
                                    }
                                });

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public Runnable onListenEventPlayServer = () -> {
        Timer timer = new Timer();
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(play_socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(play_socket.getInputStream()));
            play_socket.setKeepAlive(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(play_socket != null) {
                        if (play_socket.isConnected()) {
                            try {
                                writer.write(" \n");
                                writer.flush();

                                String message = reader.readLine();
                                JSONParser jsonParser = new JSONParser();
                                System.out.println(message);

                            } catch (IOException e) {
                                this.cancel();
                            }
                        }
                    }
                }
            }, 0, 100);
        } catch (IOException ignored) {}

    };


    public Socket getProxy_socket() {
        return proxy_socket;
    }

    public void setProxy_socket(Socket proxy_socket) {
        this.proxy_socket = proxy_socket;
    }

    public Socket getPlay_socket() {
        return play_socket;
    }

    public void setPlay_socket(Socket play_socket) {
        this.play_socket = play_socket;
    }

    public Thread getListeningProxy() {
        return listeningProxy;
    }

    public void setListeningProxy(Thread listeningProxy) {
        this.listeningProxy = listeningProxy;
    }

    public Thread getListeningPlayServer() {
        return listeningPlayServer;
    }

    public void setListeningPlayServer(Thread listeningPlayServer) {
        this.listeningPlayServer = listeningPlayServer;
    }

    public Runnable getOnListenEventProxy() {
        return onListenEventProxy;
    }

    public void setOnListenEventProxy(Runnable onListenEventProxy) {
        this.onListenEventProxy = onListenEventProxy;
    }

    public Runnable getOnListenEventPlayServer() {
        return onListenEventPlayServer;
    }

    public void setOnListenEventPlayServer(Runnable onListenEventPlayServer) {
        this.onListenEventPlayServer = onListenEventPlayServer;
    }
}
