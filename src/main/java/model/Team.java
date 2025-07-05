package model;

import java.io.File;
import java.util.ArrayList;

public class Team {
    public final Hackathon HACKATHON;
    public final String NOME_TEAM;
    public final ArrayList<Partecipante> partecipanti = new ArrayList<Partecipante>();
    private ArrayList<File> progressi = new ArrayList<File>();
    private int voto;

    Team(String nomeTeam, Hackathon hackathon, Partecipante creatore) {
        this.NOME_TEAM = nomeTeam;
        this.HACKATHON = hackathon;
        this.partecipanti.add(creatore);
    }

    public void aggiornaProgressi(File fp) {}
}
