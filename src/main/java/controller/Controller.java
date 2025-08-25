package controller;

import database.dao.DAO_Hackathon;
import database.dao.DAO_Utente;
import gui.*;
import gui.PartecipanteView;
import model.*;
import model.Hackathon;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
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
            Piattaforma piattaforma = new Piattaforma();
            //controlla nella base di dati se esiste, se esiste:
            int ruolo = 1; //se esiste, controlla il ruolo
            switch (ruolo) {
                case(-1):
                    Organizzatore organizzatore = new Organizzatore(username);
                    //se è presente un hackathon e non sei il giudice puoi solo vederlo senza fare nulla
                    //se non è presente

                    //raccogli i dati del hackathon dalla base di dati
                    //per ora i dati saranno provvisori
                    String titolo = "provvisorio";
                    String sede = null;
                    Date dataInizio =  new Date();
                    Date dataFine = new Date();
                    int maxIscritti = 15;
                    int MAX_TEAM_SIZE = 4;
                    Hackathon hackathon = new Hackathon(titolo, sede, organizzatore, dataInizio, dataFine, maxIscritti, MAX_TEAM_SIZE);
                    organizzatore.setHackathon(hackathon);
                    OrganizzatoreView.main(null, this);
                    login.getFrameLogin().dispose();
                    break;
                case(0):
                    //creare hackathon
                    //Giudice giudice = new Giudice(username, hackathon);
                    GiudiceView.main(null, this);
                    login.getFrameLogin().dispose();
                    break;
                case(10):
                    //cambia il ruolo da 10 a 1 nella base di dati
                    JOptionPane.showMessageDialog(login.getPanel1(),
                            "Non hai trovato un team in tempo",
                            "tempo scaduto",
                            JOptionPane.INFORMATION_MESSAGE);
                    ruolo = 1;
                case(1):
                    Utente utente = new Utente(username);
                    Home.main(null, this);
                    login.getFrameLogin().dispose();
                    break;
                case(2):
                    //creare hackathon
                    //Partecipante partecipante = new Partecipante(username, hackathon)
                    PartecipanteView.main(null,  this);
                    login.getFrameLogin().dispose();
                    break;
                case(3):
                    //creare team
                    //MembroTeam membroTeam = new MembroTeam(username);
                    MembroTeamView.main(null,  this);
                    login.getFrameLogin().dispose();
                    break;
            }
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
                try {
                Utente utente = new Utente(username);
                DAO_Utente utenteDAO = new DAO_Utente();
                utenteDAO.save(utente, password);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                JOptionPane.showMessageDialog(iscrizione.getPanel1(),
                        "Account creato con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                Login.main(null);
                iscrizione.getFrameIscrizione().dispose();
            }
        }
    }


    public void guardaHackathon(Home home) {
        if (2 == 2) { //controlla se c'è un hackathon corrente
            IscrizioneHackathon.main(null, this);
        } else {
            JOptionPane.showMessageDialog(home.getHomePanel(),
                    "Non è presente nessun hackathon in corso",
                    "Hackathon",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void guardaHackathon(GiudiceHome giudiceHome) {
        if(2==2) { //controlla se c'è un hackathon corrente
                IscrizioneHackathon.main(null, this);
        }
        else {
            JOptionPane.showMessageDialog(giudiceHome.getGiudiceHomePanel(),
                    "Non è presente nessun hackathon in corso",
                    "Hackathon",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void guardaHackathon(OrganizzatoreHome organizzatoreHome) {
        if (2 == 2) { //controlla se c'è un hackathon corrente
            IscrizioneHackathon.main(null, this);
        } else {
            JOptionPane.showMessageDialog(organizzatoreHome.getOrganizzatoreHomePanel(),
                    "Non è presente nessun hackathon in corso",
                    "Hackathon",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void precedentiHackathon() {
        //prendere dalla base di dati il precedente
        //PrevHackathon.main(null, this);
    }

    public void visualizzaTeam() {
        //mostra con base di dati
    }

    public void visualizzaIscritti() {
        //mostra con base di dati
    }

    public void creaHackathon(CreazioneHackathon creaHackathon, Organizzatore organizzatore) {
        //controlla se le date rispettano il formato
        int risposta = JOptionPane.showConfirmDialog(creaHackathon.getCreazioneHackathonPanel(),
                "Sei sicuro dei dati inseriti?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);
        if(risposta==JOptionPane.OK_OPTION) {
            try {
            Hackathon hackathon = new Hackathon(creaHackathon.getTitoloTextField().getText(),
                    creaHackathon.getSedeTextField().getText(),
                    organizzatore,
                    (Date) creaHackathon.getDataInizioSpinner().getValue(),
                    (Date) creaHackathon.getDataFineSpinner().getValue(),
                    (Integer) creaHackathon.getLimiteIscrittiSpinner().getValue(),
                    (Integer) creaHackathon.getLimiteComponentiSquadreSpinner().getValue());
            //aggiungi nella piattaforma
                organizzatore.setHackathon(hackathon);
                DAO_Hackathon daoHackathon = new DAO_Hackathon();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            OrganizzatoreView.main(null, this);
            creaHackathon.getFrameCreazioneHackathon().dispose();
        }
    }

    public void modificaHackathon(ModificaHackathon modificaHackathon, Organizzatore organizzatore) {
        //controlla se le date rispettano il formato
        int risposta = JOptionPane.showConfirmDialog(modificaHackathon.getModificaHackathonPanel(),
                "Sei sicuro dei dati inseriti?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);
        if(risposta==JOptionPane.OK_OPTION) {
            Hackathon hackathon = new Hackathon(modificaHackathon.getTitoloTextField().getText(),
                    modificaHackathon.getSedeTextField().getText(),
                    organizzatore,
                    (Date) modificaHackathon.getDataInizioSpinner().getValue(),
                    (Date) modificaHackathon.getDataFineSpinner().getValue(),
                    (Integer) modificaHackathon.getLimiteIscrittiSpinner().getValue(),
                    (Integer) modificaHackathon.getLimiteComponentiSquadreSpinner().getValue());
            //modifica nella base di dati
            OrganizzatoreView.main(null, this);
            modificaHackathon.getFrameModificaHackathon().dispose();
        }
    }

    public void invitaGiudice(OrganizzatoreView organizzatoreView) {
        //cerca utente in base di dati e fa addInvito
    }
}
