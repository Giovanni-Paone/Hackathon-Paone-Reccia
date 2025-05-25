package gui;

import javax.swing.*;
import java.awt.*;

public class GiudiceView {
    private static JFrame frameGiudiceView;
    private JPanel giudicePanel;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel organizzatoreLabel;
    private JLabel dataInizioLabel;
    private JLabel dataFineLabel;
    private JProgressBar progressBar1;
    private JLabel squadreLabel;
    private JLabel postiRimanentiLabel;
    private JLabel dimensioniMassimeTeamLabel;
    private JScrollBar scrollBar1;
    private JLabel giudiciLabel;
    private JScrollBar scrollBar2;

    public static void main(String[] args) {
        frameGiudiceView = new JFrame("Hackathon");
        frameGiudiceView.setContentPane(new GiudiceView().giudicePanel);
        frameGiudiceView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGiudiceView.setPreferredSize(new Dimension(420, 450));
        frameGiudiceView.setResizable(false);
        frameGiudiceView.pack();
        frameGiudiceView.setLocationRelativeTo(null);
        frameGiudiceView.setVisible(true);
    }
}
