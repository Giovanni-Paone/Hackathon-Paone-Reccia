package model;

public class Organizzatore{
    public final String USERNAME;
    private Hackathon HACKATHON;


    public Organizzatore(String NICKNAME) {
        this.USERNAME = NICKNAME;
    }

    public void setHackathon(Hackathon HACKATHON) {
        this.HACKATHON = HACKATHON;
    }

    public Hackathon getHackathon() {return HACKATHON;}

    public void invito(Utente NICKNAME) {}

    public void aperturaRegistrazione() {this.HACKATHON.apriRegistrazioni();}
}
