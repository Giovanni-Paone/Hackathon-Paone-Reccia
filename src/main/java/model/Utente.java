package model;

import java.util.ArrayList;

/**
 * The type Utente.
 */
public class Utente {
    public final String USERNAME;
    //la pasword sarà solo nella base di dati come controllo
    private ArrayList<InvitoTeam> invitiTeam = new ArrayList();
    private InvitoGiudice invitoGiudice;
    /**
     * Instantiates a new Utente.
     */
    public Utente(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    /**
     * Gets login.
     *
     * @return the login
     */

    public InvitoTeam getInvito(int indice){return invitiTeam.get(indice);}
    public void addInvito(InvitoTeam Invito){invitiTeam.add(Invito);}

    public void accettaInvitoTeam(int indice){} //diventa partecipante del team
    public void rifiutaInvito(int indice){
        this.invitiTeam.remove(indice);
    }

    public InvitoGiudice getInvitoGiudice(int indice){return this.invitoGiudice;}
    public void addInvitoGiudice(InvitoGiudice InvitoGiudice) {this.invitoGiudice = invitoGiudice;}

    public void accettaInvitoGiudice(){}//diventa giudice

    //public void registrazione(Hackathon HACKATHON){} // diventa partecipante
    // funzione rimossa perchè forse verrà risolta con la base di dati
}