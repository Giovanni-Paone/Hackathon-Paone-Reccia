package model;

public class Organizzatore extends UtenteBase{
    private Hackathon HACKATHON;

    public Organizzatore(String NICKNAME) {super(NICKNAME, -1);}

    public void setHackathon(Hackathon HACKATHON) {
        this.HACKATHON = HACKATHON;
    }

    public Hackathon getHackathon() {return HACKATHON;}

    public void invito(Utente NICKNAME) {}

    public void aperturaRegistrazione() {this.HACKATHON.apriRegistrazioni();}
}
