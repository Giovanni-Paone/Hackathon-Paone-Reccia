package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JFrame frame;

    public Login() {
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField1.requestFocusInWindow();
            }
        });
        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiLogin();
            }
        });
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                eseguiLogin();
            }
        });
    }

    private void eseguiLogin() {
        String username = textField1.getText();
        String password = new String(passwordField1.getPassword());

        if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(panel1,
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore di login",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            //apriNuovaScheda();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("User Login Frame");
        frame.setContentPane(new Login().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(300, 200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
