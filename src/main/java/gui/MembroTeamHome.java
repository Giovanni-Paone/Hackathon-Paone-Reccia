package gui;

import controller.Controller;
import model.MembroTeam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MembroTeamHome {
    private static JFrame frameMembroTeamView;
    private JPanel membroTeamViewPanel;
    private JLabel hackathonLabel;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JButton fileButton;
    private JButton aggiungiFileButton;
    private JButton invitaButton;
    private JLabel teamLabel;
    private JPanel membriPanelContainer;

    public MembroTeamHome(Controller controller, MembroTeam utente) {
        teamLabel.setText("Team: " + utente.TEAM.NOME_TEAM);

        // aggiungiamo le JLabel dei membri dentro il panel già presente nel designer
        membriPanelContainer.setLayout(new BoxLayout(membriPanelContainer, BoxLayout.Y_AXIS));
        for (String membro : utente.TEAM.partecipanti) {
            JLabel membroLabel = new JLabel(membro);
            membroLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            membriPanelContainer.add(membroLabel);
        }

        // aggiorniamo la GUI
        membriPanelContainer.revalidate();
        membriPanelContainer.repaint();

        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.precedentiHackathon();
            }
        });
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(MembroTeamHome.this, utente);
            }
        });
        invitaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CercaUtenti.main(controller, utente, null);
            }
        });
    }


    public static void main(Controller controller, MembroTeam utente) {
        frameMembroTeamView = new JFrame("Hackathon");
        frameMembroTeamView.setContentPane(new MembroTeamHome(controller, utente).membroTeamViewPanel);
        frameMembroTeamView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameMembroTeamView.setPreferredSize(new Dimension(450, 400));
        frameMembroTeamView.setResizable(false);
        frameMembroTeamView.pack();
        frameMembroTeamView.setLocationRelativeTo(null);
        frameMembroTeamView.setVisible(true);
    }
}
