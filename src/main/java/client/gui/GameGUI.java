package client.gui;

import client.main.Client;
import client.util.Data;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;

public class GameGUI {

    private JFrame frame;
    public static Map<String, JLabel> fieldMap = new HashMap<>();

    public GameGUI() {
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

        JLabel lbl_a = new JLabel("");
        lbl_a.setBackground(Color.WHITE);
        lbl_a.setBounds(99, 118, 128, 128);
        lbl_a.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_a);

        JLabel lbl_b = new JLabel("");
        lbl_b.setBounds(228, 118, 128, 128);
        lbl_b.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_b);

        JLabel lbl_c = new JLabel("");
        lbl_c.setBounds(357, 118, 128, 128);
        lbl_c.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_c);

        JLabel lbl_d = new JLabel("");
        lbl_d.setBackground(Color.WHITE);
        lbl_d.setBounds(99, 247, 128, 128);
        lbl_d.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_d);

        JLabel lbl_e = new JLabel("");
        lbl_e.setBounds(228, 247, 128, 128);
        lbl_e.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_e);

        JLabel lbl_f = new JLabel("");
        lbl_f.setBounds(357, 247, 128, 128);
        lbl_f.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_f);

        JLabel lbl_i = new JLabel("");
        lbl_i.setBounds(357, 377, 128, 128);
        lbl_i.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_i);

        JLabel lbl_h = new JLabel("");
        lbl_h.setBounds(228, 377, 128, 128);
        lbl_h.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_h);

        JLabel lbl_g = new JLabel("");
        lbl_g.setBackground(Color.WHITE);
        lbl_g.setBounds(99, 377, 128, 128);
        lbl_g.setBorder(new LineBorder(Color.BLACK, 2));
        frame.getContentPane().add(lbl_g);

        fieldMap.put("a", lbl_a);
        fieldMap.put("b", lbl_b);
        fieldMap.put("c", lbl_c);
        fieldMap.put("d", lbl_d);
        fieldMap.put("e", lbl_e);
        fieldMap.put("f", lbl_f);
        fieldMap.put("g", lbl_g);
        fieldMap.put("h", lbl_h);
        fieldMap.put("i", lbl_i);

        JButton exit_button = new JButton("Exit");
        exit_button.setBounds(6, 6, 117, 29);
        exit_button.addActionListener(e -> {
            Socket socket = Client.data.getConnectionHandler().getPlay_socket();
            try {
                socket.close();
                Client.data.getConnectionHandler().setPlay_socket(null);
                frame.dispose();
                Data.alreadyConnected = false;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        frame.getContentPane().add(exit_button);

        JLabel lblNewLabel = new JLabel("Player 1:");
        lblNewLabel.setBounds(424, 11, 61, 16);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblPlayer = new JLabel("Player 2:");
        lblPlayer.setBounds(424, 54, 61, 16);
        frame.getContentPane().add(lblPlayer);

        JLabel lbl_playerone = new JLabel("");
        lbl_playerone.setBounds(424, 28, 170, 16);
        frame.getContentPane().add(lbl_playerone);

        JLabel lbl_playertwo = new JLabel("");
        lbl_playertwo.setBounds(424, 70, 170, 16);
        frame.getContentPane().add(lbl_playertwo);
    }

}
