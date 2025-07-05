package model;

public class InvitoGiudice extends Invito{
    public final Hackathon HACKATHON;

    public InvitoGiudice(String messaggio, Hackathon hackathon) {
        super(messaggio);
        this.HACKATHON = hackathon;
    }
}
