package gui;

import controller.Controller;
import model.Hackathon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home {
    private static JFrame frameHome;
    private JPanel mainPanel;
    private JTextField searchBar;
    private JButton cercaButton;
    private JScrollBar scrollBar1;
    private JButton notificaButton;
    private JButton creaHackathonButton;
    private Controller controller;

    public Home() {
        controller = new Controller();
        // Add action listeners or other initialization code here

        notificaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Invito.main(null);
            }
        });
        creaHackathonButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CreazioneHackathon.main(null);
            }
        });
        cercaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(2==2) { //controlla se esiste hackathon
                    HackathonGUI.main(null);
                }
            }
        });
    }


    public static void main(String[] args) {
        frameHome = new JFrame("Home");
        frameHome.setContentPane(new Home().mainPanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.setPreferredSize(new Dimension(300, 350));
        frameHome.setResizable(false);
        frameHome.pack();
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);
    }
}
