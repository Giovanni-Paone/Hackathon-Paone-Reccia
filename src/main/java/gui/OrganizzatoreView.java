package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrganizzatoreView {
    private static JFrame frameOrganizzatoreView;
    private JPanel organizzatorePanel;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel dataInizioLabel;
    private JLabel dataFineLabel;
    private JLabel giudiciLabel;
    private JTextField nomeInvitatoText;
    private JLabel dimensioneMassimaTeamLabel;
    private JProgressBar progressBar1;
    private JButton apriIscrizioniButton;
    private JButton invitaGiudiciButton;
    private JButton effettuaCambiamentiButton;
    private JButton precedentiButton;
    private JScrollBar scrollBar1;
    private JButton teamButton;
    private JButton partecipantiButton;

    public JTextField getNomeInvitatoText() {return nomeInvitatoText;}

    public OrganizzatoreView(Controller controller) {
        invitaGiudiciButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.invitaGiudice(OrganizzatoreView.this);
            }
        });
        effettuaCambiamentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

            }
            //controlla se la data è prima dell'inizio
            //apri una nuova GUI uguale a creazione Hackathon con i dati già inseriti
        });

        apriIscrizioniButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showConfirmDialog(organizzatorePanel,
                        "Vuoi aprire le iscrizioni?",
                        "Conferma",
                        JOptionPane.INFORMATION_MESSAGE);
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
        frameOrganizzatoreView = new JFrame("Hackathon");
        frameOrganizzatoreView.setContentPane(new OrganizzatoreView(controller).organizzatorePanel);
        frameOrganizzatoreView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameOrganizzatoreView.setPreferredSize(new Dimension(450, 450));
        frameOrganizzatoreView.setResizable(false);
        frameOrganizzatoreView.pack();
        frameOrganizzatoreView.setLocationRelativeTo(null);
        frameOrganizzatoreView.setVisible(true);
    }
}
