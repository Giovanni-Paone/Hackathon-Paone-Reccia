package gui;

import controller.Controller;
import model.Hackathon;
import model.Team;
import model.UtenteBase;

import javax.swing.*;
import java.awt.*;
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
            // Pannello per ogni team, il bordo mostra già "Team: nomeTeam"
            JPanel panelTeam = new JPanel();
            panelTeam.setLayout(new BoxLayout(panelTeam, BoxLayout.Y_AXIS));
            panelTeam.setBorder(BorderFactory.createTitledBorder("Team: " + team.NOME_TEAM));

            // Bottone gestisci team
            JButton btnTeam = new JButton("Gestisci");
            if (utente.getRuolo() != 0 && !hackathon.getOrganizzatore().equals(utente.USERNAME)) {
                btnTeam.setVisible(false);
            }
            panelTeam.add(btnTeam);

            // Partecipanti (spostati a destra)
            for (String username : team.partecipanti) {
                JPanel panelPartecipante = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panelPartecipante.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); // indentazione
                JLabel lblUser = new JLabel(username);
                JButton btnUser = new JButton("Rimuovi");
                if (utente.getRuolo() != 0 && !hackathon.getOrganizzatore().equals(utente.USERNAME)) {
                    btnUser.setVisible(false);
                }
                panelPartecipante.add(lblUser);
                panelPartecipante.add(btnUser);

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
