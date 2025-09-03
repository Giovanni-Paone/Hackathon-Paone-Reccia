package database.dao;

import database.ConnessioneDatabase;
import model.Hackathon;
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
                                       organizzatore, dataaperturaregistrazioni) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitolo());
            stmt.setString(2, hackathon.getSede());
            stmt.setDate(3, new java.sql.Date(hackathon.getDataInizio().getTime()));
            stmt.setDate(4, new java.sql.Date(hackathon.getDataFine().getTime()));
            stmt.setInt(5, hackathon.getMaxIscritti());
            stmt.setInt(6, hackathon.getMaxTeamSize());
            stmt.setString(9, hackathon.getOrganizzatore());
            stmt.setDate(10, null); // vedere come fare

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


    public ArrayList<Hackathon> findAll() throws SQLException {
        String sql = "SELECT * FROM hackathon ORDER BY datafine DESC";
        ArrayList<Hackathon> hackathons = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hackathons.add(mapRowToHackathon(rs));
            }
        }
        return hackathons;
    }

    public ArrayList<Hackathon> getByOrganizzatore(String organizzatore) throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE organizzatore LIKE ? ORDER BY datafine DESC";
        ArrayList<Hackathon> hackathons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, organizzatore + "%"); // CORRETTO

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hackathons.add(mapRowToHackathon(rs));
                }
            }
        }
        return hackathons;
    }


        /* vedere che fare
    public boolean openRegistrations(String titolo) throws SQLException {
        String sql = "UPDATE hackathon SET aperturaregistrazioni = true, dataaperturaregistrazioni = CURRENT_DATE WHERE titolo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            return stmt.executeUpdate() > 0;
        }
    }
     */

    public ArrayList<Hackathon> getFinishedHackathons() throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE datafine < CURRENT_DATE";
        ArrayList<Hackathon> hackathons = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hackathons.add(mapRowToHackathon(rs));
            }
        }
        return hackathons;
    }

    /*
    public boolean isHackathonFinished(String titolo) throws SQLException {
        String sql = "SELECT datafine FROM hackathon WHERE titolo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date dataFine = rs.getDate("datafine");
                    return dataFine != null && dataFine.before(new java.util.Date());
                }
            }
        }
        return false;
    }

     */


    private Hackathon mapRowToHackathon(ResultSet rs) throws SQLException {
        Hackathon hackathon = new Hackathon(
                rs.getString("titolo"),
                rs.getString("sede"),
                rs.getString("organizzatore"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                rs.getInt("maxiscritti"),
                rs.getInt("maxteamsize"),
                (rs.getInt("nteamis c ritti"))-1,
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
//per farlo andare su github che non era andato
}