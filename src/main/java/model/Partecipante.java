package model;

import javax.swing.*;
import java.util.List;

public class Partecipante extends Utente {
    public final Hackathon HACKATHON;

    Partecipante(String username, Hackathon hackathon) {
        super(username);
        this.HACKATHON = hackathon;
    }

    public void creaTeam(String nomeTeam) {}//cambia ruolo nella base di dati
}
