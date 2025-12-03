package model;

//magari classe inutile che serve solo nella base di dati
//per ora rimuovo il campo Team
public class MembroTeam extends Utente {
    public final Team TEAM;

    public MembroTeam(String USERNAME, Team team) {
        super(USERNAME, 3);
        this.TEAM = team;
    }

    public void invitaNelTeam(MembroTeam mittente, String messaggio) {

    }
}
