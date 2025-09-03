package controller;

import database.dao.DAO_Hackathon;
import database.dao.DAO_Invito;
import database.dao.DAO_Team;
import database.dao.DAO_Utente;
import gui.*;
import model.*;
import model.Hackathon;
import model.Invito;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Controller {

    public void eseguiIscrizione(Iscrizione iscrizione) {
        String username = iscrizione.getUsernameText().getText().trim();
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
                Utente utente = new Utente(username, 1);
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

    public void eseguiLogin(Login login) {
        String username = login.getUsernameTextField().getText().trim();
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

            UtenteBase utente;
            try {
                DAO_Utente daoUtente = new DAO_Utente();
                utente = daoUtente.login(username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (utente == null) {
                JOptionPane.showMessageDialog(login.getPanel1(), "Username o Password sono errati.",
                        "Errore di login",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            switch (utente.getRuolo()) {
                case 1:
                    ArrayList<Invito> inviti = this.getInviti((Utente) utente);
                    Home.main(this, (Utente) utente, inviti);
                    break;
                case 2:
                    ArrayList<Invito> inviti2 = this.getInviti((Utente) utente);
                    Home.main(this, (Utente) utente, inviti2);
                    break;
                case 3:
                    MembroTeamHome.main(this, (MembroTeam) utente);
                    break;
                case 0:
                    Home2.main(this, (Giudice) utente);
                    break;
                case -1:
                    Home2.main(this, (Organizzatore) utente);
                    break;
                case 30:
                    JOptionPane.showMessageDialog(login.getPanel1(), "Utente rimosso dalla piattaforma",
                            "Utente rimosso",
                            JOptionPane.ERROR_MESSAGE);
            }
        }
        login.getFrameLogin().dispose();
    }

    public ArrayList<Invito> getInviti(Utente utente) {
        ArrayList<Invito> inviti = new ArrayList<>();

        try {
            DAO_Invito daoInvito = new DAO_Invito();
            inviti = daoInvito.getInviti(utente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return inviti;
    }

    public ArrayList<Invito> getInviti(Utente utente, String mittente) {
        ArrayList<Invito> inviti = new ArrayList<>();

        try {
            DAO_Invito daoInvito = new DAO_Invito();
            inviti = daoInvito.getInviti(utente.USERNAME, mittente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return inviti;
    }

    public void aggiornaInviti(Home home, Utente utente) {
        String mittente = home.getCercaTextField().getText().trim();
        ArrayList<Invito> inviti = this.getInviti(utente, mittente);
        home.aggiornaInvitiPanel(this, utente, inviti);
    }

    public boolean accettaInvito(Utente utente, Invito invito) {
        UtenteBase NewUtente;
        try {
            DAO_Invito daoInvito = new DAO_Invito();
            NewUtente = daoInvito.accetta(utente, invito);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (NewUtente
        return false;
    }

    public void guardaHackathon(Home2 home, UtenteBase utente) {
        Hackathon hackathon;

        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            hackathon = daoHackathon.findHackathonCorrente();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (hackathon == null) {
            if(utente.getRuolo() == -1){
                int risposta = JOptionPane.showConfirmDialog(home.getOrganizzatoreHomePanel(),
                        "Non è in corso nessun Hackathon, crearne 1?");
                if (risposta==JOptionPane.OK_OPTION) {
                    CreazioneHackathon.main(this, (Organizzatore) utente);
                    }
            } else return;
        } else {
            gui.Hackathon.main(this, hackathon, utente);
        }
    }

    public void guardaHackathon(Home home, UtenteBase utente) { //arronzato, era di organizzatore
        Hackathon hackathon;

        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            hackathon = daoHackathon.findHackathonCorrente();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (hackathon == null) {
            JOptionPane.showMessageDialog(home.getHomePanel(),
                    "Non è presente nessun hackathon in corso",
                    "Hackathon",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            gui.Hackathon.main(this, hackathon, utente);
        }
    }

    public void guardaHackathon(MembroTeamHome home, UtenteBase utente) { //arronzato, era di organizzatore
        Hackathon hackathon;

        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            hackathon = daoHackathon.findHackathonCorrente();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        gui.Hackathon.main(this, hackathon, utente);

    }

    public void precedentiHackathon() {
        //prendere dalla base di dati il precedente
        //PrevHackathon.main(null, this);
    }

    public void visualizzaIscritti(Hackathon hackathon, UtenteBase utente) {
        ArrayList<Team> iscritti;
        try {
            DAO_Utente daoUtente;
            daoUtente = new DAO_Utente();
            iscritti = daoUtente.findByHackathon(hackathon.getTitolo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        VisualizzaIscritti.main(this, hackathon, iscritti, utente);
    }

    public void visualizzaTeam(Hackathon hackathon, UtenteBase utente) {
        ArrayList<Team> iscritti;
        try {
            DAO_Team daoTeam = new DAO_Team();
            iscritti = daoTeam.findByHackathon(hackathon.getTitolo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        VisualizzaIscritti.main(this, hackathon, iscritti, utente);
    }

    public void creaHackathon(CreazioneHackathon creaHackathon, Organizzatore organizzatore) {
        //controlla se le date rispettano il formato
        int risposta = JOptionPane.showConfirmDialog(creaHackathon.getCreazioneHackathonPanel(),
                "Sei sicuro dei dati inseriti?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);

        if(risposta==JOptionPane.OK_OPTION) {
            Date dataInizio = (Date) creaHackathon.getDataInizioSpinner().getValue();
            Date dataFine = (Date) creaHackathon.getDataFineSpinner().getValue();
            if (dataInizio.after(dataFine) || dataInizio.before(new Date())) {
                JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                        "la data iniziale o finale non sono corrette",
                        "Erroe",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Hackathon hackathon = new Hackathon(creaHackathon.getTitoloTextField().getText(),
                    creaHackathon.getSedeTextField().getText(),
                    organizzatore.USERNAME,
                    dataInizio,
                    dataFine,
                    (Integer) creaHackathon.getLimiteIscrittiSpinner().getValue(),
                    (Integer) creaHackathon.getLimiteComponentiSquadreSpinner().getValue());

            try {
                organizzatore.setHackathon(hackathon);
                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                daoHackathon.save(hackathon);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                    "Hackathon creato con successo.",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);

            gui.Hackathon.main(this, hackathon, organizzatore);
            creaHackathon.getFrameCreazioneHackathon().dispose();
        }
    }

    public Utente partecipa(gui.Hackathon hackathonGUI, Utente utente, Hackathon hackathon) {
        if(hackathon.getMaxIscritti() < hackathon.getNPartecipantiIscritti()) {
            JOptionPane.showMessageDialog(hackathonGUI.getHackathonPanel(),
                    "l hackathon è già pieno",
                    "errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int risposta = JOptionPane.showConfirmDialog(hackathonGUI.getHackathonPanel(),
                "Vuoi partecipare al hackathon in corso?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);
        if (risposta==JOptionPane.OK_OPTION) {
            Utente partecipante;
            DAO_Hackathon daoHackathon = null;
            try {
                daoHackathon = new DAO_Hackathon();
                partecipante = daoHackathon.partecipa(hackathon, utente);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            return partecipante;

        } else return  null;
    };

    public MembroTeam creaTeam(CreaTeam creaTeam, Utente utente, Home home) {
        int risposta = JOptionPane.showConfirmDialog(creaTeam.getCreaTeamPanel(),
                "Sei sicuro del nome inserito?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);
        if (risposta==JOptionPane.OK_OPTION) {
            MembroTeam membroTeam;
            try {

                DAO_Team daoTeam = new DAO_Team();
                String nomeTeam = creaTeam.getNomeTeam().getText();
                daoTeam.save(nomeTeam, utente);

                membroTeam = new MembroTeam(utente.USERNAME, new Team(nomeTeam, utente.USERNAME));


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            creaTeam.getFrameCreaTeam().dispose();
            home.getFrameHome().dispose();
            MembroTeamHome.main(this, membroTeam);
            return membroTeam;
        } else {return null;}
    }

    //apre la gui per modificare
    public void modificaHackathon(gui.Hackathon hackathonGUI, Organizzatore organizzatore, Hackathon hackathon) { //da vedere se funziona
        if(hackathon.getDataInizio().after(new java.util.Date())) {
            ModificaHackathon.main(this, organizzatore, hackathon);
            hackathonGUI.getFrameHackathon().dispose();
        }
        else {
            JOptionPane.showMessageDialog(hackathonGUI.getHackathonPanel(),
                    "Non puoi modificarlo dopo l'inizio",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //modifica l hackathon
    public void modificaHackathon(ModificaHackathon modificaHackathon, Organizzatore organizzatore, Hackathon hackathon) {
        int risposta = JOptionPane.showConfirmDialog(modificaHackathon.getModificaHackathonPanel(),
                "Sei sicuro dei dati inseriti?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);

        if(risposta==JOptionPane.OK_OPTION) {
            Date dataInizio = (Date) modificaHackathon.getDataInizioSpinner().getValue();
            Date dataFine = (Date) modificaHackathon.getDataFineSpinner().getValue();

            if (dataInizio.after(dataFine) || dataInizio.before(new Date())) {
                JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                        "la data iniziale o finale non sono corrette",
                        "Erroe",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String oldtitolo = hackathon.getTitolo();
                hackathon.cambiaTitolo(modificaHackathon.getTitoloTextField().getText());
                hackathon.cambiaSede(modificaHackathon.getSedeTextField().getText());
                hackathon.cambiaInizio(dataInizio);
                hackathon.cambiaFine(dataFine);
                hackathon.cambiaMaxIscritti((Integer) modificaHackathon.getLimiteIscrittiSpinner().getValue());
                hackathon.cambiaMaxTeamSize((Integer) modificaHackathon.getLimiteComponentiSquadreSpinner().getValue());

                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                daoHackathon.update(oldtitolo, organizzatore.USERNAME, hackathon);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                    "Hackathon modificato con successo.",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);

            gui.Hackathon.main(this, hackathon, organizzatore);
            modificaHackathon.getFrameModificaHackathon().dispose();
        }
    }

    public void cercaUtentiU(CercaUtenti cercaUtenti, UtenteBase utente, String ricercato) {
        ArrayList<Utente> utenti;
        try {
            DAO_Utente daoUtente = new DAO_Utente();
            utenti = daoUtente.findByKeyUtente(ricercato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cercaUtenti.getFrameCercaUtenti().dispose();
        CercaUtenti.main(this, utente, utenti);
    }

    public void cercaUtentiO(CercaUtenti cercaUtenti, UtenteBase utente, String ricercato) {
        ArrayList<Utente> utenti;
        try {
            DAO_Utente daoUtente = new DAO_Utente();
            utenti = daoUtente.findByKey(ricercato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cercaUtenti.getFrameCercaUtenti().dispose();
        CercaUtenti.main(this, utente, utenti);
    }

    public void invitaMT(CercaUtenti cercaUtenti, MembroTeam mittente, UtenteBase destinatario) {
        boolean controllo = false;

        try {
            DAO_Invito daoInvito = new DAO_Invito();
            Invito invito = new Invito(mittente.TEAM.NOME_TEAM, false);
            controllo = daoInvito.save(invito, destinatario.USERNAME);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!controllo) {
            JOptionPane.showMessageDialog(cercaUtenti.getCercaUtenti(),
                    "Invito già inviato",
                    "Invito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void invitaO(CercaUtenti cercaUtenti, Organizzatore mittente, UtenteBase destinatario) {
        boolean controllo = false;

        try {
            DAO_Invito daoInvito = new DAO_Invito();
            Invito invito = new Invito("organizzatore", true);
            controllo = daoInvito.save(invito, destinatario.USERNAME);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!controllo) {
            JOptionPane.showMessageDialog(cercaUtenti.getCercaUtenti(),
                    "Invito già inviato",
                    "Invito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cancellaUtente(UtenteBase utente) {
        try {
            DAO_Utente daoUtente = new DAO_Utente();
            daoUtente.delete(utente.USERNAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}