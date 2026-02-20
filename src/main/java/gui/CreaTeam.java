package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Rappresenta l'interfaccia grafica per la creazione di un nuovo team.
 * Questa schermata richiede all'utente (un {@link model.Utente}) di inserire
 * il nome del team che intende creare e delega la logica di creazione al {@link controller.Controller}.
 */
public class CreaTeam {
    private static JFrame frameCreaTeam;
    private JPanel creaTeamPanel;
    private JLabel nomeTeamLabel;
    private JTextField nomeTeam;
    private JButton confermaButton;


    public JFrame getFrameCreaTeam() {return frameCreaTeam;}

    public JPanel getCreaTeamPanel() {return creaTeamPanel;}

    public JTextField getNomeTeam() {return nomeTeam;}

    /**
     * Costruisce la schermata di Creazione Team e inizializza gli ascoltatori di eventi.
     * Le azioni di creazione sono delegate al controller.
     *
     * @param controller L'oggetto {@link controller.Controller} che gestirà l'azione di creazione.
     * @param utente L'{@link model.Utente} che sta creando il team (e che ne diventerà il primo membro).
     * @param home La schermata {@link gui.Home} da aggiornare o chiudere dopo la creazione.
     */
    public CreaTeam(Controller controller, Utente utente, Home home) {
        nomeTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.creaTeam(CreaTeam.this, utente, home);
            }
        });
        confermaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.creaTeam(CreaTeam.this, utente, home);
            }
        });
    }

    public static void main(Controller controller, Utente utente, Home home) {
        frameCreaTeam = new JFrame("Creazione Team");
        frameCreaTeam.setContentPane(new CreaTeam(controller, utente, home).creaTeamPanel);
        frameCreaTeam.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameCreaTeam.setPreferredSize(new Dimension(300, 250));
        frameCreaTeam.setResizable(false);
        frameCreaTeam.pack();
        frameCreaTeam.setLocationRelativeTo(null);
        frameCreaTeam.setVisible(true);
    }
}
