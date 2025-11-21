package database.dao;

import database.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DAO_Utente {

    private Connection connection;

    public DAO_Utente() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    public boolean save(Utente utente, String password) throws SQLException {
        String sql = "INSERT INTO UTENTE (Username, Password, Ruolo) VALUES (?, ?, 1)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utente.USERNAME);
            stmt.setString(2, password);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio dell'utente: " + e.getMessage(), e);
        }
    }

    public boolean delete(String username) throws SQLException {
        String sql = "UPDATE utente SET ruolo = 30 WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }

        DAO_Hackathon daoHackathon = new DAO_Hackathon();
        Hackathon hackathon = daoHackathon.findHackathonCorrente();
        if (hackathon == null) {return true;}

        Date oggi = Date.valueOf(LocalDate.now());
        if (hackathon.getDataInizio().before(oggi)) {

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


    public UtenteBase login(String username, String password) throws SQLException {
        String sql = "SELECT Username, Ruolo FROM UTENTE WHERE Username = ? AND Password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int ruolo = rs.getInt("Ruolo");
                    String user = rs.getString("Username");

                    UtenteBase utente;

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
                            utente = new UtenteBase(user, 30);
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

public ArrayList<Team> findByHackathon(String hackathon) throws SQLException {
    ArrayList<Team> teams = new ArrayList<>();

    String sql = """
        SELECT p.nometeam, p.username
        FROM partecipante_hackathon p
        WHERE p.hackathon = ?
        ORDER BY p.nometeam NULLS LAST, p.username
    """;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, hackathon);

        try (ResultSet rs = stmt.executeQuery()) {
            if (!rs.next()) {
                return teams;
            }

            String nomeTeam = rs.getString("nometeam");
            String username = rs.getString("username");
            boolean controllo;

            if (nomeTeam.isBlank()) {
                nomeTeam = "Senza_Team";
                controllo = false;
            } else {
                controllo = true;
            }

            Team team = new Team(nomeTeam, username);
            teams.add(team);

            while (rs.next() && controllo) {
                nomeTeam = rs.getString("nometeam");
                username = rs.getString("username");

                if (nomeTeam == null) {
                    nomeTeam = "Senza_Team";
                    controllo = false;
                }

                if (team.NOME_TEAM.equals(nomeTeam)) {
                    team.addPartecipante(username);
                } else {
                    team = new Team(nomeTeam, username);
                    teams.add(team);
                }
            }

            while (rs.next() && !controllo) {
                username = rs.getString("username");
                team.addPartecipante(username);
            }
        }
    }

    return teams;
}

}