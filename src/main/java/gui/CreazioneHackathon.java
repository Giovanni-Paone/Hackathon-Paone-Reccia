package gui;

import controller.Controller;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreazioneHackathon {
    private static JFrame frameCreazioneHackathon;
    private JPanel creazioneHackathonPanel;
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
    private JButton creaButton;
    private JLabel creaHackathonLabel;

    public JFrame getFrameCreazioneHackathon() {return frameCreazioneHackathon;}
    public JPanel getCreazioneHackathonPanel() {return creazioneHackathonPanel;}
    public JTextField getTitoloTextField() {return titoloTextField; }
    public JTextField getSedeTextField() {return sedeTextField; }
    public JSpinner getDataInizioSpinner() {return dataInizioSpinner; }
    public JSpinner getDataFineSpinner() {return dataFineSpinner; }
    public JSpinner getLimiteIscrittiSpinner() { return limiteIscrittiSpinner; }
    public JSpinner getLimiteComponentiSquadreSpinner() { return limiteComponentiSquadreSpinner; }

    public CreazioneHackathon(Controller controller, Organizzatore organizzatore) {
            dataInizioSpinner.setModel(new SpinnerDateModel());
            dataInizioSpinner.setEditor(new JSpinner.DateEditor(dataInizioSpinner, "dd/MM/yyyy"));

            dataFineSpinner.setModel(new SpinnerDateModel());
            dataFineSpinner.setEditor(new JSpinner.DateEditor(dataFineSpinner, "dd/MM/yyyy"));

        titoloTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(2==2){ //nome inesistente //non ricordo cosa sia questo
                    sedeTextField.requestFocus();
                }
            }
        });
        sedeTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { dataInizioSpinner.requestFocus();
            }
        });
        creaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.creaHackathon(CreazioneHackathon.this, organizzatore );
            }
        });
    }

    public static void main(Controller controller, Organizzatore organizzatore) {
        frameCreazioneHackathon = new JFrame("Creazione Hackathon");
        frameCreazioneHackathon.setContentPane(new CreazioneHackathon(controller, organizzatore).creazioneHackathonPanel);
        frameCreazioneHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameCreazioneHackathon.setPreferredSize(new Dimension(400, 450));
        frameCreazioneHackathon.setResizable(false);
        frameCreazioneHackathon.pack();
        frameCreazioneHackathon.setLocationRelativeTo(null);
        frameCreazioneHackathon.setVisible(true);
    }

}
