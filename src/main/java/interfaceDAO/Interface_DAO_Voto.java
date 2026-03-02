package interfaceDAO;

import java.sql.SQLException;

/**
 * Interfaccia per la gestione della persistenza dei voti.
 * Definisce il contratto per le operazioni di salvataggio delle valutazioni dei team.
 */
public interface Interface_DAO_Voto {

    /**
     * Salva un nuovo voto nel database relativo alla performance di un team.
     * * @param hackathon Il nome o l'identificativo dell'hackathon di riferimento.
     * @param team      Il nome del team che riceve il voto.
     * @param giudice   Il nome o l'identificativo del giudice che esprime la valutazione.
     * @param voto      Il punteggio numerico assegnato.
     * @return true se il salvataggio è avvenuto con successo, false altrimenti.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    boolean save(String hackathon, String team, String giudice, int voto) throws SQLException;
}