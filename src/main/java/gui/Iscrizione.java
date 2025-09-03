package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Iscrizione {
    private static JFrame frameIscrizione;
    private JPanel panel1;
    private JLabel usernameLabel;
    private JTextField UsernameText;
    private JLabel passwordLabel;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton iscrivitiButton;
    private JLabel confermaPasswordLabel;

    public JFrame getFrameIscrizione() {return frameIscrizione;}
    public JPanel getPanel1() {return panel1;}
    public JTextField getUsernameText() {return UsernameText;}
    public JPasswordField getPasswordField1() {return passwordField1;}
    public JPasswordField getPasswordField2() {return passwordField2;}


    public Iscrizione(Controller controller) {
        UsernameText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {passwordField1.requestFocus();}
        });
        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {passwordField2.requestFocus();
            }
        });
        passwordField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.eseguiIscrizione(Iscrizione.this);
            }
        });
        iscrivitiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.eseguiIscrizione(Iscrizione.this);
            }
        });
    }


    public static void main(String[] args, Controller controller) {
        frameIscrizione = new JFrame("utenti");
        frameIscrizione.setContentPane(new Iscrizione(controller).panel1);
        frameIscrizione.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameIscrizione.setPreferredSize(new Dimension(280, 250));
        frameIscrizione.setResizable(true);
        frameIscrizione.pack();
        frameIscrizione.setLocationRelativeTo(null);
        frameIscrizione.setVisible(true);
    }
}
