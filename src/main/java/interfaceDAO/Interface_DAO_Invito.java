package interfaceDAO;

import model.Invito;
import model.Utente;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Interface_DAO_Invito {

    /**
     * Salva un nuovo invito nel database. Impedisce la creazione di duplicati
     * se esiste già un invito tra lo stesso mittente e destinatario.
     * @param invito L'oggetto Invito contenente i dati del mittente e i permessi.
     * @param destinatario Lo username del destinatario dell'invito.
     * @return {@code true} se l'operazione ha successo, {@code false} se l'invito esiste già.
     * @throws SQLException Se si verifica un errore SQL durante l'inserimento.
     */
    boolean save(Invito invito, String destinatario) throws SQLException;

    /**
     * Elimina un invito specifico dal database.
     * @param utente L'utente destinatario dell'invito.
     * @param invito L'oggetto Invito da eliminare (identificato dal mittente).
     * @return {@code true} se l'invito è stato rimosso, {@code false} altrimenti.
     * @throws SQLException Se si verifica un errore nel database.
     */
    boolean delete(Utente utente, Invito invito) throws SQLException;

    /**
     * Recupera un singolo invito data la coppia destinatario e mittente.
     * @param destinatario Username del destinatario.
     * @param mittente Username del mittente.
     * @return L'oggetto {@link Invito} se trovato, {@code null} altrimenti.
     * @throws SQLException Se si verifica un errore nella query.
     */
    Invito getInvito(String destinatario, String mittente) throws SQLException;

    /**
     * Recupera tutti gli inviti ricevuti da un determinato utente.
     * Gli inviti sono ordinati per tipo di permesso e data decrescente.
     * @param destinatario L'oggetto Utente di cui cercare gli inviti.
     * @return Una lista di inviti ricevuti.
     * @throws SQLException In caso di errori SQL.
     */
    ArrayList<Invito> getInviti(Utente destinatario) throws SQLException;

    /**
     * Cerca inviti ricevuti da un destinatario filtrando per una parte dello username del mittente.
     * @param destinatario Username del destinatario.
     * @param mittente Prefisso o parte dello username del mittente (usa operatore LIKE).
     * @return Lista di inviti che soddisfano il filtro.
     * @throws SQLException In caso di errori SQL.
     */
    ArrayList<Invito> getInviti(String destinatario, String mittente) throws SQLException;

    /**
     * Gestisce la logica di accettazione di un invito.
     * Se l'invito ha {@code permesso = true}, l'utente diventa un Giudice/Organizzatore.<br>
     * Se l'invito ha {@code permesso = false}, l'utente viene aggiunto a un Team come partecipante,
     * previa verifica della disponibilità di posti nell'Hackathon e nel Team.
     * Al termine, elimina tutti gli inviti pendenti per il destinatario e, se il team è pieno,
     * elimina tutti gli inviti mandati da quel team.
     * @param destinatario L'utente che accetta l'invito.
     * @param invito L'invito da accettare.
     * @return L'oggetto {@link Utente} aggiornato col nuovo ruolo, o {@code null} se l'operazione fallisce (es. hackathon pieno).
     * @throws SQLException Se si verifica un errore durante le molteplici operazioni sul database.
     */
    Utente accetta(Utente destinatario, Invito invito) throws SQLException;
}
