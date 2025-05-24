package gui;

import javax.swing.*;
import java.awt.*;

public class OrganizzatoreView {
    private static JFrame frameOrganizzatoreView;
    private JPanel organizzatorePanel;


    public static void main(String[] args) {
        frameOrganizzatoreView = new JFrame("Hackathon");
        frameOrganizzatoreView.setContentPane(new OrganizzatoreView().organizzatorePanel);
        frameOrganizzatoreView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameOrganizzatoreView.setPreferredSize(new Dimension(300, 350));
        frameOrganizzatoreView.setResizable(false);
        frameOrganizzatoreView.pack();
        frameOrganizzatoreView.setLocationRelativeTo(null);
        frameOrganizzatoreView.setVisible(true);
    }
}
