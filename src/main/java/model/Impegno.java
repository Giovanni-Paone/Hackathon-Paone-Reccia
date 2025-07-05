package model;

import java.util.Date;

public class Impegno {
    public Hackathon hackathon;
    public int ruolo;

    public Impegno(Hackathon hackathon, int ruolo) {
        this.hackathon = hackathon;
        this.ruolo = ruolo;
    }
}
