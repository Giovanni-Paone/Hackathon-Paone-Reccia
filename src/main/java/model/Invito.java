package model;

import java.util.Date;

public class Invito {
    public Utente giudice;
    public Hackathon hackathon;
    public Date dataInizio;
    public Date dataFine;
    public Date dataInvito;

    public Invito(Utente giudice, Hackathon hackathon, Date dataInizio, Date dataFine) {
        this.giudice = giudice;
        this.hackathon = hackathon;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        //aggiungere ad invito la data seguente
    }
}
