package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class Invito {
    private static JFrame frameInviti;
    private JPanel invitoPanel;
    private JScrollBar scrollBar1;
    private JLabel invitiLabel;

    public Invito(Controller controller) {}

    public static void main(String[] args, Controller controller) {
            frameInviti = new JFrame("Inviti");
            frameInviti.setContentPane(new Invito(controller).invitoPanel);
            frameInviti.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameInviti.setPreferredSize(new Dimension(300, 320));
            frameInviti.setResizable(true);
            frameInviti.pack();
            frameInviti.setLocationRelativeTo(null);
            frameInviti.setVisible(true);
    }
}
