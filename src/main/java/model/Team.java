package model;

import java.io.File;
import java.util.ArrayList;

public class Team {
    public final String NOME_TEAM;
    public final ArrayList<String> partecipanti = new ArrayList<>();
    private ArrayList<File> progressi = new ArrayList<File>();


    public Team(String nomeTeam) {
        this.NOME_TEAM = nomeTeam;
    }

    public Team(String nomeTeam, String creatoreP) {
        this.NOME_TEAM = nomeTeam;
        this.partecipanti.add(creatoreP);
    }

    public void addPartecipante(String partecipante){
        this.partecipanti.add(partecipante);
    }

    public void aggiornaProgressi(File fp) {}
}
