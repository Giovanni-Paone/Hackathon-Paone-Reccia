package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GiudiceView {
    private static JFrame frameGiudiceView;
    private JPanel giudicePanel;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel organizzatoreLabel;
    private JLabel dataInizioLabel;
    private JLabel dataFineLabel;
    private JProgressBar progressBar1;
    private JLabel postiRimanentiLabel;
    private JLabel dimensioniMassimeTeamLabel;
    private JLabel giudiciLabel;
    private JButton precedentiButton;
    private JButton teamButton;
    private JButton partecipantiButton;

    public GiudiceView(Controller controller) {
        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.precedentiHackathon();
            }
        });
        teamButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.visualizzaTeam();
                //non questo ma un altro uguale con permessi per i giudici
            }
        });
        partecipantiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.visualizzaIscritti();
                //non questo ma un altro uguale con permessi per i giudici
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
        frameGiudiceView = new JFrame("Hackathon");
        frameGiudiceView.setContentPane(new GiudiceView(controller).giudicePanel);
        frameGiudiceView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGiudiceView.setPreferredSize(new Dimension(420, 450));
        frameGiudiceView.setResizable(false);
        frameGiudiceView.pack();
        frameGiudiceView.setLocationRelativeTo(null);
        frameGiudiceView.setVisible(true);
    }
}
