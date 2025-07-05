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
    public Utente(String USERNAME, String password) {
        this.USERNAME = USERNAME;
        this.password = password;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public Invito getInvito(int indice){return inviti.get(indice);}
    public Invito getInvitoLast(){return inviti.getLast();}

    public void aggiungiInvito(Invito Invito){
        inviti.add(Invito);
    }


    public Impegno getImpegno(int indice){return this.impegni.get(indice);}
    public Impegno getImpegnoLast(){return this.impegni.getLast();}

    public void addImpegno(Hackathon hackathon, int ruolo){
        if (this.impegni.isEmpty()){
            Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
            this.impegni.add(nuovoImpegno);
            return;
        }

        if (hackathon.FINE.before(this.impegni.getFirst().hackathon.INIZIO)){
            Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
            this.impegni.addFirst(nuovoImpegno);
            return;
        }

        for (int i=0; i<this.impegni.size()-1; i++) {
            if(hackathon.INIZIO.equals(this.impegni.get(i).hackathon.INIZIO)){
                if (hackathon.TITOLO.equals(this.impegni.get(i).hackathon.TITOLO)) {
                    //sei già giudice od organizzatore, vedilo e dillo
                    return;
                }
                else return; //sei impegnato, fa qualcosa
            }

            if(hackathon.INIZIO.after(this.impegni.get(i).hackathon.FINE) &&
                    hackathon.FINE.before(this.impegni.get(i+1).hackathon.INIZIO)){
                Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
                this.impegni.add(i, nuovoImpegno);
                return;
            }
        }

        if(hackathon.INIZIO.equals(this.impegni.getLast().hackathon.INIZIO)){
            if (hackathon.TITOLO.equals(this.impegni.getLast().hackathon.TITOLO)) {
                //sei già giudice od organizzatore, vedilo e dillo
                return;
            }
            else return; //sei impegnato, fa qualcosa
        }

        if(hackathon.INIZIO.after(this.impegni.getLast().hackathon.FINE)) {
            Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
            this.impegni.add(nuovoImpegno);
        }
        //se non è stato aggiunto è perchè non è presente un arco di tempo in cui è disponibile
    }

    public void registrazione(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // diventa partecipante se è disponibile

    public void creaHackathon(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // crea hackathon diventa organizzatore se è disponibile

    public void accettaInvito(){}

    public void rifiutaInvito(){}
}