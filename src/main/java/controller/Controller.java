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

public class Controller {

    public final LocalDate oggi = LocalDate.now();

    /**
     * Gestisce la registrazione di un nuovo utente.
     * Verifica la validità di username e password e salva i dati tramite DAO_Utente.
     * @param iscrizione L'interfaccia grafica del form di iscrizione.
     */
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

    /**
     * Gestisce l'autenticazione degli utenti.
     * In base al ruolo dell'utente autenticato (Organizzatore, Utente semplice, Membro Team),
     * reindirizza alla home page corretta.
     * @param login L'interfaccia grafica del form di login.
     */
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
                ArrayList<Invito> inviti = this.getInviti(utente);
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

    /**
     * Recupera tutti gli inviti ricevuti da un determinato utente.
     * @param utente L'utente di cui recuperare gli inviti.
     * @return Una lista di oggetti Invito.
     */
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

    /**
     * Recupera gli inviti filtrandoli per il mittente.
     * @param utente L'utente destinatario.
     * @param mittente Il nome del mittente da cercare.
     * @return Una lista di inviti filtrata.
     */
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

    /**
     * Aggiorna la visualizzazione degli inviti nel pannello Home.
     * @param home L'interfaccia home utente.
     * @param utente L'utente corrente.
     */
    public void aggiornaInviti(Home home, Utente utente) {
        String mittente = home.getCercaTextField().getText().trim();
        ArrayList<Invito> inviti = this.getInviti(utente, mittente);
        home.aggiornaInvitiPanel(this, utente, inviti);
    }

    /**
     * Gestisce il rifiuto di un invito eliminandolo dal database.
     * @param home L'interfaccia home utente.
     * @param utente L'utente che rifiuta.
     * @param invito L'invito da eliminare.
     */
    public void rifiutaInvito(Home home, Utente utente, Invito invito) {
        try {
            DAO_Invito daoInvito = new DAO_Invito();
            daoInvito.delete(utente, invito);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.aggiornaInviti(home, utente);
    }

    /**
     * Gestisce l'accettazione di un invito.
     * Aggiorna il ruolo dell'utente nel database e aggiorna la GUI.
     * @param home L'interfaccia home utente.
     * @param utente L'utente che accetta.
     * @param invito L'invito accettato.
     */
    public void accettaInvito(Home home,Utente utente, Invito invito) {
        if(utente.getRuolo() == 29) {
            JOptionPane.showMessageDialog(home.getHomePanel(),
                    "Sei stato rimosso dall'hackathon",
                    "Negato l'accesso",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Utente NewUtente;
        try {
            DAO_Invito daoInvito = new DAO_Invito();
            NewUtente = daoInvito.accetta(utente, invito);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (NewUtente == null) {return;}
        else {home.getFrameHome().dispose();}

        if(NewUtente.getRuolo() == 0){
            Home2.main(this, (Organizzatore) NewUtente);
        } else {
            MembroTeamHome.main(this, (MembroTeam) NewUtente);
        }

        return;
    }

    /**
     * Apre la schermata dell'Hackathon corrente per un Organizzatore.
     * Se non esiste un hackathon, propone di crearne uno.
     * @param home Interfaccia home organizzatore.
     * @param utente L'organizzatore corrente.
     */
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

    /**
     * Apre la schermata dell'Hackathon corrente per un Utente semplice.
     * @param home Interfaccia home utente.
     * @param utente L'utente corrente.
     */
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

    /**
     * Apre la schermata dell'Hackathon corrente per un Membro del Team.
     * @param home Interfaccia home membro team.
     * @param utente Il membro team corrente.
     */
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

    /**
     * Apre la schermata di un Hackathon specifico.
     * @param utente Utente visualizzatore.
     * @param hackathon L'oggetto Hackathon da mostrare.
     */
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

    /**
     * Permette all'organizzatore di aprire ufficialmente le iscrizioni a un Hackathon.
     * @param hackathonGUI GUI dell'hackathon.
     * @param hackathon Modello dell'hackathon.
     * @param giudici Lista dei giudici associati.
     * @param organizzatore L'organizzatore che esegue l'azione.
     */
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

    /**
     * Filtra e visualizza gli Hackathon passati.
     * @param visualizzaHackathon GUI di visualizzazione.
     * @param hackathon Nome/titolo da cercare.
     * @param organizzatore Nome organizzatore da cercare.
     * @param utente Utente corrente.
     */
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

    /**
     * Mostra l'elenco dei team iscritti a un Hackathon prima del suo inizio.
     * @param hackathon L'hackathon di riferimento.
     * @param utente L'utente visualizzatore.
     */
    public void visualizzaIscritti(Hackathon hackathon, Utente utente) {
        ArrayList<Team> iscritti;

        try {
            DAO_Team daoTeam = new DAO_Team();
            iscritti = daoTeam.findByHackathonBeforeStart(hackathon.getTitolo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        VisualizzaIscritti.main(this, hackathon, iscritti, utente);
    }

    /**
     * Mostra l'elenco dei team iscritti, gestendo stati diversi (prima dell'inizio, in corso, finito).
     * Se l'hackathon è finito, mostra anche i voti.
     * @param hackathon L'hackathon di riferimento.
     * @param utente L'utente visualizzatore.
     */
    public void visualizzaTeam(Hackathon hackathon, Utente utente) {
        ArrayList<Team> iscritti;

        try {
        DAO_Team daoTeam = new DAO_Team();

            if(hackathon.getDataFine().isBefore(oggi)) {
                //chiamata con voti
                iscritti = daoTeam.findByHackathonAfterEnd(hackathon.getTitolo());;
            } else if(hackathon.getDataInizio().isBefore(oggi)) {
                //chiamata con giudici votanti
                iscritti = daoTeam.findByHackathonInBetween();
            } else {
                iscritti = daoTeam.findByHackathonBeforeStart(hackathon.getTitolo());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        VisualizzaIscritti.main(this, hackathon, iscritti, utente);
    }

    /**
     * Squalifica un utente da un Hackathon.
     * @param utente Username dell'utente da squalificare.
     * @param hackathon Hackathon da cui rimuoverlo.
     */
    public void squalifica(String utente, Hackathon hackathon) {
        try {
            DAO_Hackathon daoHackathon =  new DAO_Hackathon();
            daoHackathon.squalifica(utente, hackathon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera e visualizza il file relativo al problema/traccia dell'Hackathon.
     * @param hackathon L'hackathon corrente.
     */
    public void mostraProblema(Hackathon hackathon) {
        ArrayList<String> file =  new ArrayList<>();
        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            file.add(daoHackathon.getProblema(hackathon.getTitolo()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        VisualizzaFile.main(this, file, 0);
    }

    /**
     * Salva la descrizione del problema/traccia per un Hackathon.
     * @param hackathonGUI GUI dell'hackathon.
     * @param hackathon L'oggetto hackathon.
     * @param problema Testo della traccia.
     */
    public void salvaProblema(gui.Hackathon hackathonGUI, Hackathon hackathon, String problema) {
        try {
            DAO_Hackathon daoHackathon = new DAO_Hackathon();
            daoHackathon.salvaProblema(hackathon.getTitolo(), problema);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Rimuove un intero team e squalifica tutti i suoi partecipanti.
     * @param team Team da rimuovere.
     * @param hackathon Hackathon di riferimento.
     */
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

    /**
     * Visualizza i file consegnati da un team specifico.
     * @param team Nome del team.
     * @param hackathon Hackathon di riferimento.
     */
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

    /**
     * Salva un nuovo file di progetto per un team.
     * @param membroTeamHome GUI del membro team.
     * @param team Oggetto team.
     * @param nomeFile Nome del file da salvare.
     * @param contenuto Contenuto del file.
     * @return true se il salvataggio ha successo, false se il nome file esiste già.
     */
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

    /**
     * Gestisce la creazione di un nuovo Hackathon verificando la validità delle date e dei limiti numerici.
     * @param creaHackathon Form GUI di creazione.
     * @param organizzatore Organizzatore che crea l'evento.
     */
    public void creaHackathon(CreazioneHackathon creaHackathon, Organizzatore organizzatore) {
            LocalDate dataInizio = (LocalDate) creaHackathon.getDataInizioSpinner().getValue();
            LocalDate dataFine = (LocalDate) creaHackathon.getDataFineSpinner().getValue();
            int limiteIscritti = (Integer) creaHackathon.getLimiteIscrittiSpinner().getValue();
            int limiteComponentiSquadra = (Integer) creaHackathon.getLimiteComponentiSquadreSpinner().getValue();
            String titolo = creaHackathon.getTitoloTextField().getText();
            String sede = creaHackathon.getSedeTextField().getText();

            if (dataInizio.isAfter(dataFine) || dataInizio.isBefore(oggi)) {
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

    /**
     * Gestisce la partecipazione di un utente a un hackathon, convertendolo in partecipante.
     * @param hackathonGUI GUI dell'hackathon.
     * @param utente Utente che vuole iscriversi.
     * @param hackathon Hackathon a cui iscriversi.
     * @return L'oggetto Utente aggiornato.
     */
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

    /**
     * Gestisce la creazione di un team da parte di un partecipante.
     * @param creaTeam Form GUI di creazione team.
     * @param utente Utente capo-team.
     * @param home Interfaccia home.
     * @return L'oggetto MembroTeam creato.
     */
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

    /**
     * Apre l'interfaccia grafica per la modifica dei dati di un Hackathon.
     * Il metodo verifica che l'evento non sia ancora iniziato prima di procedere.
     * @param hackathonGUI  Il frame della visualizzazione hackathon corrente.
     * @param organizzatore L'utente organizzatore che richiede la modifica.
     * @param hackathon     L'oggetto hackathon da modificare.
     * @param giudici       La lista dei giudici attualmente assegnati.
     */
    public void modificaHackathon(gui.Hackathon hackathonGUI, Organizzatore organizzatore, Hackathon hackathon, ArrayList<Organizzatore> giudici) { //da vedere se funziona
        if(hackathon.getDataInizio().isAfter(oggi)) {
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

    /**
     * Valida i dati inseriti nel form e aggiorna i dati dell'Hackathon nel database.
     * Gestisce la chiusura della finestra di modifica in caso di successo.
     * @param modificaHackathon Il frame del form di modifica.
     * @param organizzatore     L'organizzatore che conferma le modifiche.
     * @param hackathon         L'oggetto hackathon con i nuovi dati da salvare.
     * @param giudici           La lista aggiornata dei giudici.
     */
    public void modificaHackathon(ModificaHackathon modificaHackathon, Organizzatore organizzatore, Hackathon hackathon, ArrayList<Organizzatore> giudici) {
            LocalDate dataInizio = (LocalDate) modificaHackathon.getDataInizioSpinner().getValue();
            LocalDate dataFine = (LocalDate) modificaHackathon.getDataFineSpinner().getValue();
            int limiteIscritti = (Integer) modificaHackathon.getLimiteIscrittiSpinner().getValue();
            int limiteComponentiSquadra = (Integer) modificaHackathon.getLimiteComponentiSquadreSpinner().getValue();
            String titolo = modificaHackathon.getTitoloTextField().getText();
            String sede = modificaHackathon.getSedeTextField().getText();


            if (dataInizio.isAfter(dataFine) || dataInizio.isBefore(oggi)) {
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

    /**
     * Cerca gli utenti nel database filtrandoli per una chiave di ricerca.
     * Versione specifica per le funzionalità lato Utente.
     * @param cercaUtenti L'interfaccia dei risultati di ricerca.
     * @param utente      L'utente che esegue la ricerca.
     * @param ricercato   La stringa (username) da cercare.
     */
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

    /**
     * Cerca gli utenti nel database con privilegi di Organizzatore.
     * Permette una visualizzazione completa dei profili trovati.
     * @param cercaUtenti L'interfaccia dei risultati di ricerca.
     * @param utente      L'organizzatore che esegue la ricerca.
     * @param ricercato   La stringa (username) da cercare.
     */
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

    /**
     * Rimuove un utente dal sistema e aggiorna la visualizzazione della ricerca.
     * @param cercaUtenti L'interfaccia corrente per rinfrescare la lista.
     * @param utente      L'utente da eliminare.
     * @param ricercato   La chiave di ricerca per ripristinare il filtro precedente.
     */
    public void cancellaUtente(CercaUtenti cercaUtenti, Utente utente, String ricercato) {
        try {
            DAO_Utente daoUtente = new DAO_Utente();
            daoUtente.delete(utente.USERNAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.cercaUtentiO(cercaUtenti, utente, ricercato);
    }

    /**
     * Invia un invito per conto di un Membro del Team a un potenziale partecipante.
     * @param cercaUtenti  La GUI da cui è partito l'invito.
     * @param mittente     Il membro del team che effettua la chiamata.
     * @param destinatario L'utente che riceverà l'invito nel proprio pannello.
     */
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

    /**
     * Invia un invito ufficiale da parte di un Organizzatore per reclutare nuovi gestori.
     * @param cercaUtenti  La GUI da cui è partito l'invito.
     * @param mittente     L'organizzatore che invia la richiesta.
     * @param destinatario L'utente invitato.
     */
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

    /**
     * Mostra una finestra di dialogo per inserire un voto da 1 a 10 per un team specifico.
     * @param team Team da votare.
     * @param hackathon Hackathon di riferimento.
     * @param utente Giudice che effettua la votazione.
     */
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