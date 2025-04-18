package model;

public class Organizzatore extends Utente {
    private final Hackathon HACKATHON;
    Organizzatore(String NICKNAME, String password,  Hackathon HACKATHON) {
        super(NICKNAME, password);
        this.HACKATHON = HACKATHON;
    }
    public void invito(Utente NICKNAME) {}
    public void aperturaRegistrazione(Registrazione registrazione) {}
}
