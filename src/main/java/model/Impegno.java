package model;

import java.util.Date;

public class Impegno {
    private Date dataInizio = new Date();
    private Date dataFine = new Date();
    private String ruolo = new String();
    private Hackathon hackathon;

    public Impegno(Date dataInizio, Date dataFine, String ruolo, Hackathon hackathon) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.ruolo = ruolo;
        this.hackathon = hackathon;
    }
    public Date getDataInizio() {return dataInizio;}
    public Date getDataFine() {return dataFine;}
    public String getRuolo() {return ruolo;}
}
