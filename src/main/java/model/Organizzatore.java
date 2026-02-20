package model;

/**
 * Rappresenta un utente con il ruolo specifico di Organizzatore.
 * Questa classe estende {@link Utente} e fornisce le basi per la gestione
 * degli Hackathon e dei permessi amministrativi nel sistema.
 *
 * @see Utente
 */
public class Organizzatore extends Utente {

    /**
     * Costruisce un oggetto Organizzatore.
     * Inizializza l'istanza richiamando il costruttore della superclasse.
     *
     * @param NICKNAME L'username dell'organizzatore.
     * @param ruolo Il codice numerico che rappresenta il ruolo.
     */
    public Organizzatore(String NICKNAME, int ruolo) {super(NICKNAME, ruolo);}
}
