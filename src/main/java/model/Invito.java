package model;

public class Invito {
    public final String MITTENTE;
    private final boolean permesso;

    /**
     * Costruisce un oggetto Invito.
     *
     * @param mittente L'utente che invia l'invito.
     * @param permesso ** DA CONTROLLARE **
     */
    public Invito(String mittente, boolean permesso) {
        this.MITTENTE = mittente;
        this.permesso = permesso;
    }

    public boolean getPermesso() {return permesso;}
}
