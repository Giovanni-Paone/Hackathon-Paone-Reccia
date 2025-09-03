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

public class CercaUtenti {
    private static JFrame frameCercaUtenti;
    private JPanel cercaUtentiPanel;
    private JTextField UsernametextField;
    private JScrollBar scrollBar1;
    private JButton cercaButton;
    private JPanel listaPanel;

    public JFrame getFrameCercaUtenti() {return frameCercaUtenti;}
    public JPanel getCercaUtenti() {return cercaUtentiPanel;}

    public CercaUtenti(Controller controller, UtenteBase utente, ArrayList<Utente> utenti) {
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

                if(hackathon.getOrganizzatore().equals(utente.USERNAME)) {
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

    private void invitaMTMouseListener(Controller controller, JButton invita, UtenteBase mittente, UtenteBase destinatario) {
        invita.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.invitaMT(CercaUtenti.this, (MembroTeam) mittente, destinatario);
            }
        });
    }

    private void invitaOMouseListener(Controller controller, JButton invita, UtenteBase mittente, UtenteBase destinatario) {
        invita.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.invitaO(CercaUtenti.this, (Organizzatore) mittente, destinatario);
            }
        });
    }

    private void rimuoviMouseListener(Controller controller, JButton rimuovi, UtenteBase utente) {
        rimuovi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.cancellaUtente(utente);
            }
        });
    }

    public static void main(Controller controller, UtenteBase utente, ArrayList<Utente> utenti) {
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
