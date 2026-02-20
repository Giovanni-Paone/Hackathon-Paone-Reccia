package model;

import java.util.ArrayList;

/**
 * Rappresenta un team partecipante all'Hackathon.
 * Gestisce l'elenco dei membri, il voto assegnato e
 * i giudici che hanno espresso una valutazione.
 */
public class Team {

    /** Il nome del team. */
    public final String NOME_TEAM;

    /** Elenco degli username dei partecipanti appartenenti al team. */
    public final ArrayList<String> partecipanti = new ArrayList<>();

    /** Il punteggio dato team durante la valutazione. */
    private int voto;

    /** * Elenco dei giudici che votano il team. */
    public final ArrayList<String> giudiciVotanti = new ArrayList<>();

    /**
     * Costruisce un team vuoto specificando solo il nome.
     *
     * @param nomeTeam Il nome da assegnare al team.
     */
    public Team(String nomeTeam) {
        this.NOME_TEAM = nomeTeam;
    }

    /**
     * Costruisce un team e aggiunge immediatamente il suo creatore.
     *
     * @param nomeTeam  Il nome da assegnare al team.
     * @param creatoreP L'username dell'utente che ha fondato il team.
     */
    public Team(String nomeTeam, String creatoreP) {
        this.NOME_TEAM = nomeTeam;
        this.partecipanti.add(creatoreP);
    }

    /**
     * Costruisce un team completo di creatore e voto.
     *
     * @param nomeTeam  Il nome da assegnare al team.
     * @param creatoreP L'username dell'utente che ha fondato il team.
     * @param voto      Il punteggio del team.
     */
    public Team(String nomeTeam, String creatoreP, int voto) {
        this.NOME_TEAM = nomeTeam;
        this.partecipanti.add(creatoreP);
        this.voto = voto;
    }

    /**
     * Aggiunge un nuovo partecipante alla lista dei membri del team.
     *
     * @param partecipante L'username del partecipante da aggiungere.
     */
    public void addPartecipante(String partecipante){
        this.partecipanti.add(partecipante);
    }

    /**
     * Restituisce il voto attuale assegnato al team.
     *
     * @return Il punteggio del team.
     */
    public int getVoto() {return voto;}
}
