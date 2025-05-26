package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AreaPersonale {
    private static JFrame frameAreaPersonale;
    private JPanel areaPersonalePanel;
    private JLabel titoloLabel;
    private JScrollBar scrollBar1;
    private JButton invitiButton;

    public AreaPersonale(Controller controller) {
        invitiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Invito.main(null, controller);
            }
        });
    }

    public static void main(String[] args, Controller controller) {
        frameAreaPersonale = new JFrame("Inviti");
        frameAreaPersonale.setContentPane(new AreaPersonale(controller).areaPersonalePanel);
        frameAreaPersonale.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameAreaPersonale.setPreferredSize(new Dimension(300, 320));
        frameAreaPersonale.setResizable(true);
        frameAreaPersonale.pack();
        frameAreaPersonale.setLocationRelativeTo(null);
        frameAreaPersonale.setVisible(true);
    }
}
