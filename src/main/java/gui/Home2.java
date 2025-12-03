package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home2 {
    private static JFrame frameOrganizzatoreHome;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JLabel hackathonLabel;
    private JPanel OrganizzatoreHomePanel;
    private JButton cercaButton;

    public JPanel getOrganizzatoreHomePanel() {return OrganizzatoreHomePanel;}

    public Home2(Controller controller, Utente utente) {
        if(utente.getRuolo() == 0){
            cercaButton.setVisible(false);
        }

        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(Home2.this, utente);
            }
        });
        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                VisualizzaHackathon.main(controller, utente, null);
            }
        });
        cercaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CercaUtenti.main(controller, utente, null);
            }
        });
    }

    public static void main(Controller controller, Utente utente) {
        frameOrganizzatoreHome = new JFrame("Home");
        frameOrganizzatoreHome.setContentPane(new Home2(controller, utente).OrganizzatoreHomePanel);
        frameOrganizzatoreHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameOrganizzatoreHome.setPreferredSize(new Dimension(400, 250));
        frameOrganizzatoreHome.setResizable(false);
        frameOrganizzatoreHome.pack();
        frameOrganizzatoreHome.setLocationRelativeTo(null);
        frameOrganizzatoreHome.setVisible(true);
    }
}

