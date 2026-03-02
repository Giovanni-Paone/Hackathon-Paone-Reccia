package database.dao;

import database.ConnessioneDatabase;
import model.*;
import interfaceDAO.Interface_DAO_Utente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * DAO per la gestione della persistenza degli utenti nel database.
 * Fornisce metodi per la registrazione, la cancellazione logica, l'autenticazione
 * e la ricerca degli utenti.
 */
public class DAO_Utente implements Interface_DAO_Utente {

    private Connection connection;

    /**
     * Costruttore della classe. Inizializza la connessione al database
     * utilizzando il pattern Singleton della classe {@link ConnessioneDatabase}.
     * @throws SQLException Se si verifica un errore durante il recupero della connessione.
     */
    public DAO_Utente() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    /**
     * Registra un nuovo utente nel sistema con ruolo predefinito (Ruolo = 1).
     * @param utente   Lo username del nuovo utente.
     * @param password La password associata all'utente.
     * @return {@code true} se l'inserimento è avvenuto con successo,
     * {@code false} se lo username è già esistente (Violazione Unique Constraint).
     * @throws SQLException Se si verifica un errore SQL imprevisto.
     */
    @Override
    public boolean save(String utente, String password) throws SQLException {
        String sql = "INSERT INTO UTENTE (Username, Password, Ruolo) VALUES (?, ?, 1)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utente);
            stmt.setString(2, password);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                return false;   // username duplicato
            } else throw new SQLException("Errore durante il salvataggio dell'utente: " + e.getMessage(), e);
        }
    }

    /**
     * Gestisce la rimozione di un utente. Esegue una cancellazione logica (imposta ruolo a 30)
     * e gestisce la rimozione dell'utente dai team e dalle partecipazioni se l'hackathon
     * corrente non è ancora iniziato.
     * @param username Lo username dell'utente da eliminare.
     * @return {@code true} se l'operazione è completata (default).
     * @throws SQLException Se si verifica un errore durante le query di aggiornamento o eliminazione.
     */
    @Override
    public boolean delete(String username) throws SQLException {
        String sql = "UPDATE utente SET ruolo = 30 WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }

        DAO_Hackathon daoHackathon = new DAO_Hackathon();
        Hackathon hackathon = daoHackathon.findHackathonCorrente();
        if (hackathon == null) {return true;}

        LocalDate oggi = LocalDate.now();
        if (hackathon.getDataInizio().isBefore(oggi)) {

            String nomeTeam = null;
            sql = "SELECT nometeam FROM partecipante_hackathon WHERE username = ? AND hackathon = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, hackathon.getTitolo());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        nomeTeam = rs.getString("nometeam");
                    }
                }
            }

            sql = "DELETE FROM partecipante_hackathon WHERE username = ? AND hackathon = ?";
            try (PreparedStatement del = connection.prepareStatement(sql)) {
                del.setString(1, username);
                del.setString(2, hackathon.getTitolo());
                del.executeUpdate();
            }

            if (nomeTeam != null) {
                int rimasti = 0;
                sql = "SELECT COUNT(*) FROM partecipante_hackathon WHERE hackathon = ? AND nometeam = ?";
                try (PreparedStatement cnt = connection.prepareStatement(sql)) {
                    cnt.setString(1, hackathon.getTitolo());
                    cnt.setString(2, nomeTeam);
                    try (ResultSet rs = cnt.executeQuery()) {
                        if (rs.next()) rimasti = rs.getInt(1);
                    }
                }

                if (rimasti == 0) {
                    sql = "DELETE FROM team WHERE hackathon = ? AND nome = ?";
                    try (PreparedStatement delTeam = connection.prepareStatement(sql)) {
                        delTeam.setString(1, hackathon.getTitolo());
                        delTeam.setString(2, nomeTeam);
                        delTeam.executeUpdate();
                    }
                }
            }
        }

        return true;
    }

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
    @Override
    public Utente login(String username, String password) throws SQLException {
        String sql = "SELECT Username, Ruolo FROM UTENTE WHERE Username = ? AND Password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int ruolo = rs.getInt("Ruolo");
                    String user = rs.getString("Username");

                    Utente utente;

                    switch (ruolo) {
                        case -1: //Organizzatore
                            utente = new Organizzatore(user, -1);
                            break;

                        case 0: //Giudice
                            utente = new Organizzatore(username, 0);
                            break;

                        case 1:
                            utente = new Utente(user, 1);
                            break;

                        case 2: // Partecipante
                            utente = new Utente(user, 2);
                            break;

                        case 3:// Membro di un Team
                            sql = """
                            
                                    SELECT ph.nometeam, ph.hackathon
                            FROM partecipante_hackathon ph
                            JOIN hackathon h ON ph.hackathon = h.titolo
                            WHERE ph.username = ? AND h.datafine > CURRENT_DATE
                            """;

                            try (PreparedStatement stmtTeam = connection.prepareStatement(sql)) {
                                stmtTeam.setString(1, user);

                                try (ResultSet rsTeam = stmtTeam.executeQuery()) {
                                    rsTeam.next();
                                    String nomeTeam = rsTeam.getString("nometeam");
                                    String titoloHackathon = rsTeam.getString("hackathon");

                                    Team team = new Team(nomeTeam);

                                    sql = "SELECT username FROM partecipante_hackathon WHERE nometeam = ? AND hackathon = ?";
                                    try (PreparedStatement stmtPartecipanti = connection.prepareStatement(sql)) {
                                        stmtPartecipanti.setString(1, nomeTeam);
                                        stmtPartecipanti.setString(2, titoloHackathon);

                                        try (ResultSet rsPart = stmtPartecipanti.executeQuery()) {
                                            while (rsPart.next()) {
                                                team.partecipanti.add(rsPart.getString("username"));
                                            }
                                        }
                                    }

                                    utente = new MembroTeam(user, team);
                                }
                            }

                            break;
                        case 30:
                            utente = new Utente(user, 30);
                            break;

                        default:
                            utente = null; //in caso di errori
                            break;
                    }

                    return utente;
                }
            }
        }

        return null;
    }

    /**
     * Ricerca gli utenti attivi (esclusi amministratori e utenti cancellati) il cui username
     * inizia con la stringa fornita.
     * @param username Il prefisso dello username da cercare.
     * @return Una lista di {@link Utente} che soddisfano i criteri di ricerca.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query.
     */
    @Override
    public ArrayList<Utente> findByKey(String username) throws SQLException {
    String sql = """
            SELECT Username, Ruolo 
            FROM UTENTE 
            WHERE Username LIKE ? AND ruolo <> -1 AND ruolo <> 30 
            ORDER BY Username ASC
            """;
    ArrayList<Utente> utenti = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, username + "%"); // CORRETTO: aggiungo il %

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utente utente = new Utente(
                        rs.getString("Username"),
                        rs.getInt("Ruolo")
                );
                utenti.add(utente);
            }
        }
    } catch (SQLException e) {
        throw new SQLException("Errore durante il recupero di tutti gli utenti: " + e.getMessage(), e);
    }

    return utenti;
}

    /**
     * Ricerca utenti specifici con ruolo 1 (Utente base) o 2 (Partecipante)
     * tramite prefisso dello username.
     * @param username Il prefisso dello username da cercare.
     * @return Una lista di {@link Utente} filtrata per ruolo 1 o 2.
     * @throws SQLException Se si verifica un errore durante il recupero dei dati.
     */
    @Override
    public ArrayList<Utente> findByKeyUtente(String username) throws SQLException {
    String sql = """
            SELECT Username, Ruolo FROM UTENTE
            WHERE Username LIKE ? AND (ruolo = 1 OR ruolo = 2)
            ORDER BY Username ASC
            """;
    ArrayList<Utente> utenti = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, username + "%");

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utente utente = new Utente(rs.getString("Username"), rs.getInt("Ruolo"));
                utenti.add(utente);
            }
        }
    } catch (SQLException e) {
        throw new SQLException("Errore durante il recupero di tutti gli utenti: " + e.getMessage(), e);
    }

    return utenti;
}

}