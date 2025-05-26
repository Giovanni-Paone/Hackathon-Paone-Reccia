package gui;

import controller.Controller;

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

    public JFrame getFrameLogin() {return frameLogin;}
    public JPanel getPanel1() {return panel1;}
    public JTextField getUsernameTextField() {return usernameTextField;}
    public JPasswordField getPasswordField1() {return passwordField1;}

    public Login(Controller controller) {
        usernameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField1.requestFocusInWindow();
            }
        });
        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.eseguiLogin(Login.this);
            }
        });
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.eseguiLogin(Login.this);
            }
        });
        iscrivitiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Iscrizione.main(null, controller);
                frameLogin.dispose();
            }
        });
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        frameLogin = new JFrame("Login");
        frameLogin.setContentPane(new Login(controller).panel1);
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setPreferredSize(new Dimension(300, 200));
        frameLogin.setResizable(false);
        frameLogin.pack();
        frameLogin.setLocationRelativeTo(null);
        frameLogin.setVisible(true);
    }
}
