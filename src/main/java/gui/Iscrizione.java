package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Iscrizione {
    private static JFrame frameIscrizione;
    private JPanel panel1;
    private JLabel UsernameLabel;
    private JTextField UsernameText;
    private JLabel passwordLabel;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton iscrivitiButton;

    public Iscrizione() {
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
                eseguiIscrizione();
            }
        });
        iscrivitiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                eseguiIscrizione();
            }
        });
    }

    private void eseguiIscrizione() {
        String username = UsernameText.getText();
        String password = new String(passwordField1.getPassword());
        String conferma = new String(passwordField2.getPassword());

        if (!password.equals(conferma)) {
            JOptionPane.showMessageDialog(panel1, "Password e conferma sono diversi",
                    "Errore di login",
                    JOptionPane.ERROR_MESSAGE);
        }
        else if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(panel1,
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore di login",
                    JOptionPane.ERROR_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(panel1,
                    "Account creato con successo.",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
            Login.main(null);
            frameIscrizione.dispose();
        }
    }

    public static void main(String[] args) {
        frameIscrizione = new JFrame("User Login Frame");
        frameIscrizione.setContentPane(new Iscrizione().panel1);
        frameIscrizione.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameIscrizione.setPreferredSize(new Dimension(280, 250));
        frameIscrizione.setResizable(true);
        frameIscrizione.pack();
        frameIscrizione.setLocationRelativeTo(null);
        frameIscrizione.setVisible(true);
    }
}
