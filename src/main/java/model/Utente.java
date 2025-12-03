/**
 * Classe base (Superclasse) che rappresenta un utente generico all'interno
 * del sistema, definendo le proprietà fondamentali come lo username e il ruolo.
 * Tutte le tipologie specifiche di utente (es. Organizzatore) dovrebbero
 * estendere questa classe.
 */
package model;

public class Utente {
    /**
     * Lo username univoco dell'utente. È un campo immutabile (final)
     * e funge da identificativo primario
     */
    public final String USERNAME;

    /**
     * Un codice numerico che rappresenta il ruolo o il livello di accesso
     * dell'utente nel sistema.
     */
    private int ruolo;

    /**
     * Costruisce un oggetto Utente.
     *
     * @param username L'identificativo testuale univoco per l'utente (nickname).
     * @param ruolo Il codice numerico che definisce il ruolo dell'utente.
     */
    public Utente(String username, int ruolo) {
        this.USERNAME = username;
        this.ruolo = ruolo;
    }

    /**
     * Restituisce il codice numerico del ruolo assegnato all'utente.
     *
     * @return Il codice intero che rappresenta il ruolo.
     */
    public int getRuolo() {return ruolo;}
}