package model;

public class Organizzatore{
    public final String USERNAME;
    public final Hackathon HACKATHON;

    public Organizzatore(String NICKNAME, Hackathon HACKATHON) {
        this.USERNAME = NICKNAME;
        this.HACKATHON = HACKATHON;
    }

    public void invito(Utente NICKNAME) {}

    public void aperturaRegistrazione() {this.HACKATHON.apriRegistrazioni();}
}
