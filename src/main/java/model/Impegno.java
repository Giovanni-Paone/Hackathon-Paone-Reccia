package model;

import java.util.Date;

public class Impegno {
    public Date dataInizio = new Date();
    public Date dataFine = new Date();
    public String ruolo = new String();
    public Hackathon hackathon;

    public Impegno(Date dataInizio, Date dataFine, String ruolo, Hackathon hackathon) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.ruolo = ruolo;
        this.hackathon = hackathon;
    }
}
