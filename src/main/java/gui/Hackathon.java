package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Hackathon {
    private static JFrame frameHackathon;
    private JPanel hackathonPanel;;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel organizzatoreLabel;
    private JLabel dataInizioLabel;
    private JLabel numeroTeamLabel;
    private JLabel postiRimanentiLabel;
    private JLabel dimensioneMassimaTeamLabel;
    private JProgressBar progressBar1;
    private JButton teamButton;
    private JScrollBar scrollBar1;
    private JButton partecipantiButton;
    private JButton precedentiButton;

    public Hackathon(Controller controller) {
        teamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.visualizzaTeam();
            }
        });
        partecipantiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.visualizzaIscritti();
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
        frameHackathon = new JFrame("Hackathon");
        frameHackathon.setContentPane(new Hackathon(controller).hackathonPanel);
        frameHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameHackathon.setPreferredSize(new Dimension(450, 400));
        frameHackathon.setResizable(false);
        frameHackathon.pack();
        frameHackathon.setLocationRelativeTo(null);
        frameHackathon.setVisible(true);
    }
}
