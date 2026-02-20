package gui;

import controller.Controller;
import model.Organizzatore;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Rappresenta l'interfaccia grafica per la visualizzazione dei dettagli di un singolo Hackathon.
 */
public class Hackathon {
    private static JFrame frameHackathon;
    private JPanel hackathonPanel;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel organizzatoreLabel;
    private JLabel dataInizioLabel;
    private JLabel numeroTeamLabel;
    private JLabel postiRimanentiLabel;
    private JLabel dimensioneMassimaTeamLabel;
    private JProgressBar progressBar1;
    private JButton teamButton;
    private JScrollBar scrollBar1;
    private JButton partecipantiButton;
    private JButton precedentiButton;
    private JLabel titolo;
    private JLabel sede;
    private JLabel organizzatore;
    private JLabel dataInizio;
    private JLabel dataFine;
    private JLabel nTeam;
    private JLabel nMaxTeamSize;
    private JLabel postiRimanenti;
    private JButton apriIscrizioniButton;
    private JButton effettuaCambiamentiButton;
    private JButton partecipaButton;
    private JPanel giudiciPanel;

    public JFrame getFrameHackathon() {return frameHackathon;}

    public JPanel getHackathonPanel() {return hackathonPanel;}

    /**
     * Costruisce la schermata dell'Hackathon e inizializza i listener per le azioni dell'utente.
     *
     * @param controller Il controller per la gestione della logica di business.
     * @param hackathon  L'oggetto modello contenente i dati dell'evento da mostrare.
     * @param giudici    La lista degli organizzatori che fungono da giudici per l'evento.
     * @param utente     L'utente attualmente autenticato nel sistema.
     */
    public Hackathon(Controller controller, model.Hackathon hackathon, ArrayList<Organizzatore> giudici, Utente utente) {
        setHackathon(controller, hackathon, utente);
        aggiornaGiudiciPanel(giudici);

        teamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.visualizzaTeam(hackathon, utente);
            }
        });

        partecipantiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.visualizzaIscritti(hackathon, utente);
            }
        });

        effettuaCambiamentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                controller.modificaHackathon(Hackathon.this, (Organizzatore) utente, hackathon, giudici);
            }
        });

        apriIscrizioniButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.apriRegistrazioni(Hackathon.this, hackathon, giudici, (Organizzatore) utente);
            }
        });
        partecipaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.partecipa(Hackathon.this, (Utente) utente, hackathon);
            }
        });
    }

    /**
     * Popola i campi di testo e configura la visibilità dei pulsanti in base alle regole di business:
     *
     * @param controller Riferimento al controller per l'accesso alla data corrente.
     * @param hackathon  I dati dell'hackathon da renderizzare.
     * @param utente     L'utente attuale per il controllo dei permessi.
     */
    private void setHackathon(Controller controller, model.Hackathon hackathon, Utente utente) {
        titolo.setText(hackathon.getTitolo());
        sede.setText(hackathon.getSede());
        organizzatore.setText(hackathon.getOrganizzatore());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dataInizio.setText(hackathon.getDataInizio().format(formatter));
        dataFine.setText(hackathon.getDataFine().format(formatter));

        nTeam.setText(String.valueOf(hackathon.getNTeamIscritti()));
        nMaxTeamSize.setText(String.valueOf(hackathon.getMaxTeamSize()));
        postiRimanenti.setText(String.valueOf(hackathon.getMaxIscritti() - hackathon.getNPartecipantiIscritti()));
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(hackathon.getMaxIscritti());
        progressBar1.setValue(hackathon.getNPartecipantiIscritti());

        if (hackathon.getDataFine().isBefore(controller.oggi)) {
            apriIscrizioniButton.setVisible(false);
            effettuaCambiamentiButton.setVisible(false);
            partecipaButton.setVisible(false);
            partecipantiButton.setVisible(false);
        }
        else {
            if (!utente.USERNAME.equals(hackathon.getOrganizzatore()) ||
                    hackathon.getAperturaRegistrazioni()) {
                apriIscrizioniButton.setVisible(false);
                effettuaCambiamentiButton.setVisible(false);
            }

            if (!hackathon.getAperturaRegistrazioni()) {
                partecipantiButton.setVisible(false);
                partecipaButton.setVisible(false);
            } else if(utente.getRuolo() != 1)
                partecipaButton.setVisible(false);
        }
    }

    /**
     * Aggiorna  il pannelo dei giudici.
     *
     * @param giudici La lista di oggetti {@link Organizzatore} da visualizzare.
     */
    private void aggiornaGiudiciPanel(ArrayList<Organizzatore> giudici) {
        giudiciPanel.removeAll();
        giudiciPanel.setLayout(new BoxLayout(giudiciPanel, BoxLayout.Y_AXIS));

        if(giudici != null) {
            for (int i = 0; i < giudici.size(); i += 2) {
                JPanel riga = new JPanel(new BorderLayout());

                JLabel leftLabel = new JLabel(giudici.get(i).USERNAME);
                riga.add(leftLabel, BorderLayout.WEST);

                if (i + 1 < giudici.size()) {
                    JLabel rightLabel = new JLabel(giudici.get(i + 1).USERNAME);
                    riga.add(rightLabel, BorderLayout.EAST);
                }

                giudiciPanel.add(riga);
            }

            giudiciPanel.revalidate();
            giudiciPanel.repaint();
        }
    }



    public static void main(Controller controller, model.Hackathon hackathon, ArrayList<Organizzatore> giudici, Utente utente) {
        frameHackathon = new JFrame("Hackathon");
        frameHackathon.setContentPane(new Hackathon(controller, hackathon, giudici, utente).hackathonPanel);
        frameHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameHackathon.setPreferredSize(new Dimension(450, 400));
        frameHackathon.setResizable(false);
        frameHackathon.pack();
        frameHackathon.setLocationRelativeTo(null);
        frameHackathon.setVisible(true);
    }
}
