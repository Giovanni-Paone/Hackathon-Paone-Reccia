package model;

public class Invito {
    public final String MITTENTE;
    private final boolean permesso;

    public Invito(String mittente, boolean permesso) {
        this.MITTENTE = mittente;
        this.permesso = permesso;
    }

    public boolean getPermesso() {return permesso;}
}
