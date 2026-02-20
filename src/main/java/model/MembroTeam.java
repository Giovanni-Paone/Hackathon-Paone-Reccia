package model;

public class MembroTeam extends Utente {

    /** * Il team di cui l'utente è membro. */
    public final Team TEAM;

    /**
     * Costruisce un nuovo membro del team.
     * Inizializza l'utente con un livello di accesso predefinito (livello 3).
     *
     * @param USERNAME Il nome utente univoco del membro.
     * @param team     L'istanza del {@link Team} a cui il membro appartiene.
     */
    public MembroTeam(String USERNAME, Team team) {
        super(USERNAME, 3);
        this.TEAM = team;
    }

    public void invitaNelTeam(MembroTeam mittente, String messaggio) {

    }
}
