package gui;

import controller.Controller;
import model.Organizzatore;
import model.Utente;
import model.UtenteBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Hackathon {
    private static JFrame frameHackathon;
    private JPanel hackathonPanel;
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
    private JLabel titolo;
    private JLabel sede;
    private JLabel organizzatore;
    private JLabel dataInizio;
    private JLabel dataFine;
    private JLabel nTeam;
    private JLabel nMaxTeamSize;
    private JLabel postiRimanenti;
    private JButton apriIscrizioniButton;
    private JButton effettuaCambiamentiButton;
    private JButton partecipaButton;
    private JPanel giudiciPanel;

    public JFrame getFrameHackathon() {return frameHackathon;}
    public JPanel getHackathonPanel() {return hackathonPanel;}

    public Hackathon(Controller controller, model.Hackathon hackathon, ArrayList<Organizzatore> giudici, UtenteBase utente) {
        setHackathon(controller, hackathon, utente);
        aggiornaGiudiciPanel(giudici);

        teamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.visualizzaTeam(hackathon, utente);
            }
        });

        partecipantiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.visualizzaIscritti(hackathon, utente);
            }
        });

        effettuaCambiamentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                controller.modificaHackathon(Hackathon.this, (Organizzatore) utente, hackathon, giudici);
            }
        });

        apriIscrizioniButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.apriRegistrazioni(Hackathon.this, hackathon, giudici, (Organizzatore) utente);
            }
        });
        partecipaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.partecipa(Hackathon.this, (Utente) utente, hackathon);
            }
        });
    }

    private void setHackathon(Controller controller, model.Hackathon hackathon, UtenteBase utente) {
        titolo.setText(hackathon.getTitolo());
        sede.setText(hackathon.getSede());
        organizzatore.setText(hackathon.getOrganizzatore());
        dataInizio.setText(String.valueOf(hackathon.getDataInizio()));
        dataFine.setText(String.valueOf(hackathon.getDataFine()));
        nTeam.setText(String.valueOf(hackathon.getNTeamIscritti()));
        nMaxTeamSize.setText(String.valueOf(hackathon.getMaxTeamSize()));
        postiRimanenti.setText(String.valueOf(hackathon.getMaxIscritti() - hackathon.getNPartecipantiIscritti()));
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(hackathon.getMaxIscritti());
        progressBar1.setValue(hackathon.getNPartecipantiIscritti());

        if (hackathon.getDataFine().before(controller.oggi)) {
            apriIscrizioniButton.setVisible(false);
            effettuaCambiamentiButton.setVisible(false);
            partecipaButton.setVisible(false);
            partecipantiButton.setVisible(false);
        }
        else {
            if (!utente.USERNAME.equals(hackathon.getOrganizzatore()) ||
                    hackathon.getAperturaRegistrazioni()) {
                apriIscrizioniButton.setVisible(false);
                effettuaCambiamentiButton.setVisible(false);
            }

            if (!hackathon.getAperturaRegistrazioni()) {
                partecipantiButton.setVisible(false);
                partecipaButton.setVisible(false);
            } else if(utente.getRuolo() != 1)
                partecipaButton.setVisible(false);
        }
    }

    private void aggiornaGiudiciPanel(ArrayList<Organizzatore> giudici) {
        giudiciPanel.removeAll();
        giudiciPanel.setLayout(new BoxLayout(giudiciPanel, BoxLayout.Y_AXIS));

        if(giudici != null) {
            for (int i = 0; i < giudici.size(); i += 2) {
                JPanel riga = new JPanel(new BorderLayout());

                JLabel leftLabel = new JLabel(giudici.get(i).USERNAME);
                riga.add(leftLabel, BorderLayout.WEST);

                if (i + 1 < giudici.size()) {
                    JLabel rightLabel = new JLabel(giudici.get(i + 1).USERNAME);
                    riga.add(rightLabel, BorderLayout.EAST);
                }

                giudiciPanel.add(riga);
            }

            giudiciPanel.revalidate();
            giudiciPanel.repaint();
        }
    }



    public static void main(Controller controller, model.Hackathon hackathon, ArrayList<Organizzatore> giudici, UtenteBase utente) {
        frameHackathon = new JFrame("Hackathon");
        frameHackathon.setContentPane(new Hackathon(controller, hackathon, giudici, utente).hackathonPanel);
        frameHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameHackathon.setPreferredSize(new Dimension(450, 400));
        frameHackathon.setResizable(false);
        frameHackathon.pack();
        frameHackathon.setLocationRelativeTo(null);
        frameHackathon.setVisible(true);
    }
}
