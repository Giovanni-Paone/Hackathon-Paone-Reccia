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

    public void addImpegno(Hackathon hackathon, String ruolo, Date nuovoDataInizio, Date nuovoDatFine){
        if (this.impegni.isEmpty()){
            Impegno nuovoImpegno = new Impegno(nuovoDataInizio, nuovoDatFine, ruolo, hackathon);
            this.impegni.add(nuovoImpegno);
            return;
        }

        if (nuovoDatFine.before(this.impegni.getFirst().dataInizio)){
            Impegno nuovoImpegno = new Impegno(nuovoDataInizio, nuovoDatFine, ruolo, hackathon);
            return;
        }

        for (int i=0; i<this.impegni.size()-1; i++) {
            if(nuovoDataInizio.equals(this.impegni.get(i).dataInizio) ||
                    nuovoDatFine.equals(this.impegni.get(i).dataFine)){
                if (Objects.equals(this.impegni.get(i).hackathon, hackathon)) {
                    //sei già giudice o organizzatore, vedere e dillo
                    return;
                }
                else return; //sei impegnato, fa qualcosa
            }

            if(nuovoDataInizio.after(this.impegni.get(i).dataFine) &&
                    nuovoDatFine.before(this.impegni.get(i+1).dataInizio)){
                Impegno nuovoImpegno = new Impegno(nuovoDataInizio, nuovoDatFine, ruolo, hackathon);
                this.impegni.add(i, nuovoImpegno);
                return;
            }
        }

        if(nuovoDataInizio.equals(this.impegni.getLast().dataInizio) ||
                nuovoDatFine.equals(this.impegni.getLast().dataFine)){
            if (Objects.equals(this.impegni.getLast().hackathon, hackathon)) {
                //sei già giudice o organizzatore, vedere e dillo
                return;
            }
            else return; //sei impegnato, fa qualcosa
        }

        if(nuovoDataInizio.after(this.impegni.getLast().dataFine)) {
            Impegno nuovoImpegno = new Impegno(nuovoDataInizio, nuovoDatFine, ruolo, hackathon);
            this.impegni.add(nuovoImpegno);
        }//se non è stato aggiiunto è perchè non è presente un arco di tempo in cui è disponibile
    }

    public void registrazione(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // diventa partecipante se è disponibile

    public void creaHackathon(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // crea hackathon diventa organizzatore se è disponibile

    public void accettaInvito(){}

    public void rifiutaInvito(){}
}