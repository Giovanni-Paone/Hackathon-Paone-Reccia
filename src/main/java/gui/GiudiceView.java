package gui;

import javax.swing.*;
import java.awt.*;

public class GiudiceView {
    private static JFrame frameGiudiceView;
    private JPanel giudicePanel;

    public static void main(String[] args) {
        frameGiudiceView = new JFrame("Hackathon");
        frameGiudiceView.setContentPane(new GiudiceView().giudicePanel);
        frameGiudiceView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGiudiceView.setPreferredSize(new Dimension(300, 350));
        frameGiudiceView.setResizable(false);
        frameGiudiceView.pack();
        frameGiudiceView.setLocationRelativeTo(null);
        frameGiudiceView.setVisible(true);
    }
}
