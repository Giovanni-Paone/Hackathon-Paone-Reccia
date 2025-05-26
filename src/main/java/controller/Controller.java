package controller;

import gui.*;
import model.Hackathon;
import model.Piattaforma;
import model.Utente;

import javax.swing.*;
import java.util.Date;

public class Controller {

    public void eseguiLogin(Login login) {
        String username = login.getUsernameTextField().getText();
        String password = new String(login.getPasswordField1().getPassword());

        if(username.contains(" ") || password.contains(" ")) {
            JOptionPane.showMessageDialog(login.getPanel1(), "username e password non devono contenere spazi",
                    "Errore di login",
                    JOptionPane.ERROR_MESSAGE);
        }
        else if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(login.getPanel1(),
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore di login",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            //controlla nella base di dati se esiste, se esiste:
            Utente utente = new Utente(username, password);
            Piattaforma piattaforma = new Piattaforma();
            Home.main(null, this);
            login.getFrameLogin().dispose();
        }
    }

    public void eseguiIscrizione(Iscrizione iscrizione) {
        String username = iscrizione.getUsernameText().getText();
        String password = new String(iscrizione.getPasswordField1().getPassword());
        String conferma = new String(iscrizione.getPasswordField2().getPassword());

        if(username.contains(" ") || password.contains(" ")){
            JOptionPane.showMessageDialog(iscrizione.getPanel1(), "username e password non devono contenere spazi",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
        }
        else if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(iscrizione.getPanel1(),
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
        }
        else if (!password.equals(conferma)) {
            JOptionPane.showMessageDialog(iscrizione.getPanel1(), "Password e conferma sono diversi",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
        }
        else {
            int risposta = JOptionPane.showConfirmDialog(iscrizione.getPanel1(),
                    "Sei sicuro dei dati inseriti?",
                    "Conferma",
                    JOptionPane.INFORMATION_MESSAGE);
            if(risposta==JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(iscrizione.getPanel1(),
                        "Account creato con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                Login.main(null);
                iscrizione.getFrameIscrizione().dispose();
            }
        }
    }

    public void creaHackathon(CreazioneHackathon creaHackathon) {
        //controlla se le date rispettano il formato
        int risposta = JOptionPane.showConfirmDialog(creaHackathon.getCreazioneHackathonPanel(),
                "Sei sicuro dei dati inseriti?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);
        if(risposta==JOptionPane.OK_OPTION) {
            Hackathon hackathon = new Hackathon(creaHackathon.getTitoloTextField().getText(),
                    creaHackathon.getSedeTextField().getText(),
                    (Date) creaHackathon.getDataInizioSpinner().getValue(),
                    (Date) creaHackathon.getDataFineSpinner().getValue(),
                    (Integer) creaHackathon.getLimiteIscrittiSpinner().getValue(),
                    (Integer) creaHackathon.getLimiteComponentiSquadreSpinner().getValue());
            //aggiungi nella piattaforma
            OrganizzatoreView.main(null, this);
            creaHackathon.getFrameCreazioneHackathon().dispose();
        }
    }

    public void invitaGiudice(OrganizzatoreView organizzatoreView) {
        //cerca utente in base di dati e fa addInvito
    }
}
