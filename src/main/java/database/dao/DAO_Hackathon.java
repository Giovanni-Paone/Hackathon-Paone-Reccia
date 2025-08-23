package database.dao;

import database.ConnessioneDatabase;
import model.Hackathon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_Hackathon implements DAO_Generico<Hackathon, String> {

    private final Connection connection;

    public DAO_Hackathon() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }


    @Override
    public boolean save(Hackathon hackathon) throws SQLException {
        String sql = "INSERT INTO hackathon (titolo, sede, datainizio, datafine, maxiscritti, maxteamsize, nteamiscritti, aperturaregistrazioni, organizzatore, dataaperturaregistrazioni) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitolo());
            stmt.setString(2, hackathon.getSede());
            stmt.setDate(3, new java.sql.Date(hackathon.getDataInizio().getTime()));
            stmt.setDate(4, new java.sql.Date(hackathon.getDataFine().getTime()));
            stmt.setInt(5, hackathon.getMaxIscritti());
            stmt.setInt(6, hackathon.getMAX_TEAM_SIZE());
            stmt.setInt(7, 0);
            stmt.setBoolean(8, false);
            stmt.setString(9, hackathon.getOrganizzatore().USERNAME);
            stmt.setDate(10, null); // vedere come fare

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String titolo) throws SQLException {
        String sql = "DELETE FROM hackathon WHERE titolo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Hackathon findByKey(String titolo) throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE titolo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToHackathon(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Hackathon> findAll() throws SQLException {
        String sql = "SELECT * FROM hackathon";
        List<Hackathon> hackathons = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hackathons.add(mapRowToHackathon(rs));
            }
        }
        return hackathons;
    }

    public List<Hackathon> getByOrganizer(String organizer) throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE organizzatore = ?";
        List<Hackathon> hackathons = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, organizer);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hackathons.add(mapRowToHackathon(rs));
                }
            }
        }
        return hackathons;
    }

    public List<Hackathon> getOpenRegistrations() throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE aperturaregistrazioni = true";
        List<Hackathon> hackathons = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hackathons.add(mapRowToHackathon(rs));
            }
        }
        return hackathons;
    }

    public List<Hackathon> getFinishedHackathons() throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE datafine < CURRENT_DATE";
        List<Hackathon> hackathons = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hackathons.add(mapRowToHackathon(rs));
            }
        }
        return hackathons;
    }

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

    public boolean openRegistrations(String titolo) throws SQLException {
        String sql = "UPDATE hackathon SET aperturaregistrazioni = true, dataaperturaregistrazioni = CURRENT_DATE WHERE titolo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            return stmt.executeUpdate() > 0;
        }
    }

    private Hackathon mapRowToHackathon(ResultSet rs) throws SQLException {
        Hackathon h = new Hackathon(
                rs.getString("titolo"),
                rs.getString("sede"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                rs.getInt("maxiscritti"),
                rs.getInt("maxteamsize")
        );

        // extra attributes
        if (rs.getBoolean("aperturaregistrazioni")) {
            h.apriRegistrazioni();
        }
        for (int i = 0; i < rs.getInt("nteamiscritti"); i++) {
            h.addTeam(null); // per ora non hai i team caricati
        }

        return h;
    }
}