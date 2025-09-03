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
            throw e;
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

    /*
    public Team listaMembri(String nomeTeam, Hackathon hackathon) throws SQLException {
        String sql = "SELECT username FROM partecipante_hackathon WHERE nometeam = ? AND hackathon = ? ORDER BY username";

        Team team = new Team(nomeTeam);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTeam);
            stmt.setString(2, hackathon.getTitolo());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MembroTeam partecipante = new MembroTeam(rs.getString("username"), team);
                    team.addPartecipante(partecipante);
                }
            }
        }

        return team;
    }
     */

    public ArrayList<Team> findByHackathon(String hackathon) throws SQLException {
        ArrayList<Team> teams = new ArrayList<>();

        String sql = """
        SELECT p.nometeam, p.username
        FROM partecipante_hackathon p
        WHERE p.hackathon = ? AND p.nometeam IS NOT NULL
        ORDER BY p.nometeam, p.username
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return teams;
                }

                String nomeTeam = rs.getString("nometeam");
                String username = rs.getString("username");

                Team team = new Team(nomeTeam, username);
                teams.add(team);

                while (rs.next()) {
                    nomeTeam = rs.getString("nometeam");
                    username = rs.getString("username");

                    if (team.NOME_TEAM.equals(nomeTeam)) {
                        team.addPartecipante(username);
                    } else {
                        team = new Team(nomeTeam, username);
                        teams.add(team);
                    }
                }
            }
        }

        return teams;
    }

}