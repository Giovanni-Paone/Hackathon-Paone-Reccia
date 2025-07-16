package model;

import java.io.File;
import java.util.ArrayList;

public class Team {
    public final String NOME_TEAM;
    public final ArrayList<MembroTeam> partecipanti = new ArrayList<MembroTeam>();
    private ArrayList<File> progressi = new ArrayList<File>();
    private int voto;

    Team(String nomeTeam, Partecipante creatoreP) {
        this.NOME_TEAM = nomeTeam;
        MembroTeam creatore = new MembroTeam(this, creatoreP.USERNAME );
        this.partecipanti.add(creatore);
    }

    public void addPartecipante(Partecipante partecipante){
        MembroTeam creatore = new MembroTeam(this, partecipante.USERNAME);
        this.partecipanti.add(creatore);
    }

    public void aggiornaProgressi(File fp) {}
}
