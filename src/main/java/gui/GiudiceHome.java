package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GiudiceHome {
    private static JFrame frameGiudiceHome;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JLabel hackathonLabel;
    private JPanel GiudiceHomePanel;

    public JPanel getGiudiceHomePanel() {
        return GiudiceHomePanel;
    }

    public GiudiceHome(Controller controller) {
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(GiudiceHome.this);
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
        frameGiudiceHome = new JFrame("Home");
        frameGiudiceHome.setContentPane(new GiudiceHome(controller).GiudiceHomePanel);
        frameGiudiceHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGiudiceHome.setPreferredSize(new Dimension(400, 250));
        frameGiudiceHome.setResizable(false);
        frameGiudiceHome.pack();
        frameGiudiceHome.setLocationRelativeTo(null);
        frameGiudiceHome.setVisible(true);
    }
}
