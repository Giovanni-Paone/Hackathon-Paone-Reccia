package model;

import java.time.LocalDateTime;

public class InvitoGiudice {
    public final Organizzatore MITTENTE; //forse da non mettere essendo l'organizzatore uno e anche
    //l hackathon a cui partecipare
    private final String MESSAGGIO;
    public final LocalDateTime DATA_INVITO;

    public InvitoGiudice(Organizzatore mittente, String messaggio) {
        this.MITTENTE = mittente;
        this.MESSAGGIO = messaggio;
        this.DATA_INVITO = LocalDateTime.now();
    }
}
