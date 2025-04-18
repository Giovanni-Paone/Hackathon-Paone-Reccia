package model;

import java.util.ArrayList;
import java.util.Date;
//.
public class Hackathon {
    public final String TITOLO;
    public final String SEDE;
    public final Date INIZIO;
    public final Date FINE;
    public int iscritti;
    public final int MAX_ISCRITTI;
    public final int MAX_TEAM_SIZE;
    public ArrayList<Team> Team = new ArrayList<Team>();

    Hackathon(String TITOLO, String SEDE, Date INIZIO, Date FINE, int MAX_ISCRITTI, int MAX_TEAM_SIZE) {
        this.TITOLO = TITOLO;
        this.SEDE = SEDE;
        this.INIZIO = INIZIO;
        this.FINE = FINE;
        this.iscritti = 0;
        this.MAX_ISCRITTI = MAX_ISCRITTI;
        this.MAX_TEAM_SIZE = MAX_TEAM_SIZE;
    }
}
