package gui;

import controller.Controller;
import model.Hackathon;
import model.Organizzatore;
import model.UtenteBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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


    public ModificaHackathon(Controller controller, Organizzatore organizzatore, model.Hackathon hackathon) {
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
                controller.modificaHackathon(gui.ModificaHackathon.this, organizzatore, hackathon);
            }
        });
    }

    private void setHackathon(model.Hackathon hackathon) {
        titoloTextField.setText(hackathon.getTitolo());
        sedeTextField.setText(hackathon.getSede());
        dataInizioSpinner.setValue(hackathon.getDataInizio());
        dataFineSpinner.setValue(hackathon.getDataFine());
        limiteComponentiSquadreSpinner.setValue(hackathon.getMaxTeamSize());
        limiteIscrittiSpinner.setValue(hackathon.getMaxIscritti());
    }

    public static void main(Controller controller, Organizzatore organizzatore, Hackathon hackathon) {
        frameModificaHackathon = new JFrame("Creazione Hackathon");
        frameModificaHackathon.setContentPane(new gui.ModificaHackathon(controller, organizzatore, hackathon).getModificaHackathonPanel());
        frameModificaHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameModificaHackathon.setPreferredSize(new Dimension(400, 450));
        frameModificaHackathon.setResizable(false);
        frameModificaHackathon.pack();
        frameModificaHackathon.setLocationRelativeTo(null);
        frameModificaHackathon.setVisible(true);
    }

}
