package database.dao;

import database.ConnessioneDatabase;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) per la gestione della persistenza degli inviti nel database.
 * Gestisce le operazioni di creazione, eliminazione, ricerca e l'accettazione degli inviti,
 * con conseguente aggiornamento dei ruoli degli utenti (Organizzatore o Membro del Team).
 */
public class DAO_Invito {
    private final Connection connection;

    /**
     * Costruttore della classe. Inizializza la connessione al database tramite il pattern Singleton.
     * @throws SQLException Se si verifica un errore durante il recupero della connessione.
     */
    public DAO_Invito() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    /**
     * Salva un nuovo invito nel database. Impedisce la creazione di duplicati
     * se esiste già un invito tra lo stesso mittente e destinatario.
     * @param invito L'oggetto Invito contenente i dati del mittente e i permessi.
     * @param destinatario Lo username del destinatario dell'invito.
     * @return {@code true} se l'operazione ha successo, {@code false} se l'invito esiste già.
     * @throws SQLException Se si verifica un errore SQL durante l'inserimento.
     */
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

    /**
     * Elimina un invito specifico dal database.
     * @param utente L'utente destinatario dell'invito.
     * @param invito L'oggetto Invito da eliminare (identificato dal mittente).
     * @return {@code true} se l'invito è stato rimosso, {@code false} altrimenti.
     * @throws SQLException Se si verifica un errore nel database.
     */
    public boolean delete(Utente utente, Invito invito) throws SQLException {
        String sql = " DELETE FROM Invito WHERE destinatario = ? AND mittente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utente.USERNAME);
            stmt.setString(2, invito.MITTENTE);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Recupera un singolo invito data la coppia destinatario e mittente.
     * @param destinatario Username del destinatario.
     * @param mittente Username del mittente.
     * @return L'oggetto {@link Invito} se trovato, {@code null} altrimenti.
     * @throws SQLException Se si verifica un errore nella query.
     */
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

    /**
     * Recupera tutti gli inviti ricevuti da un determinato utente.
     * Gli inviti sono ordinati per tipo di permesso e data decrescente.
     * @param destinatario L'oggetto Utente di cui cercare gli inviti.
     * @return Una lista di inviti ricevuti.
     * @throws SQLException In caso di errori SQL.
     */
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

    /**
     * Cerca inviti ricevuti da un destinatario filtrando per una parte dello username del mittente.
     * @param destinatario Username del destinatario.
     * @param mittente Prefisso o parte dello username del mittente (usa operatore LIKE).
     * @return Lista di inviti che soddisfano il filtro.
     * @throws SQLException In caso di errori SQL.
     */
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

    /**
     * Recupera la lista degli inviti spediti da un determinato utente.
     * @param mittente L'utente che ha inviato gli inviti.
     * @return Lista di inviti mandati.
     * @throws SQLException In caso di errori SQL.
     */
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

    /**
     * Gestisce la logica di accettazione di un invito.
     * <p>
     * Se l'invito ha {@code permesso = true}, l'utente diventa un Giudice/Organizzatore.<br>
     * Se l'invito ha {@code permesso = false}, l'utente viene aggiunto a un Team come partecipante,
     * previa verifica della disponibilità di posti nell'Hackathon e nel Team.
     * </p>
     * Al termine, elimina tutti gli inviti pendenti per il destinatario e, se il team è pieno,
     * elimina tutti gli inviti mandati da quel team.
     * @param destinatario L'utente che accetta l'invito.
     * @param invito L'invito da accettare.
     * @return L'oggetto {@link Utente} aggiornato col nuovo ruolo, o {@code null} se l'operazione fallisce (es. hackathon pieno).
     * @throws SQLException Se si verifica un errore durante le molteplici operazioni sul database.
     */
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