package gui;

import controller.Controller;
import model.Hackathon;
import model.Team;
import model.UtenteBase;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class VisualizzaIscritti {
    private static JFrame frameVisualizzaIscritti;
    private JPanel visualizzaIscrittiPanel;
    private JScrollBar scrollBar1;
    private JScrollPane scrollPane;

    public VisualizzaIscritti(Controller controller, Hackathon hackathon, ArrayList<Team> teams, UtenteBase utente) {
        JPanel panelTeamContainer = new JPanel();
        panelTeamContainer.setLayout(new BoxLayout(panelTeamContainer, BoxLayout.Y_AXIS));

        for (Team team : teams) {
            JPanel panelTeam = new JPanel();
            panelTeam.setLayout(new BoxLayout(panelTeam, BoxLayout.Y_AXIS));
            panelTeam.setBorder(BorderFactory.createTitledBorder("Team: " + team.NOME_TEAM));

            JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JButton buttonRimuoviTeam = new JButton("Rimuovi");
            Date oggi = Date.valueOf(LocalDate.now());
            if ((utente.getRuolo() > 0 || team.NOME_TEAM.equals("Senza_Team") || hackathon.getDataFine().before(oggi)
                   || (utente.getRuolo() == -1 && !hackathon.getOrganizzatore().equals(utente.USERNAME)))
                ) {
                buttonRimuoviTeam.setVisible(false);
            }
            buttonRimuoviTeam.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    controller.rimuoviTeam(team, hackathon);
                    panelTeamContainer.remove(panelTeam);
                }
            });

            //fare il bottone aggiungi voto

            JButton buttonFile = new JButton("File");
            if (((utente.getRuolo() > 0 || (utente.getRuolo() == -1 && !hackathon.getOrganizzatore().equals(utente.USERNAME)))
                && hackathon.getDataFine().after(oggi)) || team.NOME_TEAM.equals("Senza_Team")) {
                buttonFile.setVisible(false);
            }
            buttonFile.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    controller.visualizzaFile(team.NOME_TEAM, hackathon);
                }
            });

            panelBottoni.add(buttonRimuoviTeam);
            panelBottoni.add(buttonFile);
            panelTeam.add(panelBottoni);

            for (String username : team.partecipanti) {
                JPanel panelPartecipante = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panelPartecipante.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

                JLabel labelUser = new JLabel(username);
                JButton buttonRimuoviUser = new JButton("Rimuovi");
                if (utente.getRuolo() != 0 && !hackathon.getOrganizzatore().equals(utente.USERNAME)) {
                    buttonRimuoviUser.setVisible(false);
                }
                buttonRimuoviUser.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        controller.squalifica(username, hackathon);
                        labelUser.setVisible(false);
                        buttonRimuoviUser.setVisible(false);
                    }
                });

                panelPartecipante.add(labelUser);
                panelPartecipante.add(buttonRimuoviUser);
                panelTeam.add(panelPartecipante);
            }

            panelTeamContainer.add(panelTeam);
        }

        scrollPane = new JScrollPane(panelTeamContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        visualizzaIscrittiPanel.setLayout(new BorderLayout());
        visualizzaIscrittiPanel.add(scrollPane, BorderLayout.CENTER);
    }




    public static void main(Controller controller, model.Hackathon hackathon, ArrayList<Team> teams, UtenteBase utente) {
        frameVisualizzaIscritti = new JFrame("Iscritti");
        frameVisualizzaIscritti.setContentPane(new VisualizzaIscritti(controller, hackathon, teams, utente).visualizzaIscrittiPanel);
        frameVisualizzaIscritti.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameVisualizzaIscritti.setPreferredSize(new Dimension(450, 300));
        frameVisualizzaIscritti.setResizable(true);
        frameVisualizzaIscritti.pack();
        frameVisualizzaIscritti.setLocationRelativeTo(null);
        frameVisualizzaIscritti.setVisible(true);
    }
}
