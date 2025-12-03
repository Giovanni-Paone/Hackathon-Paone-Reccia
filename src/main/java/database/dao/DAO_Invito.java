package database.dao;

import database.ConnessioneDatabase;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DAO_Invito {
    private final Connection connection;

    public DAO_Invito() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    public boolean save(Invito invito, String destinatario) throws SQLException {
        Invito oldInvito = this.getInvito(destinatario, invito.MITTENTE);
        if (oldInvito != null) {return false;}

        String sql = """
        INSERT INTO Invito (destinatario, mittente, datainvito, permesso)
        VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, destinatario);
            stmt.setString(2, invito.MITTENTE);
            stmt.setObject(3, LocalDateTime.now());
            stmt.setBoolean(4, invito.getPermesso());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(Utente utente, Invito invito) throws SQLException {
        String sql = " DELETE FROM Invito WHERE destinatario = ? AND mittente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utente.USERNAME);
            stmt.setString(2, invito.MITTENTE);

            return stmt.executeUpdate() > 0;
        }
    }

    public Invito getInvito(String destinatario, String mittente) throws SQLException {
        String sql = """
        SELECT destinatario, mittente, datainvito, permesso
        FROM invito
        WHERE destinatario = ? AND mittente = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, destinatario);
            stmt.setString(2, mittente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Invito(
                            rs.getString("mittente"),
                            rs.getBoolean("permesso")
                    );
                }
            }
        }

        return null;
    }

    public ArrayList<Invito> getInviti(Utente destinatario) throws SQLException {
        ArrayList<Invito> inviti = new ArrayList<>();

        String sql = """
        SELECT destinatario, mittente, datainvito, permesso FROM invito 
        WHERE destinatario = ? ORDER BY permesso DESC , datainvito DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, destinatario.USERNAME);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String mittente = rs.getString("mittente");
                    Invito invito = new Invito(
                            mittente,
                            rs.getBoolean("permesso")
                    );
                    inviti.add(invito);
                }
            }
        }

        return inviti;
    }

    public ArrayList<Invito> getInviti(String destinatario, String mittente) throws SQLException {
        ArrayList<Invito> inviti = new ArrayList<>();

        String sql = """
        SELECT destinatario, mittente, datainvito, permesso
        FROM invito
        WHERE destinatario = ? AND mittente LIKE ?
        ORDER BY permesso DESC, datainvito DESC
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, destinatario);
            stmt.setString(2, mittente + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mittente = rs.getString("mittente");
                    Invito invito = new Invito(
                            mittente,
                            rs.getBoolean("permesso")
                    );
                    inviti.add(invito);
                }
            }
        }

        return inviti;
    }

    public ArrayList<Invito> getInvitiMandati(Utente mittente) throws SQLException {
        ArrayList<Invito> inviti = new ArrayList<>();

        String sql = """
        SELECT destinatario, mittente, datainvito
        FROM invito
        WHERE mittente = ?
        ORDER BY datainvito DESC 
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mittente.USERNAME);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invito invito = new Invito(
                            mittente.USERNAME,
                            rs.getBoolean("permesso")
                    );
                    inviti.add(invito);
                }
            }
        }

        return inviti;
    }

    public Utente accetta(Utente destinatario, Invito invito) throws SQLException {
        DAO_Hackathon daoHackathon = new DAO_Hackathon();
        Hackathon hackathon = daoHackathon.findHackathonCorrente();
        Utente utente = null;

        if (invito.getPermesso()) {
            String sql = "UPDATE utente SET ruolo = 0 WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, destinatario.USERNAME);
                stmt.executeUpdate();
            }

            sql = "INSERT INTO giudice (username, hackathon) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, destinatario.USERNAME);
                stmt.setString(2, hackathon.getTitolo());
                stmt.executeUpdate();
            }

            utente = new Organizzatore(destinatario.USERNAME, 0);

        } else {
            if (hackathon.getNPartecipantiIscritti() >= hackathon.getMaxIscritti()) {return null;}

            int membriTeam = 0;
            Team team = new Team(invito.MITTENTE);

            String sql = "SELECT username FROM partecipante_hackathon WHERE hackathon = ? AND nometeam = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, hackathon.getTitolo());
                stmt.setString(2, invito.MITTENTE);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        team.partecipanti.add(rs.getString("username"));
                        membriTeam++;
                    }
                }
            }

            if (membriTeam >= hackathon.getMaxTeamSize()) {return null;}

            sql = "UPDATE utente SET ruolo = 3 WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, destinatario.USERNAME);
                stmt.executeUpdate();
            }

            sql = "INSERT INTO partecipante_hackathon (hackathon, username, nometeam) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, hackathon.getTitolo());
                stmt.setString(2, destinatario.USERNAME);
                stmt.setString(3, invito.MITTENTE);
                stmt.executeUpdate();
            }

            team.addPartecipante(destinatario.USERNAME);
            utente = new MembroTeam(destinatario.USERNAME, team);

            if (membriTeam + 1 >= hackathon.getMaxTeamSize()) {
                sql = "DELETE FROM invito WHERE mittente = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, invito.MITTENTE);
                    stmt.executeUpdate();
                }
            }
        }

        String sql = "DELETE FROM invito WHERE destinatario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, destinatario.USERNAME);
            stmt.executeUpdate();
        }

        return utente;
    }

}