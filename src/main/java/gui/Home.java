package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home {
    private JPanel mainPanel;
    private JTextField searchBar;
    private JButton cercaButton;
    private JScrollBar scrollBar1;
    private JButton notificaButton;
    private JButton creaHackathonButton;
    private static JFrame frameHome;
    private Controller controller;

    public static void main(String[] args) {
        frameHome = new JFrame("Home");
        frameHome.setContentPane(new Home().mainPanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.setPreferredSize(new Dimension(300, 200));
        frameHome.setResizable(false);
        frameHome.pack();
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);


    }

    public Home() {
        controller = new Controller();
        // Add action listeners or other initialization code here

        notificaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CreazioneHackathon.main(null);
            }
        });
        creaHackathonButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }


}
