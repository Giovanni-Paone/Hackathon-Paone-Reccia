package model;

import java.util.Date;

public class Invito {
    public Hackathon hackathon;
    public Date dataInvito;
    Team team;

    public Invito(Hackathon hackathon) {
        this.hackathon = hackathon;
        //aggiungere ad invito la data seguente
    }

    public Invito(Hackathon hackathon, Team team) {
        this.hackathon = hackathon;
        this.team = team;
        //aggiungere ad invito la data seguente
    }
}
