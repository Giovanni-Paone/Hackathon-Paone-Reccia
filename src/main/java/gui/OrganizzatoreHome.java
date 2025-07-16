package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrganizzatoreHome {
    private static JFrame frameOrganizzatoreHome;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JLabel hackathonLabel;
    private JPanel OrganizzatoreHomePanel;

    public JPanel getOrganizzatoreHomePanel() {return OrganizzatoreHomePanel;}

    public OrganizzatoreHome(Controller controller) {
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(OrganizzatoreHome.this);
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
        frameOrganizzatoreHome = new JFrame("Home");
        frameOrganizzatoreHome.setContentPane(new OrganizzatoreHome(controller).OrganizzatoreHomePanel);
        frameOrganizzatoreHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameOrganizzatoreHome.setPreferredSize(new Dimension(400, 250));
        frameOrganizzatoreHome.setResizable(false);
        frameOrganizzatoreHome.pack();
        frameOrganizzatoreHome.setLocationRelativeTo(null);
        frameOrganizzatoreHome.setVisible(true);
    }
}

