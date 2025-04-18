package model;

import java.util.List;

public class Partecipante extends Utente {
    Team team;

    Partecipante(Team team) {
        this.team = team;
    }

    public void invitaPartecipante(Partecipante destinatario) {}
}
