package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Rappresenta la schermata principale (Home) dell'applicazione per un utente
 * con un ruolo specifico
 * Questa schermata permette la navigazione tra Hackathon attuali e precedenti
 * e, a seconda del ruolo, l'accesso a funzionalità amministrative come la
 * ricerca di altri utenti.
 */
public class Home2 {
    private static JFrame frameOrganizzatoreHome;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JLabel hackathonLabel;
    private JPanel OrganizzatoreHomePanel;
    private JButton cercaButton;

    /**
     * Restituisce il pannello principale (content pane) della finestra Home.
     */
    public JPanel getOrganizzatoreHomePanel() {return OrganizzatoreHomePanel;}

    /**
     * Costruisce la schermata Home, inizializzando i componenti GUI e gli ascoltatori di eventi.
     * La visibilità del pulsante 'cercaButton' (presumibilmente per la ricerca utenti/funzionalità admin)
     * è gestita in base al ruolo dell'utente.
     *
     * @param utente L'utente attualmente loggato (di tipo {@link model.Utente}).
     */
    public Home2(Controller controller, Utente utente) {
        if(utente.getRuolo() == 0){
            cercaButton.setVisible(false);
        }

        // Listener per visualizzare gli Hackathon attuali
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(Home2.this, utente);
            }
        });

        // Listener per visualizzare gli Hackathon precedenti
        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Assume che VisualizzaHackathon.main apra una nuova finestra
                VisualizzaHackathon.main(controller, utente, null);
            }
        });

        // Listener per il pulsante 'Cerca'
        cercaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Assume che CercaUtenti.main apra la finestra di ricerca utenti
                CercaUtenti.main(controller, utente, null);
            }
        });
    }

    public static void main(Controller controller, Utente utente) {
        frameOrganizzatoreHome = new JFrame("Home");
        frameOrganizzatoreHome.setContentPane(new Home2(controller, utente).OrganizzatoreHomePanel);
        frameOrganizzatoreHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameOrganizzatoreHome.setPreferredSize(new Dimension(400, 250));
        frameOrganizzatoreHome.setResizable(false);
        frameOrganizzatoreHome.pack();
        frameOrganizzatoreHome.setLocationRelativeTo(null);
        frameOrganizzatoreHome.setVisible(true);
    }
}

