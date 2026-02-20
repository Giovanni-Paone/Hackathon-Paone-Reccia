package gui;

import controller.Controller;
import model.Invito;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Rappresenta la schermata principale (Home o Dashboard) dell'applicazione,
 * visualizzata dopo un login di successo.
 * Questa schermata permette all'utente di visualizzare e gestire gli inviti,
 * navigare tra gli Hackathon e, se l'utente è un Organizzatore,
 * di creare un nuovo team.
 */
public class Home {
    private static JFrame frameHome;
    private JPanel homePanel;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JScrollBar scrollBar1;
    private JLabel invitiLabel;
    private JLabel hackathonLabel;
    private JButton creaTeamButton;
    private JPanel InvitiPanel;
    private JTextField cercaTextField;
    private JButton cercaButton;


    public JFrame getFrameHome() {return frameHome;}

    public JPanel getHomePanel() {return homePanel;}

    public JTextField getCercaTextField() {return cercaTextField;}

    /**
     * Costruisce la schermata Home, inizializzando i componenti GUI e gli ascoltatori di eventi.
     *
     * @param utente L'utente attualmente loggato.
     * @param inviti La lista iniziale degli inviti ricevuti dall'utente.
     */
    public Home(Controller controller, Utente utente, ArrayList<Invito> inviti) {
        if (utente.getRuolo() != 2) {
            creaTeamButton.setVisible(false);
        }

        if (!inviti.isEmpty()) {
            aggiornaInvitiPanel(controller, utente, inviti);
        }

        // Listener per visualizzare gli Hackathon attuali
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(Home.this, utente);
            }
        });

        // Listener per visualizzare gli Hackathon precedenti
        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Assume che VisualizzaHackathon.main apra una nuova finestra
                VisualizzaHackathon.main(controller, utente, null);
            }
        });

        // Listener per il pulsante 'Crea Team' (visibile solo agli Organizzatori)
        creaTeamButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Assume che CreaTeam.main apra una nuova finestra
                CreaTeam.main(controller, (Utente) utente, Home.this);
            }
        });

        // Listener per la ricerca (tasto Invio o click sul pulsante Cerca)
        cercaTextField.addActionListener(e -> controller.aggiornaInviti(Home.this, utente));
        cercaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.aggiornaInviti(Home.this, utente);
            }
        });
    }

    /**
     * Aggiorna dinamicamente il pannello di visualizzazione degli inviti ricevuti.
     * Rimuove tutti i vecchi inviti e aggiunge i nuovi, creando un {@link JPanel}
     * con un'etichetta del mittente e due pulsanti (Accetta e Rifiuta) per ogni invito.
     *
     * @param utente L'utente che ha ricevuto gli inviti.
     * @param inviti La lista aggiornata degli {@link model.Invito} da visualizzare.
     */
    public void aggiornaInvitiPanel(Controller controller, Utente utente, ArrayList<Invito> inviti) {
        InvitiPanel.removeAll();
        InvitiPanel.setLayout(new BoxLayout(InvitiPanel, BoxLayout.Y_AXIS));

        for (Invito invito : inviti) {
            JPanel panelInvito = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel mittente = new JLabel(invito.MITTENTE);
            JButton rifiuta = new JButton("Rifiuta");
            JButton accetta = new JButton("Accetta");


            // Listener per Rifiuta
            rifiuta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    controller.rifiutaInvito(Home.this, utente, invito);
                }
            });
            // Listener per Accetta
            accetta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                   controller.accettaInvito(Home.this, utente, invito);
                }
            });

            panelInvito.add(mittente);
            panelInvito.add(rifiuta);
            panelInvito.add(accetta);

            InvitiPanel.add(panelInvito);
        }

        InvitiPanel.revalidate();
        InvitiPanel.repaint();
    }

    public static void main(Controller controller, Utente utente, ArrayList<Invito> inviti) {
        frameHome = new JFrame("Home");
        frameHome.setContentPane(new Home(controller, utente, inviti).homePanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.setPreferredSize(new Dimension(425, 300));
        frameHome.setResizable(false);
        frameHome.pack();
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);
    }
}

