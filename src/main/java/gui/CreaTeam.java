package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreaTeam {
    private static JFrame frameCreaTeam;
    private JPanel creaTeamPanel;
    private JLabel nomeTeamLabel;
    private JTextField nomeTeam;
    private JButton confermaButton;

    public JFrame getFrameCreaTeam() {return frameCreaTeam;}
    public JPanel getCreaTeamPanel() {return creaTeamPanel;}
    public JTextField getNomeTeam() {return nomeTeam;}

    public CreaTeam(Controller controller, Utente utente, Home home) {
        nomeTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.creaTeam(CreaTeam.this, utente, home);
            }
        });
        confermaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.creaTeam(CreaTeam.this, utente, home);
            }
        });
    }

    public static void main(Controller controller, Utente utente, Home home) {
        frameCreaTeam = new JFrame("Creazione Team");
        frameCreaTeam.setContentPane(new CreaTeam(controller, utente, home).creaTeamPanel);
        frameCreaTeam.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameCreaTeam.setPreferredSize(new Dimension(300, 250));
        frameCreaTeam.setResizable(false);
        frameCreaTeam.pack();
        frameCreaTeam.setLocationRelativeTo(null);
        frameCreaTeam.setVisible(true);
    }
}
