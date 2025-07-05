package model;

public class InvitoTeam extends Invito{
    public final Team TEAM;

    public InvitoTeam(String messaggio, Team team) {
        super(messaggio);
        this.TEAM = team;
    }
}
