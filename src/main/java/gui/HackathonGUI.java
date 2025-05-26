package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HackathonGUI {
    private static JFrame frameHackathon;
    private JPanel hackathonPanel;;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel organizzatoreLabel;
    private JLabel dataInizioLabel;
    private JLabel numeroSquadreLabel;
    private JLabel postiRimanentiLabel;
    private JLabel dimensioneMassimaTeamLabel;
    private JButton iscrivitiButton;
    private JProgressBar progressBar1;

    public HackathonGUI(Controller controller) {
        iscrivitiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(2==2) //se iscriversi è permesso
                    JOptionPane.showConfirmDialog(hackathonPanel,
                            "Sei sicuro?",
                            "Sei sicuro?",
                            JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args, Controller controller) {
        frameHackathon = new JFrame("Hackathon");
        frameHackathon.setContentPane(new HackathonGUI(controller).hackathonPanel);
        frameHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameHackathon.setPreferredSize(new Dimension(450, 400));
        frameHackathon.setResizable(false);
        frameHackathon.pack();
        frameHackathon.setLocationRelativeTo(null);
        frameHackathon.setVisible(true);
    }
}
