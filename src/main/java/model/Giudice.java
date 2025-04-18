package model;

import java.io.File;

public class Giudice extends Utente {
    public final Hackathon HACKATHON;

    Giudice(String NICKNAME, String password, Hackathon HACKATHON) {
        super(NICKNAME, password);
        this.HACKATHON = HACKATHON;
    }

    void pubblicaProblema(File fp) {}
    void esaminaECommenta() {}
    void assegnaVoti() {}
}