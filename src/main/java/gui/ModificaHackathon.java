package gui;

import controller.Controller;
import model.Hackathon;
import model.Organizzatore;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Rappresenta l'interfaccia grafica per la modifica di un Hackathon esistente.
 * Questa schermata carica i dati correnti di un evento e permette a un
 * {@link model.Organizzatore} di aggiornare i dettagli (titolo, sede, date e limiti),
 * delegando la persistenza delle modifiche al {@link controller.Controller}.
 */
public class ModificaHackathon {
    private static JFrame frameModificaHackathon;
    private JPanel modificaHackathonPanel;
    private JLabel titoloLabel;
    private JTextField titoloTextField;
    private JLabel sedeLabel;
    private JTextField sedeTextField;
    private JLabel dataInizioLabel;
    private JSpinner dataInizioSpinner;
    private JLabel dataFineLabel;
    private JSpinner dataFineSpinner;
    private JLabel limiteIscrittiLabel;
    private JSpinner limiteIscrittiSpinner;
    private JLabel limiteComponentiSquadreLabel;
    private JSpinner limiteComponentiSquadreSpinner;
    private JButton confermaButton;
    private JLabel modificaHackathonLabel;


    public JFrame getFrameModificaHackathon() {return frameModificaHackathon;}

    public JPanel getModificaHackathonPanel() {return modificaHackathonPanel;}

    public JTextField getTitoloTextField() {return titoloTextField; }

    public JTextField getSedeTextField() {return sedeTextField; }

    public JSpinner getDataInizioSpinner() {return dataInizioSpinner; }

    public JSpinner getDataFineSpinner() {return dataFineSpinner; }

    public JSpinner getLimiteIscrittiSpinner() { return limiteIscrittiSpinner; }

    public JSpinner getLimiteComponentiSquadreSpinner() { return limiteComponentiSquadreSpinner; }

    /**
     * Costruisce la schermata di Modifica Hackathon, inizializza i modelli dei {@link JSpinner}
     * per le date, precarica i dati dell'Hackathon e configura gli ascoltatori di eventi.
     *
     * @param controller L'oggetto {@link controller.Controller} che gestirà l'azione di modifica.
     * @param organizzatore L'oggetto {@link model.Organizzatore} che sta modificando l'Hackathon.
     * @param hackathon L'oggetto {@link model.Hackathon} da modificare.
     * @param giudici La lista corrente dei Giudici associati all'Hackathon.
     */
    public ModificaHackathon(Controller controller, Organizzatore organizzatore, model.Hackathon hackathon, ArrayList<Organizzatore> giudici) {
        dataInizioSpinner.setModel(new SpinnerDateModel());
        dataInizioSpinner.setEditor(new JSpinner.DateEditor(dataInizioSpinner, "dd/MM/yyyy"));

        dataFineSpinner.setModel(new SpinnerDateModel());
        dataFineSpinner.setEditor(new JSpinner.DateEditor(dataFineSpinner, "dd/MM/yyyy"));

        setHackathon(hackathon);

        titoloTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(2==2){ //nome inesistente
                    sedeTextField.requestFocus();
                }
            }
        });
        sedeTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { dataInizioSpinner.requestFocus();
            }
        });
        confermaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.modificaHackathon(gui.ModificaHackathon.this, organizzatore, hackathon, giudici);
            }
        });
    }

    /**
     * Popola i campi di input della GUI con i valori correnti dell'oggetto Hackathon.
     *
     * @param hackathon L'oggetto {@link model.Hackathon} da cui recuperare i dati.
     */
    private void setHackathon(model.Hackathon hackathon) {
        titoloTextField.setText(hackathon.getTitolo());
        sedeTextField.setText(hackathon.getSede());
        dataInizioSpinner.setValue(hackathon.getDataInizio());
        dataFineSpinner.setValue(hackathon.getDataFine());
        limiteComponentiSquadreSpinner.setValue(hackathon.getMaxTeamSize());
        limiteIscrittiSpinner.setValue(hackathon.getMaxIscritti());
    }

    public static void main(Controller controller, Organizzatore organizzatore, Hackathon hackathon, ArrayList<Organizzatore> giudici) {
        frameModificaHackathon = new JFrame("Creazione Hackathon");
        frameModificaHackathon.setContentPane(new gui.ModificaHackathon(controller, organizzatore, hackathon, giudici).getModificaHackathonPanel());
        frameModificaHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameModificaHackathon.setPreferredSize(new Dimension(400, 450));
        frameModificaHackathon.setResizable(false);
        frameModificaHackathon.pack();
        frameModificaHackathon.setLocationRelativeTo(null);
        frameModificaHackathon.setVisible(true);
    }

}
