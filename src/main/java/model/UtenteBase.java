package model;

public class UtenteBase {
    public final String USERNAME;
    private int ruolo;

    public UtenteBase(String username, int ruolo) {
        this.USERNAME = username;
        this.ruolo = ruolo;
    }

    public int getRuolo() {return ruolo;}
}