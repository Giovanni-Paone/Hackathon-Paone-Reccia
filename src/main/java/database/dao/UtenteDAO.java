package database.dao;

import database.ConnessioneDatabase;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO implements DAO_Generico<Utente, String> {

    private Connection connection;

    public UtenteDAO() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public boolean save(Utente utente, String password) throws SQLException {
        String sql = "INSERT INTO UTENTE (Username, Password) VALUES (?, ?)"; //vedere che fare con ruolo

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utente.USERNAME);
            stmt.setString(2, password);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio dell'utente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String username) throws SQLException {
        String sql = "DELETE FROM UTENTE WHERE Username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'eliminazione dell'utente: " + e.getMessage(), e);
        }
    }

    @Override
    public Utente findByKey(String username) throws SQLException {
        String sql = "SELECT Username FROM UTENTE WHERE Username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(rs.getString("Username"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca dell'utente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Utente> findAll() throws SQLException {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT Username FROM UTENTE ORDER BY Username";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                Utente utente = new Utente(rs.getString("Username"));
                utenti.add(utente);
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero di tutti gli utenti: " + e.getMessage(), e);
        }

        return utenti;
    }

    public Utente findByUsername(String username) throws SQLException {
        // Questo metodo è identico a findByKey, ma lo mantengo per chiarezza dell'interfaccia
        return findByKey(username);
    }

    public Utente login(String username, String password) throws SQLException {
        String sql = "SELECT Username, Password FROM UTENTE WHERE Username = ? AND Password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(rs.getString("Username"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il login dell'utente: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Utente> findByHackathon(int hackathonId) throws SQLException {
        List<Utente> utenti = new ArrayList<>();
        String sql = """
            SELECT DISTINCT u.Username, u.Password 
            FROM UTENTE u 
            JOIN MEMBERSHIP m ON u.Username = m.Username_utente 
            WHERE m.Titolo_hackathon = ?
            ORDER BY u.Username
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(hackathonId));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Utente utente = new Utente(rs.getString("Username"));
                    utenti.add(utente);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca degli utenti per hackathon: " + e.getMessage(), e);
        }

        return utenti;
    }

}

