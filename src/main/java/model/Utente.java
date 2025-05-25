package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Utente.
 */
public class Utente {
    public final String USERNAME;
    private String password;
    public ArrayList<Impegno> impegni = new ArrayList();
    private ArrayList<Invito> inviti = new ArrayList();
    /**
     * Instantiates a new Utente.
     */
    Utente(String USERNAME, String password) {
        this.USERNAME = USERNAME;
        this.password = password;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public void disponibilita(Date dataInizio, Date dataFine){} //funzione utilizzata per vedere se si è disponibili

    public void registrazione(Hackathon HACKATHON, Date dataInizio, Date dataFine){} // diventa partecipante se è disponibile

    public void creaHackathon(Hackathon HACKATHON, Date dataInizio, Date dataFine){} // crea hackathon diventa organizzatore se è disponibile

    public void aggiungiInvito(Invito Invito){
        inviti.add(Invito);
    }

    public void accettaInvito(){}

    public void rifiutaInvito(){}
}

