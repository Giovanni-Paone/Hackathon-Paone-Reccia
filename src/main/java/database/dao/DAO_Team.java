package database.dao;

import database.ConnessioneDatabase;
import model.Hackathon;
import model.MembroTeam;
import model.Team;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;

public class DAO_Team {

    private final Connection connection;

    public DAO_Team() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    public boolean save(String nomeTeam, Utente utente) throws SQLException {
        DAO_Hackathon daoHackathon = new DAO_Hackathon();
        Hackathon hackathon = daoHackathon.findHackathonCorrente();

        try {

            String sql = "INSERT INTO team (nome, hackathon) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, nomeTeam);
                stmt.setString(2, hackathon.getTitolo());
                stmt.executeUpdate();
            }

            sql = "UPDATE partecipante_hackathon SET nometeam = ? WHERE hackathon = ? AND username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, nomeTeam);
                stmt.setString(2, hackathon.getTitolo());
                stmt.setString(3, utente.USERNAME);
                stmt.executeUpdate();
            }

            sql = "UPDATE utente SET ruolo = 3 WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, utente.USERNAME);
                int rowsUpdated = stmt.executeUpdate();

                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState()))
                return false;
            else throw new SQLException("Errore durante il salvataggio del team: " + e.getMessage(), e);
        }
    }


    public boolean delete(String nomeTeam, Hackathon hackathon) throws SQLException {
        String sql = "DELETE FROM TEAM WHERE Nome = ? AND Hackathon = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTeam);
            stmt.setString(2, hackathon.getTitolo());
            return stmt.executeUpdate() > 0;
        }
    }

    /* probabilmente inutile, vedrò che fare
    public ArrayList<Team> findByKey(String nomeTeam, Hackathon hackathon) throws SQLException {
        String sql = "SELECT DISTINCT nometeam FROM team WHERE nometeam LIKE ? AND hackathon = ? GROUP BY hackathon, nometeam";
        ArrayList<Team> teams = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTeam + "%"); // CORRETTO
            stmt.setString(2, hackathon.getTitolo());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team(rs.getString("nometeam"), hackathon.getTitolo());
                    teams.add(team);
                }
            }
        }

        return teams;
    }

     */

    public ArrayList<Team> findByHackathonBeforeStart(String hackathon) throws SQLException {
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

                if (nomeTeam == null || nomeTeam.isBlank()) {
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

    public ArrayList<Team> findByHackathonInBetween() throws SQLException {

        ArrayList<Team> teams = new ArrayList<>();

        String sql = """
        SELECT ph.nometeam, ph.username AS partecipante, vt.giudice
        FROM partecipante_hackathon ph
        LEFT JOIN voto_team vt
          ON vt.team = ph.nometeam
         AND vt.hackathon = ph.hackathon
        ORDER BY ph.nometeam, partecipante, vt.giudice
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {

                Team team = null;

                while (rs.next()) {

                    String nomeTeam = rs.getString("nometeam");
                    String partecipante = rs.getString("partecipante");
                    String giudice = rs.getString("giudice");

                    // Se è un team nuovo, crealo
                    if (team == null || !team.NOME_TEAM.equals(nomeTeam)) {
                        team = new Team(nomeTeam);
                        teams.add(team);
                    }

                    // Aggiungi partecipante se non c'è
                    if (!team.partecipanti.contains(partecipante)) {
                        team.addPartecipante(partecipante);
                    }

                    // Aggiungi giudice se esiste e non è un duplicato
                    if (giudice != null && !team.giudiciVotanti.contains(giudice)) {
                        team.giudiciVotanti.add(giudice);
                    }
                }
            }
        }

        return teams;
    }

    public ArrayList<Team> findByHackathonAfterEnd(String hackathon) throws SQLException {
        ArrayList<Team> teams = new ArrayList<>();

        String sql = """
        SELECT t.nome AS nometeam, t.voto, ph.username AS partecipante
        FROM team t
        LEFT JOIN partecipante_hackathon ph 
            ON ph.nometeam = t.nome AND t.nomefile = 'Vuoto'
        WHERE ph.hackathon = ?
        ORDER BY 
            t.voto DESC,
            t.nome ASC,
            ph.username ASC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    return teams;
                }

                String nomeTeam = rs.getString("nometeam");
                int voto = rs.getInt("voto");
                String partecipante = rs.getString("partecipante");

                Team team = new Team(nomeTeam, partecipante, voto);
                teams.add(team);

                while (rs.next()) {
                    String nextTeam = rs.getString("nometeam");
                    int nextVoto = rs.getInt("voto");
                    String nextPartecipante = rs.getString("partecipante");

                    // stesso team
                    if (team.NOME_TEAM.equals(nextTeam)) {
                        team.addPartecipante(nextPartecipante);
                    } else {
                        team = new Team(nextTeam, nextPartecipante, nextVoto);
                        teams.add(team);
                    }
                }
            }
        }

        return teams;
    }


    public boolean saveFile(String nomeTeam, String nomeFile, String contenuto) throws SQLException {
        database.dao.DAO_Hackathon daoHackathon = new DAO_Hackathon();
        Hackathon hackathon;
        try {
            hackathon = daoHackathon.findHackathonCorrente();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Creiamo sempre un nuovo record nel team con hackathon, nomeTeam e file
        String sqlInsert = "INSERT INTO team(hackathon, nome, progressi, nomefile) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, hackathon.getTitolo());
            stmtInsert.setString(2, nomeTeam);
            stmtInsert.setString(3, contenuto);
            stmtInsert.setString(4, nomeFile);

            int inserted = stmtInsert.executeUpdate();
            return inserted > 0;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState()))
                return false;
            else throw new SQLException("Errore durante il salvataggio del file: " + e.getMessage(), e);
        }
    }

    public ArrayList<String> getFile(String team, String hackathon) {
        ArrayList<String> files = new ArrayList<>();

        String sql = "SELECT nomefile, progressi FROM team WHERE hackathon = ? AND nome = ? AND nomefile <> 'Vuoto'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, hackathon);
            stmt.setString(2, team);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    String titolo = rs.getString("nomefile");
                    String progressi = rs.getString("progressi");

                    files.add(titolo);
                    files.add(progressi);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return files;
    }


}