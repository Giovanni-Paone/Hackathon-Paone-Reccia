package interfaceDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Hackathon;
import model.Organizzatore;
import model.Utente;


public interface Interface_DAO_Hackathon {

    /**
     * Salva un nuovo hackathon nel database.
     * @param hackathon L'oggetto Hackathon contenente i dati da inserire.
     * @return true se l'inserimento avviene con successo, false se il titolo è già presente (violazione unique).
     * @throws SQLException Se si verifica un errore SQL imprevisto.
     */
    boolean save(Hackathon hackathon) throws SQLException;

    /**
     * Aggiorna i dati di un hackathon esistente identificato dal titolo precedente.
     * @param oldtitolo Il titolo attuale dell'hackathon nel database (chiave di ricerca).
     * @param organizzatore L'organizzatore che effettua la modifica.
     * @param hackathon L'oggetto Hackathon con i nuovi valori aggiornati.
     * @return true se l'aggiornamento avviene con successo, false in caso di conflitto di nomi.
     * @throws SQLException In caso di errore durante l'esecuzione della query.
     */
    boolean update(String oldtitolo, String organizzatore, Hackathon hackathon) throws SQLException;

    /**
     * Recupera l'hackathon corrente, ovvero quello la cui data di fine è successiva alla data attuale.
     * @return L'oggetto Hackathon trovato, oppure null se non ci sono eventi attivi.
     * @throws SQLException Se si verifica un errore nella query.
     */
    Hackathon findHackathonCorrente() throws SQLException;

    /**
     * Recupera una lista di hackathon filtrati per titolo e/o organizzatore.
     * I risultati sono ordinati per data di fine decrescente.
     * @param hackathon Filtro per il titolo dell'evento.
     * @param organizzatore Filtro per lo username dell'organizzatore.
     * @return ArrayList di oggetti Hackathon corrispondenti ai filtri.
     * @throws SQLException Se la query fallisce.
     */
    ArrayList<Hackathon> getHackathons(String hackathon, String organizzatore) throws SQLException;

    /**
     * Abilita le iscrizioni per un determinato hackathon e imposta la data di apertura a oggi.
     * @param titolo Il titolo dell'hackathon da attivare.
     * @return true se l'operazione è andata a buon fine, false altrimenti.
     * @throws SQLException Se si verifica un errore durante l'aggiornamento del record.
     */
    boolean aperturaRegistrazioni(String titolo) throws SQLException;

    /**
     * Metodo helper privato per mappare una riga del ResultSet in un oggetto Hackathon.
     * Include logica di business per chiudere automaticamente le registrazioni se la data d'inizio è passata
     * o per aggiornare lo stato di controllo se l'evento è concluso.
     * @param rs Il ResultSet derivante da una query sulla tabella hackathon.
     * @return Un oggetto Hackathon popolato con i dati del database.
     * @throws SQLException Se si verifica un errore nel recupero delle colonne.
     */
    Hackathon creaHackathon(ResultSet rs) throws SQLException;

    /**
     * Registra un utente come partecipante a un hackathon.
     * L'operazione aggiorna il ruolo dell'utente a 'Partecipante' (codice 2) e inserisce il legame nella tabella di join.
     * @param hackathon L'oggetto Hackathon a cui iscriversi.
     * @param utente L'utente che intende partecipare.
     * @return Una nuova istanza di Utente con il ruolo aggiornato.
     * @throws SQLException In caso di violazione di vincoli o errore di connessione.
     */
    Utente partecipa(Hackathon hackathon, Utente utente) throws SQLException;

    /**
     * Recupera l'elenco di tutti gli organizzatori che fungono da giudici per un determinato hackathon.
     * @param hackathon L'hackathon di riferimento.
     * @return ArrayList di oggetti Organizzatore (giudici), ordinati alfabeticamente per username.
     * @throws SQLException Se la query fallisce.
     */
    ArrayList<Organizzatore> findAllGiudici(Hackathon hackathon) throws SQLException;

    /**
     * Rimuove un utente dalla lista dei partecipanti di un hackathon e ne cambia il ruolo in 'Squalificato' (codice 29).
     * @param username Lo username dell'utente da squalificare.
     * @param hackathon L'hackathon da cui l'utente deve essere rimosso.
     * @throws SQLException In caso di errore durante la cancellazione o l'aggiornamento del ruolo.
     */
    void squalifica(String username, Hackathon hackathon) throws SQLException;

    /**
     * Salva o aggiorna la descrizione del problema/tema assegnato a un hackathon.
     * @param hackathon Il titolo dell'hackathon.
     * @param problema Il testo descrittivo del problema da risolvere.
     * @throws SQLException Se l'aggiornamento fallisce.
     */
    void salvaProblema(String hackathon, String problema) throws SQLException;

    /**
     * Recupera la descrizione del problema associato a un determinato hackathon.
     * @param hackathon Il titolo dell'hackathon di cui si vuole conoscere il tema.
     * @return La stringa contenente il problema, oppure null se non è ancora stato definito o l'hackathon non esiste.
     * @throws SQLException In caso di errore nella lettura dal database.
     */
    String getProblema(String hackathon) throws SQLException;
}
