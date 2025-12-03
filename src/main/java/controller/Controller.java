package controller;

import database.dao.*;
import gui.*;
import model.*;
import model.Hackathon;
import model.Invito;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Controller {

    public final java.sql.Date oggi = java.sql.Date.valueOf(LocalDate.now());

    public void eseguiIscrizione(Iscrizione iscrizione) {
        String username = iscrizione.getUsernameText().getText().trim();
        String password = new String(iscrizione.getPasswordField1().getPassword());
        String conferma = new String(iscrizione.getPasswordField2().getPassword());

        if(username.contains(" ") || password.contains(" ")){
            JOptionPane.showMessageDialog(iscrizione.getPanel1(), "username e password non devono contenere spazi",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(iscrizione.getPanel1(),
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (!password.equals(conferma)) {
            JOptionPane.showMessageDialog(iscrizione.getPanel1(), "Password e conferma sono diversi",
                    "Errore in Iscrizione",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else {
            int risposta = JOptionPane.showConfirmDialog(iscrizione.getPanel1(),
                    "Sei sicuro dei dati inseriti?",
                    "Conferma",
                    JOptionPane.INFORMATION_MESSAGE);

            if(risposta==JOptionPane.OK_OPTION) {
                boolean controllo;
                try {
                DAO_Utente utenteDAO = new DAO_Utente();
                controllo = utenteDAO.save(username, password);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if(controllo) {
                    JOptionPane.showMessageDialog(iscrizione.getPanel1(),
                            "Account creato con successo.",
                            "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                    Login.main(null);
                    iscrizione.getFrameIscrizione().dispose();
                } else
                    JOptionPane.showMessageDialog(iscrizione.getPanel1(),
                            "Username già usato",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
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
            return;
        }
        else if (username.length() < 5 || password.length() < 5) {
            JOptionPane.showMessageDialog(login.getPanel1(),
                    "Username e Password devono contenere almeno 5 caratteri.",
                    "Errore di login",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else {

            Utente utente;
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

            if(utente.getRuolo() <= 0) {
                Home2.main(this, (Organizzatore) utente);
            } else if(utente.getRuolo() <= 2) {
                ArrayList<Invito> inviti = this.getInviti((Utente) utente);
                Home.main(this, (Utente) utente, inviti);
            } else if(utente.getRuolo() == 3) {
                MembroTeamHome.main(this, (MembroTeam) utente);
            } else if(utente.getRuolo() == 30) {
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

    public void rifiutaInvito(Home home, Utente utente, Invito invito) {
        try {
            DAO_Invito daoInvito = new DAO_Invito();
            daoInvito.delete(utente, invito);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.aggiornaInviti(home, utente);
    }

    public boolean accettaInvito(Home home,Utente utente, Invito invito) {
        if(utente.getRuolo() == 29) {
            JOptionPane.showMessageDialog(home.getHomePanel(),
                    "Sei stato rimosso dall'hackathon",
                    "Negato l'accesso",
                    JOptionPane.ERROR_MESSAGE);
        }

        Utente NewUtente;
        try {
            DAO_Invito daoInvito = new DAO_Invito();
            NewUtente = daoInvito.accetta(utente, invito);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (NewUtente == null) {return false;}
        else {home.getFrameHome().dispose();}

        if(NewUtente.getRuolo() == 0){
            Home2.main(this, (Organizzatore) NewUtente);
        } else {
            MembroTeamHome.main(this, (MembroTeam) NewUtente);
        }

        return true;
    }

    public void guardaHackathon(Home2 home, Utente utente) {
        Hackathon hackathon;
        ArrayList<Organizzatore> giudici;

        DAO_Hackathon daoHackathon;
        try {
            daoHackathon = new DAO_Hackathon();
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
            try {
                giudici = daoHackathon.findAllGiudici(hackathon);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            gui.Hackathon.main(this, hackathon, giudici, utente);
        }
    }

    public void guardaHackathon(Home home, Utente utente) {
        Hackathon hackathon;
        ArrayList<Organizzatore> giudici;
        DAO_Hackathon daoHackathon;
        try {
            daoHackathon = new DAO_Hackathon();
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
            try {
                giudici = daoHackathon.findAllGiudici(hackathon);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            gui.Hackathon.main(this, hackathon, giudici, utente);
        }
    }

    public void guardaHackathon(MembroTeamHome home, Utente utente) { //arronzato, era di organizzatore
        Hackathon hackathon;
        ArrayList<Organizzatore> giudici;
        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            hackathon = daoHackathon.findHackathonCorrente();
            giudici = daoHackathon.findAllGiudici(hackathon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        gui.Hackathon.main(this, hackathon, giudici, utente);

    }

    public void guardaHackathon(Utente utente, Hackathon hackathon) {
        ArrayList<Organizzatore> giudici;
        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            giudici = daoHackathon.findAllGiudici(hackathon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        gui.Hackathon.main(this, hackathon, giudici, utente);
    } //per visualizzaHackathon

    public void apriRegistrazioni(gui.Hackathon hackathonGUI, Hackathon hackathon,
                                  ArrayList<Organizzatore> giudici, Organizzatore organizzatore) {

        int risposta = JOptionPane.showConfirmDialog(hackathonGUI.getHackathonPanel(),
                "Vuoi aprire le iscrizioni?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);
        if (risposta==JOptionPane.OK_OPTION) {
            try {
                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                daoHackathon.aperturaRegistrazioni(hackathon.getTitolo());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            hackathonGUI.getFrameHackathon().dispose();
            gui.Hackathon.main(this, hackathon, giudici, organizzatore);
        }
    }

    public void precedentiHackathon(VisualizzaHackathon visualizzaHackathon, String hackathon, String organizzatore, Utente utente) {
        ArrayList<model.Hackathon> hackathons = new ArrayList<>();
        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            hackathons = daoHackathon.getHackathons(hackathon, organizzatore);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        visualizzaHackathon.getFrameVisualizzaHackathon().dispose();
        VisualizzaHackathon.main(this, utente, hackathons);
    }

    public void visualizzaIscritti(Hackathon hackathon, Utente utente) {
        ArrayList<Team> iscritti;

        try {
            DAO_Utente daoUtente = new DAO_Utente();
            iscritti = daoUtente.findByHackathon(hackathon.getTitolo());

            if(hackathon.getDataInizio().after(oggi)) {
                DAO_Voto daoVoto = new DAO_Voto();
                if(hackathon.getDataFine().after(oggi)) {
                    daoVoto.getVoti(iscritti);
                } else {
                    daoVoto.getGiudiciVotanti(iscritti);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        VisualizzaIscritti.main(this, hackathon, iscritti, utente);
    }

    public void visualizzaTeam(Hackathon hackathon, Utente utente) {
        ArrayList<Team> iscritti;

        try {
            DAO_Team daoTeam = new DAO_Team();
            iscritti = daoTeam.findByHackathon(hackathon.getTitolo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        VisualizzaIscritti.main(this, hackathon, iscritti, utente);
    }

    public void squalifica(String utente, Hackathon hackathon) {
        try {
            DAO_Hackathon daoHackathon =  new DAO_Hackathon();
            daoHackathon.squalifica(utente, hackathon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rimuoviTeam(Team team, Hackathon hackathon) {
        for(String u : team.partecipanti)
            this.squalifica(u, hackathon);

        try {
            DAO_Team daoTeam= new DAO_Team();
            daoTeam.delete(team.NOME_TEAM, hackathon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void visualizzaFile(String team, Hackathon hackathon) {
        if(hackathon == null){
            try {
                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                hackathon = daoHackathon.findHackathonCorrente();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        ArrayList<String> files;
        try {
            DAO_Team daoTeam = new DAO_Team();
            files = daoTeam.getFile(team, hackathon.getTitolo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        VisualizzaFile.main(this, files, 0);
    }

    public boolean saveFile(MembroTeamHome membroTeamHome, Team team, String nomeFile, String contenuto) {
        boolean controllo;
        try {
            DAO_Team daoTeam = new DAO_Team();
            controllo = daoTeam.saveFile(team.NOME_TEAM, nomeFile, contenuto);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        if(!controllo) {
            JOptionPane.showMessageDialog(membroTeamHome.getMembroTeamHomePanel(),
                    "Nome file già usato",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else return true;
    }

    public void creaHackathon(CreazioneHackathon creaHackathon, Organizzatore organizzatore) {
            Date dataInizio = (Date) creaHackathon.getDataInizioSpinner().getValue();
            Date dataFine = (Date) creaHackathon.getDataFineSpinner().getValue();
            int limiteIscritti = (Integer) creaHackathon.getLimiteIscrittiSpinner().getValue();
            int limiteComponentiSquadra = (Integer) creaHackathon.getLimiteComponentiSquadreSpinner().getValue();
            String titolo = creaHackathon.getTitoloTextField().getText();
            String sede = creaHackathon.getSedeTextField().getText();

            if (dataInizio.after(dataFine) || dataInizio.before(new Date())) {
                JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                        "la data iniziale o finale non sono corrette",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (limiteIscritti < 4) {
                JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                        "Non possono esserci meno di 4 iscritti",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (limiteComponentiSquadra < 2) {
                JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                        "Non possono esserci meno di 2 squadre",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (titolo.isEmpty() || sede.isEmpty()) {
                JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                        "Dati mancanti",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        int risposta = JOptionPane.showConfirmDialog(creaHackathon.getCreazioneHackathonPanel(),
                "Sei sicuro dei dati inseriti?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);

        if(risposta==JOptionPane.OK_OPTION) {
            Hackathon hackathon = new Hackathon(titolo,
                    sede,
                    organizzatore.USERNAME,
                    dataInizio,
                    dataFine,
                    limiteIscritti,
                    limiteComponentiSquadra);

            boolean controllo;
            try {
                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                controllo = daoHackathon.save(hackathon);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(controllo) {
                JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                        "Hackathon creato con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);

                gui.Hackathon.main(this, hackathon, null, organizzatore);
                creaHackathon.getFrameCreazioneHackathon().dispose();
            } else
                JOptionPane.showMessageDialog(creaHackathon.getCreazioneHackathonPanel(),
                    "Titolo già usato",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Utente partecipa(gui.Hackathon hackathonGUI, Utente utente, Hackathon hackathon) {
        if(utente.getRuolo() == 29) {
            JOptionPane.showMessageDialog(hackathonGUI.getHackathonPanel(),
                    "Sei stato rimosso",
                    "Negato l'accesso",
                    JOptionPane.ERROR_MESSAGE);
        }
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
            ArrayList<Invito> inviti;

            try {
                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                partecipante = daoHackathon.partecipa(hackathon, utente);

                DAO_Invito daoInvito = new DAO_Invito();
                inviti = daoInvito.getInviti(utente);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            hackathonGUI.getFrameHackathon().dispose();
            Home.main(this, utente, inviti);
            return partecipante;

        } else return  null;
    };

    public MembroTeam creaTeam(CreaTeam creaTeam, Utente utente, Home home) {
        String nomeTeam = creaTeam.getNomeTeam().getText();
        if (nomeTeam.isEmpty()) {
            JOptionPane.showMessageDialog(creaTeam.getCreaTeamPanel(),
                    "Nome team non inserito",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int risposta = JOptionPane.showConfirmDialog(creaTeam.getCreaTeamPanel(),
                "Sei sicuro del nome inserito?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);

        if (risposta==JOptionPane.OK_OPTION) {
            MembroTeam membroTeam;
            boolean controllo;

            try {
                DAO_Team daoTeam = new DAO_Team();
                controllo = daoTeam.save(nomeTeam, utente);

                membroTeam = new MembroTeam(utente.USERNAME, new Team(nomeTeam, utente.USERNAME));


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(controllo) {
                creaTeam.getFrameCreaTeam().dispose();
                home.getFrameHome().dispose();
                MembroTeamHome.main(this, membroTeam);
                return membroTeam;
            } else
            {
                JOptionPane.showMessageDialog(creaTeam.getCreaTeamPanel(),
                        "Nome team già usato",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

        } else return null;
    }

    //apre la gui per modificare
    public void modificaHackathon(gui.Hackathon hackathonGUI, Organizzatore organizzatore, Hackathon hackathon, ArrayList<Organizzatore> giudici) { //da vedere se funziona
        if(hackathon.getDataInizio().after(new java.util.Date())) {
            ModificaHackathon.main(this, organizzatore, hackathon, giudici);
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
    public void modificaHackathon(ModificaHackathon modificaHackathon, Organizzatore organizzatore, Hackathon hackathon, ArrayList<Organizzatore> giudici) {
            Date dataInizio = (Date) modificaHackathon.getDataInizioSpinner().getValue();
            Date dataFine = (Date) modificaHackathon.getDataFineSpinner().getValue();
            int limiteIscritti = (Integer) modificaHackathon.getLimiteIscrittiSpinner().getValue();
            int limiteComponentiSquadra = (Integer) modificaHackathon.getLimiteComponentiSquadreSpinner().getValue();
            String titolo = modificaHackathon.getTitoloTextField().getText();
            String sede = modificaHackathon.getSedeTextField().getText();


            if (dataInizio.after(dataFine) || dataInizio.before(new Date())) {
                JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                        "la data iniziale o finale non sono corrette",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (limiteIscritti < 4) {
                JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                        "Non possono esserci meno di 4 iscritti",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (limiteComponentiSquadra < 2) {
                JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                        "Non possono esserci meno di 2 squadre",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (titolo.isEmpty() || sede.isEmpty()) {
                JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                        "Dati mancanti",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        int risposta = JOptionPane.showConfirmDialog(modificaHackathon.getModificaHackathonPanel(),
                "Sei sicuro dei dati inseriti?",
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);

        if(risposta==JOptionPane.OK_OPTION) {

            boolean controllo;
            try {
                String oldtitolo = hackathon.getTitolo();
                hackathon.cambiaTitolo(titolo);
                hackathon.cambiaSede(sede);
                hackathon.cambiaInizio(dataInizio);
                hackathon.cambiaFine(dataFine);
                hackathon.cambiaMaxIscritti(limiteIscritti);
                hackathon.cambiaMaxTeamSize(limiteComponentiSquadra);

                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                controllo = daoHackathon.update(oldtitolo, organizzatore.USERNAME, hackathon);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (controllo) {
                JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                        "Hackathon modificato con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);

                gui.Hackathon.main(this, hackathon, giudici, organizzatore);
                modificaHackathon.getFrameModificaHackathon().dispose();
            } else
                JOptionPane.showMessageDialog(modificaHackathon.getModificaHackathonPanel(),
                        "Titolo già usato.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cercaUtentiU(CercaUtenti cercaUtenti, Utente utente, String ricercato) {
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

    public void cercaUtentiO(CercaUtenti cercaUtenti, Utente utente, String ricercato) {
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

    public void cancellaUtente(CercaUtenti cercaUtenti, Utente utente, String ricercato) {
        try {
            DAO_Utente daoUtente = new DAO_Utente();
            daoUtente.delete(utente.USERNAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.cercaUtentiO(cercaUtenti, utente, ricercato);
    }

    public void invitaMT(CercaUtenti cercaUtenti, MembroTeam mittente, Utente destinatario) {
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

    public void invitaO(CercaUtenti cercaUtenti, Organizzatore mittente, Utente destinatario) {
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

    public void aggiungiVoto(Team team, Hackathon hackathon, Utente utente) {
        JSpinner spinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 10, 1)
        );

        int result = JOptionPane.showConfirmDialog(
                null,
                spinner,
                "Inserisci il voto (1-10)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int voto = (int) spinner.getValue();
            try {
                DAO_Voto daoVoto = new DAO_Voto();
                daoVoto.save(hackathon.getTitolo(), team.NOME_TEAM, utente.USERNAME, voto);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}