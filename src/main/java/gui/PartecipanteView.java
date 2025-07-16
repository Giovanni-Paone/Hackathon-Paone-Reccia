package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PartecipanteView {
    private static JFrame framePartecipante;
    private JLabel hackathonLabel;
    private JPanel partecipantePanel;
    private JButton precedentiButton;
    private JButton attualeButton;
    private JScrollBar scrollBar1;
    private JLabel invitiLabel;
    private JButton creaTeamButton;

    public JPanel getPartecipantePanel() {return partecipantePanel;}


    public PartecipanteView(Controller controller) {
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //controller.guardaHackathon(Home.this);
            }
        });
        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //CreazioneTeam.main(Partecipante.this, controller);
            }
        });
    }

    public static void main(String[] args, Controller controller) {
        framePartecipante = new JFrame("Home");
        framePartecipante.setContentPane(new PartecipanteView(controller).partecipantePanel);
        framePartecipante.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePartecipante.setPreferredSize(new Dimension(400, 300));
        framePartecipante.setResizable(false);
        framePartecipante.pack();
        framePartecipante.setLocationRelativeTo(null);
        framePartecipante.setVisible(true);
    }
}


