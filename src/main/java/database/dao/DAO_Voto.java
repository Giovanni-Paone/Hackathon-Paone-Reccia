package database.dao;
import database.ConnessioneDatabase;
import interfaceDAO.Interface_DAO_Voto;

import java.sql.*;

/**
 * DAO per la gestione della tabella dei voti nel database.
 * Questa classe fornisce i metodi per persistere le valutazioni assegnate dai giudici
 * ai vari team durante gli hackathon.
 */
public class DAO_Voto implements Interface_DAO_Voto {

    private Connection connection;

    /**
     * Inizializza un nuovo oggetto DAO_Voto recuperando la connessione
     * tramite il singleton {@link ConnessioneDatabase}.
     * @throws SQLException Se si verifica un errore durante il recupero della connessione.
     */
    public DAO_Voto() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    /**
     * Salva un nuovo voto nel database relativo alla performance di un team.
     * @param hackathon Il nome o l'identificativo dell'hackathon di riferimento.
     * @param team      Il nome del team che riceve il voto.
     * @param giudice   Il nome o l'identificativo del giudice che esprime la valutazione.
     * @param voto      Il punteggio numerico assegnato.
     * @return {@code true} se il salvataggio è avvenuto con successo, {@code false} altrimenti.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public boolean save(String hackathon, String team, String giudice, int voto) throws SQLException {
        String sql = "INSERT INTO voto_team (hackathon, team, giudice, voto) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon);
            stmt.setString(2, team);
            stmt.setString(3, giudice);
            stmt.setInt(4, voto);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore DAO aggiungiVoto(): " + e.getMessage());
            return false;
        }
    }

}
