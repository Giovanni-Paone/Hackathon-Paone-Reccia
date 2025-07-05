package model;

import javax.swing.*;
import java.util.List;

public class Partecipante extends Utente {
    private Team TEAM;
    public final Hackathon HACKATHON;

    Partecipante(String NICKNAME, String password, Hackathon hackathon) {
        super(NICKNAME, password);
        this.HACKATHON = hackathon;
    }


    public Team getTeam() {return TEAM;}


    public void creaTeam(String nomeTeam) {
        if (this.TEAM == null) {
            this.TEAM = new Team(nomeTeam, this.HACKATHON, this);
        }
        else
        {
            //vedere che fare, sei già in un team
        }
    }

    public void invitaPartecipante(Team team, Partecipante destinatario) {} //invita a un team esistente
}
