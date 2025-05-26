package controller;

import gui.Login;
import model.Utente;

import javax.swing.*;

public class Controller {

    public void creaUtente(String USERNAME, String password){
        Utente utente = new Utente(USERNAME, password);
    }

    public void eseguiIscrizione() {
        String username = UsernameText.getText();
        String password = new String(passwordField1.getPassword());
        String conferma = new String(passwordField2.getPassword());

        if(username.contains(" ") || password.contains(" ")){
            JOptionPane.showMessageDialog(panel1, "username e password non devono contenere spazi",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
        }
        else if (!password.equals(conferma)) {
            JOptionPane.showMessageDialog(panel1, "Password e conferma sono diversi",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
        }
        else if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(panel1,
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
        }
        else {
            int risposta = JOptionPane.showConfirmDialog(panel1,
                    "Sei sicuro dei dati inseriti?",
                    "Conferma",
                    JOptionPane.INFORMATION_MESSAGE);
            if(risposta==JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(panel1,
                        "Account creato con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                Login.main(null);
                frameIscrizione.dispose();
            }
        }
    }

    public void main(String[] args) {
        Login login = new Login(this);
        login.main(null);
    }
}
