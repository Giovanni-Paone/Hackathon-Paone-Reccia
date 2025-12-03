/**
 * Estende la classe Utente e rappresenta un utente con il ruolo specifico
 * di Organizzatore all'interno del sistema (es. gestione Hackathon, permessi, ecc.).
 * Eredita tutte le proprietà e i metodi della classe Utente.
 *
 * @see UtenteBase
 */
package model;

public class Organizzatore extends Utente {

    /**
     * Costruisce un oggetto Organizzatore.
     * Chiama il costruttore della superclasse (Utente) per inizializzare
     * il nickname e il ruolo dell'Organizzatore.
     *
     * @param NICKNAME L'username univoco (nickname) dell'organizzatore.
     * @param ruolo Il codice numerico che rappresenta il ruolo.
     */
    public Organizzatore(String NICKNAME, int ruolo) {super(NICKNAME, ruolo);}
}
