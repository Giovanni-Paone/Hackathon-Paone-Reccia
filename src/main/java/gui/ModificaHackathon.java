package gui;

import controller.Controller;

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


    public ModificaHackathon(Controller controller) {
        dataInizioSpinner.setModel(new SpinnerDateModel());
        dataInizioSpinner.setEditor(new JSpinner.DateEditor(dataInizioSpinner, "dd/MM/yyyy"));

        dataFineSpinner.setModel(new SpinnerDateModel());
        dataFineSpinner.setEditor(new JSpinner.DateEditor(dataFineSpinner, "dd/MM/yyyy"));

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
                controller.modificaHackathon(gui.ModificaHackathon.this);
            }
        });
    }

    public static void main(String[] args, Controller controller) {
        frameModificaHackathon = new JFrame("Creazione Hackathon");
        frameModificaHackathon.setContentPane(new gui.CreazioneHackathon(controller).getCreazioneHackathonPanel());
        frameModificaHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameModificaHackathon.setPreferredSize(new Dimension(400, 450));
        frameModificaHackathon.setResizable(false);
        frameModificaHackathon.pack();
        frameModificaHackathon.setLocationRelativeTo(null);
        frameModificaHackathon.setVisible(true);
    }

}
