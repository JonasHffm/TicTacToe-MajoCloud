package client.gui;

import client.main.Client;
import client.util.Data;
import client.util.PlayserverInfoState;
import de.majo.tictactoe.cloud.main.Main;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.*;
import java.util.Timer;

public class GameGUI {

    private JFrame frame;
    public static Map<Integer, JLabel> fieldMap = new HashMap<>();

    private JLabel lbl_playerone;
    private JLabel lbl_playertwo;

    private JLabel lbl_turn;
    private JLabel lbl_symboluser;

    private Socket client;
    private BufferedWriter writer;

    private Timer timer;

    private boolean pressDelay = false;

    public GameGUI(Socket client) {
        this.client = client;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initialize();



        EventQueue.invokeLater(() -> {
            try {
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 600);
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel lbl_a = new JLabel("");
        lbl_a.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_a.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_a.setBackground(Color.WHITE);
        lbl_a.setBounds(99, 118, 128, 128);
        lbl_a.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_a);

        JLabel lbl_b = new JLabel("");
        lbl_b.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_b.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_b.setBounds(228, 118, 128, 128);
        lbl_b.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_b);

        JLabel lbl_c = new JLabel("");
        lbl_c.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_c.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_c.setBounds(357, 118, 128, 128);
        lbl_c.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_c);

        JLabel lbl_d = new JLabel("");
        lbl_d.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_d.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_d.setBackground(Color.WHITE);
        lbl_d.setBounds(99, 247, 128, 128);
        lbl_d.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_d);

        JLabel lbl_e = new JLabel("");
        lbl_e.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_e.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_e.setBounds(228, 247, 128, 128);
        lbl_e.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_e);

        JLabel lbl_f = new JLabel("");
        lbl_f.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_f.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_f.setBounds(357, 247, 128, 128);
        lbl_f.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_f);

        JLabel lbl_i = new JLabel("");
        lbl_i.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_i.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_i.setBounds(357, 377, 128, 128);
        lbl_i.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_i);

        JLabel lbl_h = new JLabel("");
        lbl_h.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_h.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_h.setBounds(228, 377, 128, 128);
        lbl_h.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_h);

        JLabel lbl_g = new JLabel("");
        lbl_g.setFont(new Font("Lucida Grande", Font.PLAIN, 88));
        lbl_g.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_g.setBackground(Color.WHITE);
        lbl_g.setBounds(99, 377, 128, 128);
        lbl_g.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_g);

        fieldMap.put(0, lbl_a);
        fieldMap.put(1, lbl_b);
        fieldMap.put(2, lbl_c);
        fieldMap.put(3, lbl_d);
        fieldMap.put(4, lbl_e);
        fieldMap.put(5, lbl_f);
        fieldMap.put(6, lbl_g);
        fieldMap.put(7, lbl_h);
        fieldMap.put(8, lbl_i);

        fieldMap.forEach((key, label) -> label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(key);
                PlayserverInfoState playserverInfoState = Client.data.getConnectionHandler().getPlayserverInfoState();

                if(!pressDelay) {
                    if (playserverInfoState.getTurn().equals(Data.username)) {
                        if (!label.getText().equals("X") && !label.getText().equals("O")) {
                            JSONObject object = new JSONObject();
                            ArrayList<String> newField = new ArrayList<>();
                            newField.addAll(playserverInfoState.getField());
                            newField.set(key, playserverInfoState.getPlayerSymbols().get(Data.username).toLowerCase(Locale.ROOT));
                            object.put("gamefield", newField);
                            pressDelay = true;
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    pressDelay = false;
                                }
                            }, 1000*2);
                            try {
                                writer.write("PacketPlayOutChangeGame;;;" + object.toJSONString() + "\n");
                                writer.flush();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "That field is already blocked!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "It is not your turn!");
                    }
                }
            }
        }));

        JButton exit_button = new JButton("Exit");
        exit_button.setBounds(6, 6, 117, 29);
        exit_button.addActionListener(e -> {
            System.out.println(client);
            Data.gameQuit = true;
            try {
                writer.write("LOGOUT\n");
                writer.flush();
                writer.close();
                client.close();
                timer.cancel();
                MainGUI.timer.cancel();
                Client.data.getConnectionHandler().setPlay_socket(null);
                //Client.data.getConnectionHandler().setListeningPlayServer(
                //        new Thread(Client.data.getConnectionHandler().getOnListenEventPlayServer())
                //);
                frame.dispose();
                Data.alreadyConnected = false;
                Data.gameQuit = false;
                Client.data = new Data();
            } catch (IOException ignored) {
                frame.dispose();
                Data.alreadyConnected = false;
                Client.data = new Data();
            }
        });
        frame.getContentPane().add(exit_button);

        JLabel lblNewLabel = new JLabel("Player 1:");
        lblNewLabel.setBounds(424, 11, 61, 16);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblPlayer = new JLabel("Player 2:");
        lblPlayer.setBounds(424, 54, 61, 16);
        frame.getContentPane().add(lblPlayer);

        lbl_playerone = new JLabel("");
        lbl_playerone.setBounds(424, 28, 170, 16);
        frame.getContentPane().add(lbl_playerone);

        lbl_playertwo = new JLabel("");
        lbl_playertwo.setBounds(424, 70, 170, 16);
        frame.getContentPane().add(lbl_playertwo);

        lbl_turn = new JLabel("");
        lbl_turn.setBounds(249, 28, 170, 16);
        frame.getContentPane().add(lbl_turn);

        lbl_symboluser = new JLabel("");
        lbl_symboluser.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
        lbl_symboluser.setBounds(249, 54, 46, 46);
        frame.getContentPane().add(lbl_symboluser);

        JLabel lbl_spielzug = new JLabel("Spielzug:");
        lbl_spielzug.setBounds(249, 11, 107, 16);
        frame.getContentPane().add(lbl_spielzug);

        updateGameUI();
    }


    public void updateGameUI() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                PlayserverInfoState playserverInfoState = Client.data.getConnectionHandler().getPlayserverInfoState();
                if(playserverInfoState.getUser().size() == 2) {
                    lbl_playerone.setText(playserverInfoState.getUser().get(0));
                    lbl_playertwo.setText(playserverInfoState.getUser().get(1));
                    lbl_turn.setText(playserverInfoState.getTurn());
                    lbl_symboluser.setText(playserverInfoState.getPlayerSymbols().get(playserverInfoState.getTurn()));
                }else {
                    if(playserverInfoState.getUser().size() >= 1) {
                        lbl_playerone.setText(playserverInfoState.getUser().get(0));
                        lbl_turn.setText("-");
                        lbl_playertwo.setText("-");
                    }
                }

                for(int i = 0; i < playserverInfoState.getField().size(); i++) {
                    if(playserverInfoState.getField().get(i).equals("x")) {
                        fieldMap.get(i).setText("X");
                    }else if(playserverInfoState.getField().get(i).equals("o")) {
                        fieldMap.get(i).setText("O");
                    }
                }
            }
        }, 0, 100);
    }

}
