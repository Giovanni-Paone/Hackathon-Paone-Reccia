package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Utente.
 */
public class Utente {
    private final String NICKNAME;
    private String password;
    public ArrayList<Date> impegni = new ArrayList();

    /**
     * Instantiates a new Utente.
     *
     * @param NICKNAME    the NICKNAME
     * @param password the password
     */
    public Utente(String NICKNAME, String password) {
        this.NICKNAME = NICKNAME;
        this.password = password;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public String getNICKNAME() {
        return NICKNAME;
    }
    public void registrazione() // aggiungere in ()
}

