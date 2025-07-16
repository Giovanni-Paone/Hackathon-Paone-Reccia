package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VisualizzaTeam {
    private static JFrame frameHome;
    private JPanel visualizzaTeamPanel;
    private JScrollBar scrollBar1;
    private JLabel teamLabel;

    public VisualizzaTeam(Controller controller) {
        //fa visualizzare i team con la base di dati
    }

    public static void main(String[] args, Controller controller) {
        frameHome = new JFrame("Hackathon");
        frameHome.setContentPane(new VisualizzaTeam(controller).visualizzaTeamPanel);
        frameHome.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameHome.setPreferredSize(new Dimension(450, 400));
        frameHome.setResizable(false);
        frameHome.pack();
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);
    }
}
