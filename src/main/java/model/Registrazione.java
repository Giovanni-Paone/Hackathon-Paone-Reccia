package model;

import java.util.Date;

public class Registrazione {
    public final Date APERTURA_REGISTRAZIONI;
    public final Date CHIUSURA_REGISTRAZIONI;

    public Registrazione(Date APERTURA_REGISTRAZIONI, Date CHIUSURA_REGISTRAZIONI) {
        this.APERTURA_REGISTRAZIONI = APERTURA_REGISTRAZIONI;
        this.CHIUSURA_REGISTRAZIONI = CHIUSURA_REGISTRAZIONI;
    }
}
