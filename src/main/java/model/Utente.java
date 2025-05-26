package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * The type Utente.
 */
public class Utente {
    public final String USERNAME;
    private String password;
    private ArrayList<Invito> inviti = new ArrayList();
    private ArrayList<Impegno> impegni = new ArrayList();
    /**
     * Instantiates a new Utente.
     */
    Utente(String USERNAME, String password) {
        this.USERNAME = USERNAME;
        this.password = password;
    }

    /**
     * Gets login.
     *
     * @return the login
     */

    public void aggiungiInvito(Invito Invito){
        inviti.add(Invito);
    }
    public Invito returnInvito(int indice){return inviti.get(indice);}

    public void addImpegnoFirst(Hackathon hackathon, String ruolo, Date dataInizio, Date datFine){
        Impegno nuovoImpegno = new Impegno(dataInizio, datFine, ruolo, hackathon);
        this.impegni.addFirst(nuovoImpegno);
    }

    public void addImpegno(Hackathon hackathon, String ruolo, Date dataInizio, Date datFine){
        Impegno nuovoImpegno = new Impegno(dataInizio, datFine, ruolo, hackathon);
        this.impegni.add(nuovoImpegno);
    }

    public void addImpegno(int indice, Hackathon hackathon, String ruolo, Date dataInizio, Date datFine){
        Impegno nuovoImpegno = new Impegno(dataInizio, datFine, ruolo, hackathon);
        this.impegni.add(indice, nuovoImpegno);
    }

    public Impegno returnImpegno(){return this.impegni.getFirst();}

    public Impegno returnImpegno(int indice){return this.impegni.get(indice);}

    public void controllaDisponibilità(Hackathon hackathon, String ruolo, Date nuovoDataInizio, Date nuovoDatFine){
        if (this.impegni.isEmpty()){
            addImpegno(hackathon, ruolo, nuovoDataInizio, nuovoDatFine);
        }

        if (nuovoDatFine.before(this.impegni.getFirst().dataInizio)){
            addImpegnoFirst(hackathon, ruolo, nuovoDataInizio, nuovoDatFine);
        }

        for (int i=0; i<this.impegni.size()-1; i++) {
            if(nuovoDataInizio.equals(this.impegni.get(i).dataInizio) ||
                    nuovoDatFine.equals(this.impegni.get(i).dataFine)){
                if (Objects.equals(this.impegni.get(i).hackathon, hackathon)) {
                    //sei già giudice o organizzatore, vedere e dillo
                }
                else return; //sei impegnato, fa qualcosa
            }

            if(nuovoDataInizio.after(this.impegni.get(i).dataFine) &&
                    nuovoDatFine.before(this.impegni.get(i+1).dataInizio)){
                addImpegno(i, hackathon, ruolo, nuovoDataInizio, nuovoDatFine);
            }
        }

        if(nuovoDataInizio.equals(this.impegni.getLast().dataInizio) ||
                nuovoDatFine.equals(this.impegni.getLast().dataFine)){
            if (Objects.equals(this.impegni.getLast().hackathon, hackathon)) {
                //sei già giudice o organizzatore, vedere e dillo
            }
            else return; //sei impegnato, fa qualcosa
        }

        if(nuovoDataInizio.after(this.impegni.getLast().dataFine)) {
            addImpegno(hackathon, ruolo, nuovoDataInizio, nuovoDatFine);
        }
    }

    public void registrazione(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // diventa partecipante se è disponibile

    public void creaHackathon(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // crea hackathon diventa organizzatore se è disponibile

    public void accettaInvito(){}

    public void rifiutaInvito(){}
}

