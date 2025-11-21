package database.dao;

import database.ConnessioneDatabase;
import model.Hackathon;
import model.Organizzatore;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;

public class DAO_Hackathon {

    private final Connection connection;

    public DAO_Hackathon() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    public boolean save(Hackathon hackathon) throws SQLException {
        String sql = """
                INSERT INTO hackathon (titolo, sede, datainizio, datafine, maxiscritti, maxteamsize,
                                       organizzatore)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitolo());
            stmt.setString(2, hackathon.getSede());
            stmt.setDate(3, new java.sql.Date(hackathon.getDataInizio().getTime()));
            stmt.setDate(4, new java.sql.Date(hackathon.getDataFine().getTime()));
            stmt.setInt(5, hackathon.getMaxIscritti());
            stmt.setInt(6, hackathon.getMaxTeamSize());
            stmt.setString(7, hackathon.getOrganizzatore());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean update(String oldtitolo, String organizzatore, Hackathon hackathon) throws SQLException {
        String sql = """
            UPDATE hackathon
            SET titolo = ?, sede = ?, datainizio = ?, datafine = ?,
            maxiscritti = ?, maxteamsize = ?
            WHERE titolo = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitolo());
            stmt.setString(2, hackathon.getSede());
            stmt.setDate(3, new java.sql.Date(hackathon.getDataInizio().getTime()));
            stmt.setDate(4, new java.sql.Date(hackathon.getDataFine().getTime()));
            stmt.setInt(5, hackathon.getMaxIscritti());
            stmt.setInt(6, hackathon.getMaxTeamSize());
            stmt.setString(7, oldtitolo);

            return stmt.executeUpdate() > 0;
        }
    }

    public Hackathon findHackathonCorrente() throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE datafine > CURRENT_DATE";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToHackathon(rs);
                }
            }
        }
        return null;
    }

    public Hackathon findByKey(String titolo) throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE titolo LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo + "%"); // CORRETTO

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToHackathon(rs);
                }
            }
        }
        return null;
    }

    public ArrayList<Hackathon> getHackathons(String hackathon, String organizzatore) throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE titolo LIKE ? AND organizzatore LIKE ? ORDER BY datafine DESC";
        ArrayList<Hackathon> hackathons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon + "%");
            stmt.setString(2, organizzatore + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hackathons.add(mapRowToHackathon(rs));
                }
            }
        }
        return hackathons;
    }

    public boolean aperturaRegistrazioni(String titolo) throws SQLException {
        String sql = "UPDATE hackathon SET aperturaregistrazioni = true, dataaperturaregistrazioni = CURRENT_DATE WHERE titolo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            return stmt.executeUpdate() > 0;
        }
    }

    private Hackathon mapRowToHackathon(ResultSet rs) throws SQLException {
        Hackathon hackathon = new Hackathon(
                rs.getString("titolo"),
                rs.getString("sede"),
                rs.getString("organizzatore"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                rs.getInt("maxiscritti"),
                rs.getInt("maxteamsize"),
                (rs.getInt("nteamiscritti"))-1,
                rs.getInt("utentiIscritti"),
                rs.getBoolean("aperturaregistrazioni")
        );
        return hackathon;
    }

    public Utente partecipa(Hackathon hackathon, Utente utente) throws SQLException {
        String updateSql = "UPDATE utente SET ruolo = 2 WHERE username = ?";

        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            updateStmt.setString(1, utente.USERNAME);
            updateStmt.executeUpdate();
        }

        String insertSql = "INSERT INTO partecipante_hackathon (hackathon, username) VALUES (?, ?)";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setString(1, hackathon.getTitolo());
            insertStmt.setString(2, utente.USERNAME);
            insertStmt.executeUpdate();
        }

        return new Utente(utente.USERNAME, 2);
    }

    public ArrayList<Organizzatore> findAllGiudici(Hackathon hackathon) throws SQLException {
        ArrayList<Organizzatore> result = new ArrayList<>();
        String sql = "SELECT username FROM giudice WHERE hackathon = ? ORDER BY username ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitolo());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new Organizzatore(rs.getString("username"), 0));
                }
            }
        }

        return result;
    }

    public void squalifica(String username, Hackathon hackathon) throws SQLException {
        String sql = "DELETE FROM partecipanti WHERE hackathon = ? AND username = ?";

        try (PreparedStatement deleteStmt = connection.prepareStatement(sql)) {
            deleteStmt.setString(1, hackathon.getTitolo());
            deleteStmt.setString(2, username);
            deleteStmt.executeUpdate();
        }

        sql = "UPDATE utente SET ruolo = 29 WHERE username = ?";

        try (PreparedStatement updateStmt = connection.prepareStatement(sql)) {
            updateStmt.setString(1, username);
            updateStmt.executeUpdate();
        }

    }



}