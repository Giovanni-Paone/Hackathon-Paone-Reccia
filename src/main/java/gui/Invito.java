package gui;

import javax.swing.*;
import java.awt.*;

public class Invito {
    private static JFrame frameInviti;
    private JPanel invitoPanel;
    private JScrollBar scrollBar1;
    private JLabel invitiLabel;

    public static void main(String[] args) {
            frameInviti = new JFrame("Inviti");
            frameInviti.setContentPane(new Invito().invitoPanel);
            frameInviti.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameInviti.setPreferredSize(new Dimension(300, 320));
            frameInviti.setResizable(true);
            frameInviti.pack();
            frameInviti.setLocationRelativeTo(null);
            frameInviti.setVisible(true);
    }
}
