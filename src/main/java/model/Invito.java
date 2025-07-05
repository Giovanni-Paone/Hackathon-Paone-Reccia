package model;

import java.time.LocalDateTime;

public abstract class Invito {
    private final String MESSAGGIO;
    public final LocalDateTime DATA_INVITO;

    public Invito(String messaggio) {
        this.MESSAGGIO = messaggio;
        this.DATA_INVITO = LocalDateTime.now();
    }

}
