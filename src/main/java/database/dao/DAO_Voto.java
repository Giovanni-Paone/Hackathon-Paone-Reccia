package database.dao;
import database.ConnessioneDatabase;
import model.Team;

import java.sql.*;
import java.util.ArrayList;

public class DAO_Voto {

    private Connection connection;

    public DAO_Voto() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    public boolean save(String hackathon, String team, String giudice, int voto) throws SQLException {
        String sql = "INSERT INTO voto_team (hackathon, team, giudice, voto) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon);
            stmt.setString(2, team);
            stmt.setString(3, giudice);
            stmt.setInt(4, voto);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore DAO aggiungiVoto(): " + e.getMessage());
            return false;
        }
    }

    public void getVoti(ArrayList<Team> teams) throws SQLException {return;}

    public void getGiudiciVotanti(ArrayList<Team> teams) {return;}
}
