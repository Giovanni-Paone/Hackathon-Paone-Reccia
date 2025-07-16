package model;

//magari classe inutile che serve solo nella base di dati
//per ora rimuovo il campo Team
public class MembroTeam {
    public final Team TEAM;
    public final String USERNAME;

    public MembroTeam(Team team, String USERNAME) {
        this.TEAM = team;
        this.USERNAME = USERNAME;
    }

    public void invitaNelTeam(MembroTeam mittente, String messaggio) {

    }
}
