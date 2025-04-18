package model;

import java.util.List;

public class Partecipante extends Utente {

    private List<String> inviti;



    public void riceviInvito(String messaggio) {
        inviti.add(messaggio);
    }

    public void invitaPartecipante(Partecipante destinatario, String messaggio) {
        destinatario.riceviInvito(messaggio);
    }
}
