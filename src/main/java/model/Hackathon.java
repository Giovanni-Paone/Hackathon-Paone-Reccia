package model;

import java.util.ArrayList;
import java.util.Date;
//.
public class Hackathon {
    private String titolo;
    private String sede;
    private String organizzatore;
    private Date dataInizio;
    private Date dataFine;
    private int maxIscritti;
    private int maxTeamSize;
    private int nTeamIscritti;
    private int nPartecipantiIscritti;
    private boolean aperturaRegistrazioni;

    public Hackathon(String titolo, String sede, String organizzatore, Date dataInizio,
                     Date fine, int maxIscritti, int maxTeamSize) {
        this.titolo = titolo;
        this.sede = sede;
        this.organizzatore = organizzatore;
        this.dataInizio = dataInizio;
        this.dataFine = fine;
        this.maxIscritti = maxIscritti;
        this.maxTeamSize = maxTeamSize;
        this.nTeamIscritti = 0;
        this.nPartecipantiIscritti = 0;
        this.aperturaRegistrazioni = false;
    }

    public Hackathon(String titolo, String sede, String organizzatore, Date dataInizio,
                     Date fine, int maxIscritti, int maxTeamSize, int nTeamIscritti, int nPartecipantiIscritti,
                     boolean aperturaRegistrazioni) {
        this.titolo = titolo;
        this.sede = sede;
        this.organizzatore = organizzatore;
        this.dataInizio = dataInizio;
        this.dataFine = fine;
        this.maxIscritti = maxIscritti;
        this.maxTeamSize = maxTeamSize;
        this.nTeamIscritti = nTeamIscritti;
        this.nPartecipantiIscritti = nPartecipantiIscritti;
        this.aperturaRegistrazioni = aperturaRegistrazioni;
    }

    public String getTitolo() {return titolo;}
    public void cambiaTitolo(String titolo) {this.titolo = titolo;}

    public String getSede() {return sede;}
    public void cambiaSede(String sede) {this.sede = sede;}

    public String getOrganizzatore() {return organizzatore;}

    public Date getDataInizio() {return dataInizio;}
    public void cambiaInizio(Date dataInizio) {this.dataInizio = dataInizio;}

    public Date getDataFine() {return dataFine;}
    public void cambiaFine(Date fine) {this.dataFine = fine;}

    public int getMaxIscritti() {return maxIscritti;}
    public void cambiaMaxIscritti(int maxIscritti) {this.maxIscritti = maxIscritti;}

    public int getMaxTeamSize() {return maxTeamSize;}
    public void cambiaMaxTeamSize(int maxIscritti) {this.maxTeamSize = maxTeamSize;}

    public int getNTeamIscritti() {return this.nTeamIscritti;}

    public int getNPartecipantiIscritti() {return this.nPartecipantiIscritti;}

    public boolean getAperturaRegistrazioni() {return aperturaRegistrazioni;}

    public void apriRegistrazioni() {this.aperturaRegistrazioni = true;}
}