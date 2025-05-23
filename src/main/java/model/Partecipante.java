package model;

import javax.swing.*;
import java.util.List;

public class Partecipante extends Utente {
    Team team;

    Partecipante(String NICKNAME, String password, Team team) {
        super(NICKNAME, password);
        this.team = team;
    }

    public void invitaPartecipante(String nometeam, Partecipante destinatario) {} //crea il team se partecipante accettà
    public void invitaPartecipante(Team team, Partecipante destinatario) {} //invita ad un team esistente
}
