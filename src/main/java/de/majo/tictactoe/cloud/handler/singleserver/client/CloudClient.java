package de.majo.tictactoe.cloud.handler.singleserver.client;

import de.majo.tictactoe.cloud.handler.ConsoleHandler;
import de.majo.tictactoe.cloud.handler.ServerHandler;
import de.majo.tictactoe.cloud.handler.singleserver.CloudServer;
import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CloudClient extends Thread{

    private Socket client;
    private CloudServer cloudServer;
    private String playerType;
    private String username;

    private HashMap<String, String> dataStorageMap;

    private ServerHandler serverHandler;

    public CloudClient(Socket client, CloudServer cloudServer, String playerType, ServerHandler serverHandler) {
        this.playerType = playerType;
        this.serverHandler = serverHandler;

        this.dataStorageMap = new HashMap<>();
        loadDataInput();

        this.client = client;
        this.cloudServer = cloudServer;
        this.start();
    }

    @Override
    public void run() {
        client_communication();
    }

    public void client_communication() {
        //ConsoleHandler.log(" > Started listen thread! ");
        do {
            loadDataInput();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                String input = reader.readLine();

                packetReadHandler(input);
                //System.out.println(input);



                if(dataStorageMap.containsKey(input)) {
                    writer.write(dataStorageMap.get(input) + "\n");
                    writer.flush();
                }

                if(input == null) {
                    ConsoleHandler.log(" > Client disconnected! [" + cloudServer.getPort() + "] - UID: " + cloudServer.getUID());
                    client.close();
                    cloudServer.getClient_list().remove(this);
                    break;
                }

                /*
                if(input != null) {
                    if(!input.equals("ServerList")) {
                        System.out.println(input);
                    }
                }
                */

            } catch (IOException e) {
                try {
                    client.close();
                } catch (IOException ioException) {
                }
            }
        } while (client.isConnected());
    }


    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public CloudServer getCloudServer() {
        return cloudServer;
    }

    public void setCloudServer(CloudServer cloudServer) {
        this.cloudServer = cloudServer;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }


    public void loadDataInput() {

        //proxy mode information
        if(cloudServer != null) {
            if(cloudServer.isProxyMode()) {

                //input proxy server list information
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                JSONObject onlineserver_count = new JSONObject();
                JSONObject onlineserver_motd = new JSONObject();

                serverHandler.getServerList().forEach(cloudServerObj -> {
                    jsonArray.add(cloudServerObj.getPort());
                    onlineserver_count.put(cloudServerObj.getPort(), cloudServerObj.getClient_list().size());
                    onlineserver_motd.put(cloudServerObj.getPort(), cloudServerObj.getMotd());
                });

                jsonObject.put("onlineserver", jsonArray);
                jsonObject.put("onlineservermotd", onlineserver_motd);
                jsonObject.put("onlineservercount", onlineserver_count);
                dataStorageMap.put("ServerList", jsonObject.toJSONString());

            }else {
                //data packet for client information
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonArray.addAll(cloudServer.getGameField());
                jsonObject.put("gamefield", jsonArray);
                JSONArray jsonArrayUser = new JSONArray();
                jsonArrayUser.addAll(cloudServer.getClient_list().values());
                jsonObject.put("user", jsonArrayUser);
                jsonObject.put("won", cloudServer.isWon());
                if(!cloudServer.isWon()) {
                    jsonObject.put("playerwon", "-");
                }else {
                    //TODO: set player won
                }
                if(cloudServer.getClient_list().size() == 2) {
                    jsonObject.put("turn", cloudServer.getTurn());
                    jsonObject.put(cloudServer.getClient_list().values().toArray()[0], "X");
                    jsonObject.put(cloudServer.getClient_list().values().toArray()[1], "O");
                }else {
                    jsonObject.put("turn", "-");
                }
                dataStorageMap.put("PacketInfoGamestate", jsonObject.toJSONString());
            }
        }
    }


    public void packetReadHandler(String packetMessage) {
        JSONParser jsonParser = new JSONParser();

        if(packetMessage != null) {
            if (packetMessage.startsWith("PacketPlayOutSendUsername;;;")) {
                String jsonStr = packetMessage.split(";;;")[1];
                try {
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonStr);
                    String username = (String) jsonObject.get("username");
                    if(!cloudServer.getClient_list().containsValue(username)) {
                        System.out.println(" > New client registered as -> " + username + " > [" + cloudServer.getPort() + "]");
                        cloudServer.getClient_list().put(this, username);
                        this.username = username;
                        if (cloudServer.getClient_list().size() >= 2) {
                            cloudServer.setTurn((String) cloudServer.getClient_list().values().toArray()[new Random().nextInt(2)]);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(packetMessage.startsWith("PacketPlayOutChangeGame;;;")) {
                String jsonStr = packetMessage.split(";;;")[1];
                try {
                    if(!jsonStr.contains("REQUESTINFO")) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonStr);
                        //String username = (String) jsonObject.get("username");

                        //debug
                        //System.out.println("   ----->> New game change!");
                        //System.out.println(jsonObject.get("gamefield"));

                        cloudServer.setGameField((List<String>) jsonObject.get("gamefield"));
                        ArrayList<String> users = new ArrayList<>();
                        users.addAll(cloudServer.getClient_list().values());
                        users.remove(cloudServer.getTurn());
                        cloudServer.setTurn(users.get(0));

                        try {
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                            writer.write("PacketInfoGamestate;;;" + dataStorageMap.get("PacketInfoGamestate") + "\n");
                            writer.flush();
                        } catch (IOException e) {
                        }
                    }else {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                        writer.write("PacketInfoGamestate;;;" + dataStorageMap.get("PacketInfoGamestate") + "\n");
                        writer.flush();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
