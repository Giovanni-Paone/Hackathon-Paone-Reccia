package model;

import java.io.File;
import java.util.ArrayList;

public class Team {
    public final Hackathon HACKATHON;
    public String nome;
    public ArrayList<Partecipante> partecipanti = new ArrayList<Partecipante>();
    public ArrayList<File> progressi = new ArrayList<File>();
    public short voto;

    Team(String nome, Hackathon HACKATHON, Partecipante creatore) {
        this.nome = nome;
        this.HACKATHON = HACKATHON;
        this.partecipanti.add(creatore);
    }

    public void aggiornaProgressi(File fp) {}
}
