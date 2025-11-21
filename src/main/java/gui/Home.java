package gui;

import controller.Controller;
import model.Invito;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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


    public Home(Controller controller, Utente utente, ArrayList<Invito> inviti) {
        if (utente.getRuolo() != 2) {
            creaTeamButton.setVisible(false);
        }

        if (!inviti.isEmpty()) {
            aggiornaInvitiPanel(controller, utente, inviti);
        }

        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(Home.this, utente);
            }
        });

        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VisualizzaHackathon.main(controller, utente, null);
            }
        });

        creaTeamButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreaTeam.main(controller, (Utente) utente, Home.this);
            }
        });

        cercaTextField.addActionListener(e -> controller.aggiornaInviti(Home.this, utente));
        cercaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.aggiornaInviti(Home.this, utente);
            }
        });
    }

    public void aggiornaInvitiPanel(Controller controller, Utente utente, ArrayList<Invito> inviti) {
        InvitiPanel.removeAll();
        InvitiPanel.setLayout(new BoxLayout(InvitiPanel, BoxLayout.Y_AXIS));

        for (Invito invito : inviti) {
            JPanel panelInvito = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel mittente = new JLabel(invito.MITTENTE);
            JButton rifiuta = new JButton("Rifiuta");
            JButton accetta = new JButton("Accetta");


            rifiuta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    controller.rifiutaInvito(Home.this, utente, invito);
                }
            });
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

