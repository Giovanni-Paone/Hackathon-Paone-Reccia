package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Utente.
 */
public class Utente {
    public final String NICKNAME;
    private String password;
    public ArrayList<Date> impegni = new ArrayList();

    /**
     * Instantiates a new Utente.
     */
    Utente(String nickname, String password) {
        this.NICKNAME = NICKNAME;
        this.password = password;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public String getNICKNAME() {return NICKNAME;}

    public void registrazione(Hackathon HACKATHON){} // diventa partecipante
}

