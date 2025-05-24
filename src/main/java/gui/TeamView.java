package gui;

import javax.swing.*;
import java.awt.*;

public class TeamView {
    private static JFrame frameTeamView;
    private JPanel teamViewPanel;
    private JLabel nomeTeamLabel;
    private JLabel partecipantiLabel;
    private JScrollBar scrollBar1;
    private JLabel progressiLabel;


    public static void main(String[] args) {
        frameTeamView = new JFrame("Creazione Hackathon");
        frameTeamView.setContentPane(new TeamView().teamViewPanel);
        frameTeamView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameTeamView.setPreferredSize(new Dimension(300, 400));
        frameTeamView.setResizable(false);
        frameTeamView.pack();
        frameTeamView.setLocationRelativeTo(null);
        frameTeamView.setVisible(true);
    }
}
