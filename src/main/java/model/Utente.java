package model;

import java.util.ArrayList;

/**
 * The type Utente.
 */
public class Utente extends UtenteBase {
    //la password sarà solo nella base di dati come controllo
    private ArrayList<Invito> invitiTeam = new ArrayList();
    /**
     * Instantiates a new Utente.
     */
    public Utente(String USERNAME, int ruolo) {super(USERNAME, ruolo);}

    /**
     * Gets login.
     *
     * @return the login
     */


    /* probabilmente funzioni inutili che possono solo esseere fatte con dao, gui e controller
    public Invito getInvito(int indice){return invitiTeam.get(indice);}
    public void addInvito(Invito invito){invitiTeam.add(invito);}

    public void accettaInvitoTeam(int indice){} //diventa partecipante del team
    public void rifiutaInvito(int indice){
        this.invitiTeam.remove(indice);
    }

    public void accettaInvitoGiudice(){}//diventa giudice

    //public void registrazione(Hackathon HACKATHON){} // diventa partecipante
    // funzione rimossa perchè forse verrà risolta con la base di dati

     */
}