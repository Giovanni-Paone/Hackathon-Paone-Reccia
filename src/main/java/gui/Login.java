package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login {
    private static JFrame frameLogin;
    private JPanel panel1;
    private JTextField usernameTextField;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JLabel iscrivitiLabel;
    private JButton iscrivitiButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public Login() {
        usernameTextField.addActionListener(new ActionListener() {
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
        iscrivitiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Iscrizione.main(null);
                frameLogin.dispose();
            }
        });
    }

    private void eseguiLogin() {
        String username = usernameTextField.getText();
        String password = new String(passwordField1.getPassword());

        if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(panel1,
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore di login",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            Home.main(null);
            frameLogin.dispose();
        }
    }

    public static void main(String[] args) {
        frameLogin = new JFrame("User Login Frame");
        frameLogin.setContentPane(new Login().panel1);
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setPreferredSize(new Dimension(300, 200));
        frameLogin.setResizable(false);
        frameLogin.pack();
        frameLogin.setLocationRelativeTo(null);
        frameLogin.setVisible(true);
    }
}
