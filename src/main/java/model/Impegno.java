package model;

import java.util.Date;

public class Impegno {
    public final Hackathon HACKATHON;
    public final int RUOLO;

    public Impegno(Hackathon hackathon, int ruolo) {
        this.HACKATHON = hackathon;
        this.RUOLO = ruolo;
    }
}
