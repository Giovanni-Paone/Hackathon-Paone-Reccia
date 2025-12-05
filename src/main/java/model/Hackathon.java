/**
 * Rappresenta un Hackathon con i suoi dettagli principali,
 * come il titolo, la sede, l'organizzatore, le date, i limiti di iscrizione
 * e lo stato delle registrazioni.
 *
 * @author Paone Giovanni e Reccia Antonio
 * @version 1.0
 */
package model;

import java.time.LocalDate;
import java.util.Date;

public class Hackathon {
    private String titolo;
    private String sede;
    private String organizzatore;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int maxIscritti;
    private int maxTeamSize;
    private int nTeamIscritti;
    private int nPartecipantiIscritti;
    private boolean aperturaRegistrazioni;

    /**
     * Costruisce un oggetto Hackathon con i parametri essenziali.
     * I contatori di team e partecipanti iscritti sono inizializzati a zero.
     *
     * @param titolo Il titolo dell'Hackathon.
     * @param sede La sede in cui si svolge l'Hackathon.
     * @param organizzatore Il nome dell'utente che organizza l'Hackathon.
     * @param dataInizio La data di inizio dell'evento.
     * @param fine La data di fine dell'evento.
     * @param maxIscritti Il numero massimo totale di partecipanti che possono iscriversi.
     * @param maxTeamSize La dimensione massima di un singolo team.
     */

    public Hackathon(String titolo, String sede, String organizzatore, LocalDate dataInizio,
                     LocalDate fine, int maxIscritti, int maxTeamSize) {
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

    /**
     * Costruisce un oggetto Hackathon con tutti i dettagli, inclusi i dati
     * sulle iscrizioni attuali e lo stato delle registrazioni.
     *
     * @param titolo Il titolo dell'Hackathon.
     * @param sede La sede in cui si svolge l'Hackathon.
     * @param organizzatore Il nome dell'utente che organizza l'Hackathon.
     * @param dataInizio La data inizio dell'evento.
     * @param fine La data di fine dell'evento.
     * @param maxIscritti Il numero massimo totale di partecipanti che possono iscriversi.
     * @param maxTeamSize La dimensione massima di un singolo team.
     * @param nTeamIscritti Il numero di team attualmente iscritti.
     * @param nPartecipantiIscritti Il numero totale di partecipanti attualmente iscritti.
     * @param aperturaRegistrazioni Lo stato di apertura/chiusura delle registrazioni ({@code true} se aperte).
     */

    public Hackathon(String titolo, String sede, String organizzatore, LocalDate dataInizio,
                     LocalDate fine, int maxIscritti, int maxTeamSize, int nTeamIscritti, int nPartecipantiIscritti,
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

    /**
     * Restituisce il titolo dell'Hackathon.
     * @return Il titolo dell'Hackathon.
     */
    public String getTitolo() {return titolo;}

    /**
     * Modifica il titolo dell'Hackathon.
     * @param titolo Il nuovo titolo da assegnare.
     */
    public void cambiaTitolo(String titolo) {this.titolo = titolo;}

    /**
     * Restituisce la sede in cui si svolge l'Hackathon.
     * @return La sede dell'evento.
     */
    public String getSede() {return sede;}
    /**
     * Modifica la sede dell'Hackathon.
     * @param sede La nuova sede da assegnare.
     */
    public void cambiaSede(String sede) {this.sede = sede;}

    /**
     * Restituisce l'organizzatore dell'Hackathon.
     * @return L'organizzatore dell'evento.
     */
    public String getOrganizzatore() {return organizzatore;}

    /**
     * Restituisce la data e ora di inizio dell'Hackathon.
     * @return La data di inizio.
     */
    public LocalDate getDataInizio() {return dataInizio;}

    /**
     * Modifica la data e ora di inizio dell'Hackathon.
     * @param dataInizio La nuova data di inizio.
     */
    public void cambiaInizio(LocalDate dataInizio) {this.dataInizio = dataInizio;}

    /**
     * Restituisce la data e ora di fine dell'Hackathon.
     * @return La data di fine.
     */
    public LocalDate getDataFine() {return dataFine;}

    /**
     * Modifica la data e ora di fine dell'Hackathon.
     * @param fine La nuova data di fine.
     */
    public void cambiaFine(LocalDate fine) {this.dataFine = fine;}

    /**
     * Restituisce il numero massimo di partecipanti ammessi all'Hackathon.
     * @return Il limite massimo di iscritti.
     */
    public int getMaxIscritti() {return maxIscritti;}

    /**
     * Modifica il numero massimo di partecipanti ammessi all'Hackathon.
     * @param maxIscritti Il nuovo limite massimo di iscritti.
     */
    public void cambiaMaxIscritti(int maxIscritti) {this.maxIscritti = maxIscritti;}

    /**
     * Restituisce la dimensione massima consentita per un singolo team.
     * @return La dimensione massima del team.
     */
    public int getMaxTeamSize() {return maxTeamSize;}

    /**
     * Modifica la dimensione massima consentita per un singolo team.
     * @param maxTeamSize La nuova dimensione massima del team.
     */
    public void cambiaMaxTeamSize(int maxTeamSize) {this.maxTeamSize = maxTeamSize;}

    /**
     * Restituisce il numero di team attualmente iscritti all'Hackathon.
     * @return Il conteggio dei team iscritti.
     */
    public int getNTeamIscritti() {return this.nTeamIscritti;}

    /**
     * Restituisce il numero totale di partecipanti attualmente iscritti.
     * @return Il conteggio dei partecipanti iscritti.
     */
    public int getNPartecipantiIscritti() {return this.nPartecipantiIscritti;}

    /**
     * Restituisce lo stato attuale delle registrazioni.
     * @return {@code true} se le registrazioni sono aperte, {@code false} altrimenti.
     */
    public boolean getAperturaRegistrazioni() {return aperturaRegistrazioni;}

    /**
     * Imposta lo stato delle registrazioni su aperto ({@code true}),
     * consentendo nuove iscrizioni.
     */
    public void apriRegistrazioni() {this.aperturaRegistrazioni = true;}
}