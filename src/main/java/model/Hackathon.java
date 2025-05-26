package model;

import java.util.ArrayList;
import java.util.Date;
//.
public class Hackathon {
    public final String TITOLO;
    public final String SEDE;
    public final Date INIZIO;
    public final Date FINE;
    private int teamIscritti;
    public final int MAX_ISCRITTI;
    public final int MAX_TEAM_SIZE;
    private ArrayList<Team> Team = new ArrayList<Team>();
    private boolean aperturaRegistrazioni = false;

    public Hackathon(String TITOLO, String SEDE, Date INIZIO, Date FINE, int MAX_ISCRITTI, int MAX_TEAM_SIZE) {
        this.TITOLO = TITOLO;
        this.SEDE = SEDE;
        this.INIZIO = INIZIO;
        this.FINE = FINE;
        this.teamIscritti = 0;
        this.MAX_ISCRITTI = MAX_ISCRITTI;
        this.MAX_TEAM_SIZE = MAX_TEAM_SIZE;
    }

    public void addTeamIscritti() {this.teamIscritti++;}

    public Team getTeamIscritti(int indice) {return Team.get(indice);}

    public void addTeam(Team team) {this.Team.add(team);}

    public boolean getAperturaRegistrazioni() {return aperturaRegistrazioni;}

    public void apriRegistrazioni() {this.aperturaRegistrazioni = true;}

    public void rimuoviImpegni() {}
}
