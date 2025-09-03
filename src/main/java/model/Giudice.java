package model;

import java.io.File;

public class Giudice extends UtenteBase{

    public Giudice(String USERNAME) {
        super(USERNAME, 0);
    }

    void pubblicaProblema(File fp) {}
    void esaminaECommenta() {}
    void assegnaVoti() {}
}