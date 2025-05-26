package gui;

import controller.Controller;

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
    private JButton areaPersonaleButton;
    private JButton creaHackathonButton;
    private Controller controller;

    public Home(Controller controller) {

        areaPersonaleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AreaPersonale.main(null, controller);
            }
        });
        creaHackathonButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CreazioneHackathon.main(null, controller);
            }
        });
        cercaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(2==2) { //controlla se esiste hackathon
                    HackathonGUI.main(null, controller);
                }
            }
        });
    }


    public static void main(String[] args, Controller controller) {
        frameHome = new JFrame("Home");
        frameHome.setContentPane(new Home(controller).mainPanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.setPreferredSize(new Dimension(400, 350));
        frameHome.setResizable(false);
        frameHome.pack();
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);
    }
}
