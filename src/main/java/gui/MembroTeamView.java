package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MembroTeamView {
    private static JFrame frameMembroTeamView;
    private JPanel membroTeamViewPanel;
    private JLabel hackathonLabel;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JButton fileButton;
    private JButton aggiungiFileButton;

    public MembroTeamView(Controller controller) {

        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.precedentiHackathon();
            }
        });
    }

    public static void main(String[] args, Controller controller) {
        frameMembroTeamView = new JFrame("Hackathon");
        frameMembroTeamView.setContentPane(new MembroTeamView(controller).membroTeamViewPanel);
        frameMembroTeamView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameMembroTeamView.setPreferredSize(new Dimension(450, 400));
        frameMembroTeamView.setResizable(false);
        frameMembroTeamView.pack();
        frameMembroTeamView.setLocationRelativeTo(null);
        frameMembroTeamView.setVisible(true);
    }
}
