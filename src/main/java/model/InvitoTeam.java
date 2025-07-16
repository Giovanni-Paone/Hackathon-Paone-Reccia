package model;

import java.time.LocalDateTime;

public class InvitoTeam {
    public final MembroTeam MITTENTE;
    private final String MESSAGGIO;
    public final LocalDateTime DATA_INVITO;

    public InvitoTeam(MembroTeam mittente, String messaggio) {
        this.MITTENTE = mittente;
        this.MESSAGGIO = messaggio;
        this.DATA_INVITO = LocalDateTime.now();
    }
}
