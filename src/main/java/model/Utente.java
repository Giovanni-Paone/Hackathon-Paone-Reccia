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

    public void aggiungiInvito(Invito Invito){inviti.add(Invito);}


    public Impegno getImpegno(int indice){return this.impegni.get(indice);}
    public Impegno getImpegnoLast(){return this.impegni.getLast();}

    public int addImpegno(Hackathon hackathon, int ruolo){
        if (this.impegni.isEmpty()){
            Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
            this.impegni.add(nuovoImpegno);
            return 0;
        }

        if (hackathon.FINE.before(this.impegni.getFirst().HACKATHON.INIZIO)){
            Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
            this.impegni.addFirst(nuovoImpegno);
            return 0;
        }

        for (int i=0; i<this.impegni.size()-1; i++) {
            if(hackathon.INIZIO.equals(this.impegni.get(i).HACKATHON.INIZIO)){
                if (hackathon.TITOLO.equals(this.impegni.get(i).HACKATHON.TITOLO)) {
                    //sei già giudice od organizzatore, vedilo e dillo
                    return 1; //o 1 o 2 o 3, controllare che sei
                }
                else return -1; //sei impegnato, fa qualcosa
            }

            if(hackathon.INIZIO.after(this.impegni.get(i).HACKATHON.FINE) &&
                    hackathon.FINE.before(this.impegni.get(i+1).HACKATHON.INIZIO)){
                Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
                this.impegni.add(i, nuovoImpegno);
                return 0;
            }
        }

        if(hackathon.INIZIO.equals(this.impegni.getLast().HACKATHON.INIZIO)){
            if (hackathon.TITOLO.equals(this.impegni.getLast().HACKATHON.TITOLO)) {
                //sei già giudice od organizzatore, vedilo e dillo
                return 1; //o 1 o 2 o 3, controllare che sei
            }
            else return -1; //sei impegnato, fa qualcosa
        }

        if(hackathon.INIZIO.after(this.impegni.getLast().HACKATHON.FINE)) {
            Impegno nuovoImpegno = new Impegno(hackathon, ruolo);
            this.impegni.add(nuovoImpegno);
        }
        return -1;
    }

    public void registrazione(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // diventa partecipante se è disponibile

    public void creaHackathon(Hackathon HACKATHON, Date nuovoDataInizio, Date nuovoDataFine){} // crea hackathon diventa organizzatore se è disponibile

    public void accettaInvito(int indice){
        int controllo = -1;
        if(this.inviti.get(indice) instanceof InvitoGiudice) {
            addImpegno(((InvitoGiudice) this.inviti.get(indice)).HACKATHON, 1);
            if(controllo == 0){
                //crea giudice
            }
        }
        else {
            controllo = addImpegno(((InvitoTeam) this.inviti.get(indice)).TEAM.HACKATHON, 2);
            if(controllo == 0){
                //crea partecipante con team
            }
            if(controllo == 3) {
                //sei gia partecipante del hackathon, controlla team e se disponibile aggiungilo
            }
        }
    }

    public void rifiutaInvito(int indice){
        this.inviti.remove(indice);
    }
}