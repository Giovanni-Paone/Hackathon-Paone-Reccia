package interfaceDAO;

import model.Hackathon;
import model.Team;
import model.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Interface_DAO_Team {

    /**
     * Registra un nuovo team nel database e associa l'utente creatore ad esso.
     * Aggiorna inoltre il ruolo dell'utente per riflettere la creazione del team.
     * @param nomeTeam Il nome del team da creare.
     * @param utente L'utente che crea il team (diventa membro).
     * @return true se il salvataggio avviene con successo, false se il team esiste già (violazione unique).
     * @throws SQLException Se si verificano errori nell'esecuzione delle query SQL.
     */
    boolean save(String nomeTeam, Utente utente) throws SQLException;

    /**
     * Rimuove un team dal database per un determinato hackathon.
     * @param nomeTeam Nome del team da eliminare.
     * @param hackathon Oggetto Hackathon di riferimento.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    boolean delete(String nomeTeam, Hackathon hackathon) throws SQLException;

    /**
     * Recupera la lista dei team e dei relativi partecipanti prima dell'inizio dell'hackathon.
     * Gestisce anche gli utenti "Senza Team" raggruppandoli sotto un unico identificativo.
     * @param hackathon Il titolo dell'hackathon.
     * @return Una lista di oggetti Team popolati con i rispettivi partecipanti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    ArrayList<Team> findByHackathonBeforeStart(String hackathon) throws SQLException;

    /**
     * Recupera i team durante lo svolgimento dell'hackathon, includendo i partecipanti
     * e la lista dei giudici che hanno già espresso un voto per quel team.
     * @return Una lista di oggetti Team con partecipanti e giudici votanti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    ArrayList<Team> findByHackathonInBetween() throws SQLException;

    /**
     * Recupera i team dopo la fine dell'hackathon, ordinandoli per voto decrescente.
     * Include solo i team che non hanno caricato file (contrassegnati come 'Vuoto').
     * @param hackathon Il titolo dell'hackathon terminato.
     * @return Una lista di Team ordinata per classifica (voto).
     * @throws SQLException Se si verifica un errore SQL.
     */
    ArrayList<Team> findByHackathonAfterEnd(String hackathon) throws SQLException;

    /**
     * Salva i progressi di un team caricando un file (o il relativo contenuto testuale).
     * Crea un nuovo record nella tabella team per tracciare la cronologia o i diversi file.
     * @param nomeTeam Nome del team che effettua il caricamento.
     * @param nomeFile Nome del file da salvare.
     * @param contenuto Il contenuto o i progressi relativi al file.
     * @return true se il salvataggio è riuscito, false se esiste già un record identico.
     * @throws SQLException Se si verifica un errore SQL.
     */
    boolean saveFile(String nomeTeam, String nomeFile, String contenuto) throws SQLException;

    /**
     * Recupera tutti i file e i progressi associati a un team specifico per un hackathon.
     * Esclude i record che non contengono file reali (nomefile = 'Vuoto').
     * @param team Il nome del team.
     * @param hackathon Il titolo dell'hackathon.
     * @return Un'ArrayList di stringhe dove gli elementi sono alternati: [titoloFile1, contenuto1, titoloFile2, contenuto2, ...].
     */
    ArrayList<String> getFile(String team, String hackathon);
}
