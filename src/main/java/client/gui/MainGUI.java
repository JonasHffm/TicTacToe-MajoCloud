package client.gui;

import client.main.Client;
import client.util.Data;
import client.util.PlayserverInfoState;
import client.util.ProxyData;
import client.util.Server;
import de.majo.tictactoe.cloud.handler.singleserver.CloudServer;
import org.json.simple.JSONObject;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.*;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class MainGUI {

    private JFrame frame;
    public static JTextField input_username;
    private ProxyData proxyData;
    private int playersplaying = 0;
    private Socket client;

    private JLabel lbl_motd, lbl_playersplaying, lbl_playersonline, lbl_port;
    private DefaultListModel<String> defaultListModel;

    public MainGUI(ProxyData proxyData) {
        this.proxyData = proxyData;
        initialize();

        EventQueue.invokeLater(() -> {
            try {
                frame.setVisible(true);
                valueUpdater();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 514, 522);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(19, 82, 477, 199);
        frame.getContentPane().add(scrollPane);

        JList serverlist = new JList();
        serverlist.setForeground(Color.DARK_GRAY);
        serverlist.setBorder(null);
        serverlist.setVisibleRowCount(16);
        serverlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(serverlist);
        serverlist.setLayoutOrientation(JList.VERTICAL);
        serverlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {

                    if(input_username.getText() == null && !input_username.getText().equals("")) {
                        input_username.getText();
                        JOptionPane.showMessageDialog(frame, "Please set your username!");
                        return;
                    }

                    if(Data.alreadyConnected) {
                        JOptionPane.showMessageDialog(frame, "You are already connected to a server!");
                        return;
                    }

                    String selected = (String) serverlist.getSelectedValue();
                    if(selected == null) {
                        return;
                    }

                    if(!selected.contains("[2/2]")) {
                        int port = Integer.valueOf(selected.split(" ")[0]);
                        System.out.println(port);
                        try {
                            client = new Socket(Data.IP, port);
                            Client.data.getConnectionHandler().setPlay_socket(client);
                            Data.username = input_username.getText();
                            Data.alreadyConnected = true;

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("username", Data.username);
                                        for(int i = 0; i < 5; i++) {
                                            writer.write("PacketPlayOutSendUsername;;;" + jsonObject.toJSONString() + "\n");
                                            writer.flush();
                                        }
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                }
                            }, 1000);

                            Client.data.getConnectionHandler().setPlayserverInfoState(new PlayserverInfoState());

                            if(!Client.data.getConnectionHandler().getListeningPlayServer().isAlive()) {
                                Client.data.getConnectionHandler()
                                        .getListeningPlayServer()
                                        .start();
                            }

                            GameGUI gameGUI = new GameGUI(client);
                        } catch (IOException ioException) {
                            JOptionPane.showMessageDialog(frame, "This server is full or offline!");
                        }
                    }else {
                        JOptionPane.showMessageDialog(frame, "This server is full or offline!");
                    }
                }
            }
        });
        defaultListModel = new DefaultListModel<>();

        serverlist.setModel(defaultListModel);
        serverlist.setFont(new Font("Arial", Font.BOLD, 15));
        serverlist.setBounds(32, 199, 366, 227);

        JLabel lblNewLabel = new JLabel("TicTacToe - Hauptmenü");
        lblNewLabel.setFont(new Font("Al Bayan", Font.PLAIN, 16));
        lblNewLabel.setBounds(6, 6, 340, 29);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Majo-Cloud Serverliste");
        lblNewLabel_1.setBounds(19, 54, 340, 16);
        frame.getContentPane().add(lblNewLabel_1);

        JButton btn_connect = new JButton("Connect");
        btn_connect.setBounds(19, 299, 117, 29);

        JLabel lblNewLabel_2 = new JLabel("Proxy-Information");
        lblNewLabel_2.setBounds(19, 359, 130*3, 25);
        lblNewLabel_2.setFont(new Font("Al Bayan", Font.BOLD, 20));
        frame.getContentPane().add(lblNewLabel_2);

        lbl_port = new JLabel("Port: " + proxyData.getPort());
        lbl_port.setBounds(19, 413, 207, 16);
        frame.getContentPane().add(lbl_port);

        lbl_playersonline = new JLabel("Players online: " + proxyData.getPlayers());
        lbl_playersonline.setBounds(19, 437, 207, 16);
        frame.getContentPane().add(lbl_playersonline);


        lbl_playersplaying = new JLabel("Players playing: ");
        lbl_playersplaying.setBounds(19, 461, 207, 16);
        frame.getContentPane().add(lbl_playersplaying);

        lbl_motd = new JLabel("MOTD: " + proxyData.getMotd());
        lbl_motd.setBounds(19, 387, 400, 16);
        frame.getContentPane().add(lbl_motd);

        input_username = new JTextField();
        input_username.setBounds(19, 298, 346, 26);
        frame.getContentPane().add(input_username);
        input_username.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Username");
        lblNewLabel_3.setBounds(18, 282, 169, 16);
        frame.getContentPane().add(lblNewLabel_3);
    }

    public void valueUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lbl_motd.setText("MOTD: " + proxyData.getMotd());
                lbl_port.setText("Port: " + proxyData.getPort());
                lbl_playersonline.setText("Players online: " + proxyData.getPlayers());
                lbl_playersplaying.setText("Players playing: ");

                playersplaying = 0;
                if(Data.serverData.serverCollection.size() != 0) {
                    for (Server cloudServer : Data.serverData.serverCollection) {
                        if (cloudServer.getPort() != 9999) {
                            playersplaying += cloudServer.getPlayerAmount();
                        }
                    }
                    lbl_playersplaying.setText("Players playing: " + playersplaying);
                }

                defaultListModel.clear();
                Data.serverData.serverCollection.forEach(server ->
                        defaultListModel.addElement(server.getPort() + "  - " + server.getMotd()
                        + "   [" + server.getPlayerAmount() + "/2]"));
            }
        }, 0, 1000);
    }

    public Socket getClient() {
        return client;
    }
}
