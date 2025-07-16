package model;

import java.util.ArrayList;
import java.util.Date;
//.
public class Hackathon {
    private String titolo;
    private String sede;
    private Date dataInizio;
    private Date dataFine;
    private int maxIscritti;
    private int MAX_TEAM_SIZE;
    private ArrayList<Team> teamIscritti = new ArrayList<Team>();
    private int nTeamIscritti;
    private boolean aperturaRegistrazioni = false;

    public Hackathon(String titolo, String sede, Date dataInizio, Date fine, int maxIscritti, int MAX_TEAM_SIZE) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = fine;
        this.maxIscritti = maxIscritti;
        this.MAX_TEAM_SIZE = MAX_TEAM_SIZE;
        this.nTeamIscritti = 0;
    }

    public String getTitolo() {return titolo;}
    public void cambiaTitolo(String titolo) {this.titolo = titolo;}

    public String getSede() {return sede;}
    public void cambiaSede(String sede) {this.sede = sede;}

    public Date getDataInizio() {return dataInizio;}
    public void cambiaInizio(Date dataInizio) {this.dataInizio = dataInizio;}

    public Date getDataFine() {return dataFine;}
    public void cambiaFine(Date fine) {this.dataFine = fine;}

    public int getMaxIscritti() {return maxIscritti;}
    public void cambiaMaxIscritti(int maxIscritti) {this.maxIscritti = maxIscritti;}

    public int getMAX_TEAM_SIZE() {return MAX_TEAM_SIZE;}
    public void cambiaMAX_TEAM_SIZE(int MAX_TEAM_SIZE) {this.MAX_TEAM_SIZE = MAX_TEAM_SIZE;}

    public Team getTeamIscritti(int indice) {return teamIscritti.get(indice);}
    public void addTeam(Team team) {
        this.teamIscritti.add(team);
        nTeamIscritti++;
    }

    public int returnNTeamIscritti() {return this.nTeamIscritti;}

    public boolean getAperturaRegistrazioni() {return aperturaRegistrazioni;}

    public void apriRegistrazioni() {this.aperturaRegistrazioni = true;}
}