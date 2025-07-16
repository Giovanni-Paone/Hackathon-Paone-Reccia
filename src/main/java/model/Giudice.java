package model;

import java.io.File;

public class Giudice {
    public final String USERNAME;
    public final Hackathon HACKATHON;
    //La password viene controllata nella base di dati
    //hackathon verrà aggiunto nella base di dati come partecipazione
    //vedere questa cosa o no essendo che c'è bisogno di utilizzare hackathon in giudice per invito

    public Giudice(String USERNAME, Hackathon HACKATHON) {
        this.USERNAME = USERNAME;
        this.HACKATHON = HACKATHON;
    }

    void pubblicaProblema(File fp) {}
    void esaminaECommenta() {}
    void assegnaVoti() {}
}