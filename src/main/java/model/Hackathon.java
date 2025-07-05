package model;

import java.util.ArrayList;
import java.util.Date;
//.
public class Hackathon {
    public final String TITOLO;
    public final String SEDE;
    public final Date INIZIO;
    public final Date FINE;
    private int nTeamIscritti;
    public final int MAX_ISCRITTI;
    public final int MAX_TEAM_SIZE;
    private ArrayList<Team> teamIscritti = new ArrayList<Team>();
    private boolean aperturaRegistrazioni = false;

    public Hackathon(String TITOLO, String SEDE, Date INIZIO, Date FINE, int MAX_ISCRITTI, int MAX_TEAM_SIZE) {
        this.TITOLO = TITOLO;
        this.SEDE = SEDE;
        this.INIZIO = INIZIO;
        this.FINE = FINE;
        this.nTeamIscritti = 0;
        this.MAX_ISCRITTI = MAX_ISCRITTI;
        this.MAX_TEAM_SIZE = MAX_TEAM_SIZE;
    }

    public void addTeamIscritti() {this.nTeamIscritti++;}

    public Team getTeamIscritti(int indice) {return teamIscritti.get(indice);}

    public void addTeam(Team team) {this.teamIscritti.add(team);}

    public boolean getAperturaRegistrazioni() {return aperturaRegistrazioni;}

    public void apriRegistrazioni() {this.aperturaRegistrazioni = true;}

    public void rimuoviImpegni() {}
}
