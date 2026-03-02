package interfaceDAO;

import model.MembroTeam;
import model.Organizzatore;
import model.Utente;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Interface_DAO_Utente {

    /**
     * Registra un nuovo utente nel sistema con ruolo predefinito (Ruolo = 1).
     * @param utente   Lo username del nuovo utente.
     * @param password La password associata all'utente.
     * @return {@code true} se l'inserimento è avvenuto con successo,
     * {@code false} se lo username è già esistente (Violazione Unique Constraint).
     * @throws SQLException Se si verifica un errore SQL imprevisto.
     */
    boolean save(String utente, String password) throws SQLException;

    /**
     * Gestisce la rimozione di un utente. Esegue una cancellazione logica (imposta ruolo a 30)
     * e gestisce la rimozione dell'utente dai team e dalle partecipazioni se l'hackathon
     * corrente non è ancora iniziato.
     * @param username Lo username dell'utente da eliminare.
     * @return {@code true} se l'operazione è completata (default).
     * @throws SQLException Se si verifica un errore durante le query di aggiornamento o eliminazione.
     */
    boolean delete(String username) throws SQLException;

    /**
     * Esegue l'autenticazione di un utente e restituisce l'oggetto specifico in base al ruolo.
     * Il metodo gestisce diversi tipi di utenti: Organizzatori, Giudici, Utenti generici
     * e Membri di un Team, caricando per questi ultimi le informazioni del team corrente.
     * @param username Lo username per il login.
     * @param password La password per il login.
     * @return Un'istanza di {@link Utente}, {@link Organizzatore} o {@link MembroTeam},
     * oppure {@code null} se le credenziali sono errate.
     * @throws SQLException Se si verifica un errore durante il recupero dei dati o la gestione del ResultSet.
     */
    Utente login(String username, String password) throws SQLException;

    /**
     * Ricerca gli utenti attivi (esclusi amministratori e utenti cancellati) il cui username
     * inizia con la stringa fornita.
     * @param username Il prefisso dello username da cercare.
     * @return Una lista di {@link Utente} che soddisfano i criteri di ricerca.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query.
     */
    ArrayList<Utente> findByKey(String username) throws SQLException;

    /**
     * Ricerca utenti specifici con ruolo 1 (Utente base) o 2 (Partecipante)
     * tramite prefisso dello username.
     * @param username Il prefisso dello username da cercare.
     * @return Una lista di {@link Utente} filtrata per ruolo 1 o 2.
     * @throws SQLException Se si verifica un errore durante il recupero dei dati.
     */
    ArrayList<Utente> findByKeyUtente(String username) throws SQLException;
}
