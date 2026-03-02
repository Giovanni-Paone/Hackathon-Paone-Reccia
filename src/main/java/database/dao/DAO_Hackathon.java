package database.dao;

import database.ConnessioneDatabase;
import interfaceDAO.Interface_DAO_Hackathon;
import model.Hackathon;
import model.Organizzatore;
import model.Utente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DAO_Hackathon implements Interface_DAO_Hackathon {

    private final Connection connection;

    /**
     * Inizializza il DAO recuperando la connessione attiva dal singleton ConnessioneDatabase.
     * @throws SQLException Se si verifica un errore durante il recupero della connessione
     */
    public DAO_Hackathon() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    /**
     * Salva un nuovo hackathon nel database.
     * @param hackathon L'oggetto Hackathon contenente i dati da inserire.
     * @return true se l'inserimento avviene con successo, false se il titolo è già presente (violazione unique).
     * @throws SQLException Se si verifica un errore SQL imprevisto.
     */
    @Override
    public boolean save(Hackathon hackathon) throws SQLException {
        String sql = """
                INSERT INTO hackathon (titolo, sede, datainizio, datafine, maxiscritti, maxteamsize,
                                       organizzatore)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitolo());
            stmt.setString(2, hackathon.getSede());
            stmt.setDate(3, java.sql.Date.valueOf(hackathon.getDataInizio()));
            stmt.setDate(4, java.sql.Date.valueOf(hackathon.getDataFine()));
            stmt.setInt(5, hackathon.getMaxIscritti());
            stmt.setInt(6, hackathon.getMaxTeamSize());
            stmt.setString(7, hackathon.getOrganizzatore());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
        if ("23505".equals(e.getSQLState()))
            return false;
        else throw new SQLException("Errore durante il salvataggio del hackathon: " + e.getMessage(), e);
        }

    }

    /**
     * Aggiorna i dati di un hackathon esistente identificato dal titolo precedente.
     * @param oldtitolo Il titolo attuale dell'hackathon nel database (chiave di ricerca).
     * @param organizzatore L'organizzatore che effettua la modifica.
     * @param hackathon L'oggetto Hackathon con i nuovi valori aggiornati.
     * @return true se l'aggiornamento avviene con successo, false in caso di conflitto di nomi.
     * @throws SQLException In caso di errore durante l'esecuzione della query.
     */
    @Override
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
            stmt.setDate(3, java.sql.Date.valueOf(hackathon.getDataInizio()));
            stmt.setDate(4, java.sql.Date.valueOf(hackathon.getDataFine()));
            stmt.setInt(5, hackathon.getMaxIscritti());
            stmt.setInt(6, hackathon.getMaxTeamSize());
            stmt.setString(7, oldtitolo);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState()))
                return false;
            else throw new SQLException("Errore durante il salvataggio del hackathon: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera l'hackathon corrente, ovvero quello la cui data di fine è successiva alla data attuale.
     * * @return L'oggetto Hackathon trovato, oppure null se non ci sono eventi attivi.
     * @throws SQLException Se si verifica un errore nella query.
     */
    @Override
    public Hackathon findHackathonCorrente() throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE datafine > CURRENT_DATE";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return creaHackathon(rs);
                }
            }
        }
        return null;
    }

    /**
     * Recupera una lista di hackathon filtrati per titolo e/o organizzatore.
     * I risultati sono ordinati per data di fine decrescente.
     * @param hackathon Filtro per il titolo dell'evento.
     * @param organizzatore Filtro per lo username dell'organizzatore.
     * @return ArrayList di oggetti Hackathon corrispondenti ai filtri.
     * @throws SQLException Se la query fallisce.
     */
    @Override
    public ArrayList<Hackathon> getHackathons(String hackathon, String organizzatore) throws SQLException {
        String sql = "SELECT * FROM hackathon WHERE titolo LIKE ? AND organizzatore LIKE ? ORDER BY datafine DESC";
        ArrayList<Hackathon> hackathons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon + "%");
            stmt.setString(2, organizzatore + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hackathons.add(creaHackathon(rs));
                }
            }
        }
        return hackathons;
    }

    /**
     * Abilita le iscrizioni per un determinato hackathon e imposta la data di apertura a oggi.
     * @param titolo Il titolo dell'hackathon da attivare.
     * @return true se l'operazione è andata a buon fine, false altrimenti.
     * @throws SQLException Se si verifica un errore durante l'aggiornamento del record.
     */
    @Override
    public boolean aperturaRegistrazioni(String titolo) throws SQLException {
        String sql = "UPDATE hackathon SET aperturaregistrazioni = true, dataaperturaregistrazioni = CURRENT_DATE WHERE titolo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Metodo helper privato per mappare una riga del ResultSet in un oggetto Hackathon.
     * Include logica di business per chiudere automaticamente le registrazioni se la data d'inizio è passata
     * o per aggiornare lo stato di controllo se l'evento è concluso.
     * @param rs Il ResultSet derivante da una query sulla tabella hackathon.
     * @return Un oggetto Hackathon popolato con i dati del database.
     * @throws SQLException Se si verifica un errore nel recupero delle colonne.
     */
    @Override
    public Hackathon creaHackathon(ResultSet rs) throws SQLException {
        Hackathon hackathon = new Hackathon(
                rs.getString("titolo"),
                rs.getString("sede"),
                rs.getString("organizzatore"),
                rs.getDate("datainizio").toLocalDate(),
                rs.getDate("datafine").toLocalDate(),
                rs.getInt("maxiscritti"),
                rs.getInt("maxteamsize"),
                (rs.getInt("nteamiscritti"))-1,
                rs.getInt("utentiIscritti"),
                rs.getBoolean("aperturaregistrazioni")
        );

        if(hackathon.getAperturaRegistrazioni() && hackathon.getDataInizio().isBefore(LocalDate.now())) {
            String sql = "UPDATE Hackathon SET aperturaRegistrazioni = FALSE WHERE titolo = ?;";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, hackathon.getTitolo());
                stmt.executeUpdate();
            }
        } else if (rs.getBoolean("controllo") && hackathon.getDataFine().isBefore(LocalDate.now())) {
            String sql = "UPDATE Hackathon SET controllo = FALSE WHERE titolo = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, hackathon.getTitolo());
                stmt.executeUpdate();
            }
        }

        return hackathon;
    }

    /**
     * Registra un utente come partecipante a un hackathon.
     * L'operazione aggiorna il ruolo dell'utente a 'Partecipante' (codice 2) e inserisce il legame nella tabella di join.
     * @param hackathon L'oggetto Hackathon a cui iscriversi.
     * @param utente L'utente che intende partecipare.
     * @return Una nuova istanza di Utente con il ruolo aggiornato.
     * @throws SQLException In caso di violazione di vincoli o errore di connessione.
     */
    @Override
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

    /**
     * Recupera l'elenco di tutti gli organizzatori che fungono da giudici per un determinato hackathon.
     * @param hackathon L'hackathon di riferimento.
     * @return ArrayList di oggetti Organizzatore (giudici), ordinati alfabeticamente per username.
     * @throws SQLException Se la query fallisce.
     */
    @Override
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

    /**
     * Rimuove un utente dalla lista dei partecipanti di un hackathon e ne cambia il ruolo in 'Squalificato' (codice 29).
     * @param username Lo username dell'utente da squalificare.
     * @param hackathon L'hackathon da cui l'utente deve essere rimosso.
     * @throws SQLException In caso di errore durante la cancellazione o l'aggiornamento del ruolo.
     */
    @Override
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

    /**
     * Salva o aggiorna la descrizione del problema/tema assegnato a un hackathon.
     * @param hackathon Il titolo dell'hackathon.
     * @param problema Il testo descrittivo del problema da risolvere.
     * @throws SQLException Se l'aggiornamento fallisce.
     */
    @Override
    public void salvaProblema(String hackathon, String problema) throws SQLException {
            String sql = "UPDATE hackathon SET problema = ? WHERE titolo = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, problema);
                stmt.setString(2, hackathon);

                stmt.executeUpdate();
            }
    }

    /**
     * Recupera la descrizione del problema associato a un determinato hackathon.
     * @param hackathon Il titolo dell'hackathon di cui si vuole conoscere il tema.
     * @return La stringa contenente il problema, oppure null se non è ancora stato definito o l'hackathon non esiste.
     * @throws SQLException In caso di errore nella lettura dal database.
     */
    @Override
    public String getProblema(String hackathon) throws SQLException {

        String sql = "SELECT problema FROM hackathon WHERE titolo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("problema");
                }
            }
        }
        return null;
    }

}