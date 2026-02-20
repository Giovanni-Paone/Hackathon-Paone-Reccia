package gui;

import controller.Controller;
import database.dao.DAO_Hackathon;
import model.*;
import model.Hackathon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Rappresenta la schermata di ricerca utenti.
 * Questa interfaccia permette di cercare utenti per nickname e visualizza i risultati
 * in una lista dinamica. I pulsanti d'azione visualizzati accanto a ciascun utente
 * (es. Invita, Rimuovi) variano in base al ruolo dell'utente loggato
 * (es. Organizzatore o MembroTeam) e al contesto dell'Hackathon.
*/
public class CercaUtenti {
    private static JFrame frameCercaUtenti;
    private JPanel cercaUtentiPanel;
    private JTextField UsernametextField;
    private JScrollBar scrollBar1;
    private JButton cercaButton;
    private JPanel listaPanel;


    public JFrame getFrameCercaUtenti() {return frameCercaUtenti;}

    public JPanel getCercaUtenti() {return cercaUtentiPanel;}

    /**
     * Costruisce la schermata di ricerca utenti, impostando gli ascoltatori
     * per il campo di testo e il pulsante di ricerca, e popolando la lista
     * dei risultati iniziali (se forniti).
     *
     * @param controller L'oggetto {@link controller.Controller} che gestirà la logica di ricerca e azione.
     * @param utente L'utente attualmente loggato (mittente dell'azione di ricerca/invito/rimozione).
     * @param utenti La lista iniziale di {@link model.Utente} da visualizzare (risultati di una ricerca precedente o lista iniziale).
     */
    public CercaUtenti(Controller controller, Utente utente, ArrayList<Utente> utenti) {
        UsernametextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (utente.getRuolo() == -1) {
                    controller.cercaUtentiO(CercaUtenti.this, utente, UsernametextField.getText());
                } else {
                    controller.cercaUtentiU(CercaUtenti.this, utente, UsernametextField.getText());
                }
            }
        });

        cercaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (utente.getRuolo() == -1) {
                    controller.cercaUtentiO(CercaUtenti.this, utente, UsernametextField.getText().trim());
                } else {
                    controller.cercaUtentiU(CercaUtenti.this, utente, UsernametextField.getText().trim());
                }
            }
        });


        if (utenti != null) {
            listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
            if(utente.getRuolo() == 3) {

                for (Utente u : utenti) {
                    JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel label = new JLabel(u.USERNAME);

                    JButton invita = new JButton("Invita");
                    userPanel.add(label);
                    userPanel.add(invita);
                    listaPanel.add(userPanel);

                    CercaUtenti.this.invitaMTMouseListener(controller, invita, utente, u);
                }
            } else {
                Hackathon hackathon;
                try{
                DAO_Hackathon daoHackathon = new DAO_Hackathon();
                    hackathon = daoHackathon.findHackathonCorrente();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if(hackathon != null && hackathon.getOrganizzatore().equals(utente.USERNAME)) {
                    for (Utente u : utenti) {
                        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JLabel label = new JLabel(u.USERNAME);
                        userPanel.add(label);

                        listaPanel.add(userPanel);

                        JButton rimuovi = new JButton("Rimuovi");
                        userPanel.add(rimuovi);

                        CercaUtenti.this.rimuoviMouseListener(controller, rimuovi, u);

                        if(u.getRuolo() == 1) {
                            JButton invita = new JButton("Invita");
                            userPanel.add(invita);

                            CercaUtenti.this.invitaOMouseListener(controller, invita, utente, u);
                        }
                    }
                }
                else
                {
                    for (Utente u : utenti) {
                        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JLabel label = new JLabel(u.USERNAME);
                        userPanel.add(label);

                        listaPanel.add(userPanel);

                        JButton rimuovi = new JButton("Rimuovi");
                        userPanel.add(rimuovi);

                        CercaUtenti.this.rimuoviMouseListener(controller, rimuovi, u);
                    }
                }
            }

            listaPanel.revalidate();
            listaPanel.repaint();
        }
    }

    /**
     * Configura il listener per il pulsante "Invita" quando l'azione proviene da un {@link model.MembroTeam}.
     * Delega al controller la logica per l'invito di un utente a unirsi al proprio team.
     *
     * @param controller Il controller per eseguire la logica di business.
     * @param invita Il pulsante "Invita".
     * @param mittente L'utente che sta inviando l'invito (MembroTeam).
     * @param destinatario L'utente invitato.
     */
    private void invitaMTMouseListener(Controller controller, JButton invita, Utente mittente, Utente destinatario) {
        invita.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.invitaMT(CercaUtenti.this, (MembroTeam) mittente, destinatario);
            }
        });
    }

    /**
     * Configura il listener per il pulsante "Invita" quando l'azione proviene da un {@link model.Organizzatore}.
     * Delega al controller la logica per l'invito di un partecipante a diventare Giudice.
     *
     * @param controller Il controller per eseguire la logica di business.
     * @param invita Il pulsante "Invita".
     * @param mittente L'utente che sta inviando l'invito (Organizzatore).
     * @param destinatario L'utente invitato (Partecipante).
     */
    private void invitaOMouseListener(Controller controller, JButton invita, Utente mittente, Utente destinatario) {
        invita.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.invitaO(CercaUtenti.this, (Organizzatore) mittente, destinatario);
            }
        });
    }

    /**
     * Configura il listener per il pulsante "Rimuovi".
     * Delega al controller la logica per cancellare l'utente specificato dal sistema.
     *
     * @param controller Il controller per eseguire la logica di business.
     * @param rimuovi Il pulsante "Rimuovi".
     * @param utente L'utente da cancellare/rimuovere.
     */
    private void rimuoviMouseListener(Controller controller, JButton rimuovi, Utente utente) {
        rimuovi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.cancellaUtente(CercaUtenti.this, utente, UsernametextField.getText());
            }
        });
    }

    public static void main(Controller controller, Utente utente, ArrayList<Utente> utenti) {
        frameCercaUtenti = new JFrame("Utenti");
        frameCercaUtenti.setContentPane(new CercaUtenti(controller, utente, utenti).cercaUtentiPanel);
        frameCercaUtenti.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameCercaUtenti.setPreferredSize(new Dimension(450, 300));
        frameCercaUtenti.setResizable(true);
        frameCercaUtenti.pack();
        frameCercaUtenti.setLocationRelativeTo(null);
        frameCercaUtenti.setVisible(true);
    }
}
