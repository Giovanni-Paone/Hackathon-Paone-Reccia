package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home {
    private static JFrame frameHome;
    private JPanel homePanel;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JScrollBar scrollBar1;
    private JLabel invitiLabel;
    private JLabel hackathonLabel;

    public JPanel getHomePanel() {return homePanel;}


    public Home(Controller controller) {
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(Home.this);
            }
        });
        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.precedentiHackathon();
            }
        });
    }

    public static void main(String[] args, Controller controller) {
        frameHome = new JFrame("Home");
        frameHome.setContentPane(new Home(controller).homePanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.setPreferredSize(new Dimension(400, 300));
        frameHome.setResizable(false);
        frameHome.pack();
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);
    }
}

