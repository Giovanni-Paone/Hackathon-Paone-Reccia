package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreazioneHackathon {
    private static JFrame frameCreazioneHackathon;
    private JPanel creazioneHackathonPanel;
    private JFormattedTextField dataFineformattedTextField;
    private JFormattedTextField dataInizioFormattedTextField;
    private JTextField titoloTextField;
    private JTextField sedeTextField;
    private JSpinner limiteIscrittiSpinner;
    private JLabel limiteComponentiSquadreLabel;
    private JSpinner limiteComponentiSquadreSpinner;
    private JButton creaButton;
    private JLabel creaHackathonLabel;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel dataInizioLabel;
    private JLabel dataFineLabel;
    private JLabel limiteIscrittiLabel;

    public CreazioneHackathon() {

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
            public void actionPerformed(ActionEvent e) { dataInizioFormattedTextField.requestFocus();
            }
        });
        dataInizioFormattedTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(2==2) { //sei libero
                    dataFineformattedTextField.requestFocus();
                }
            }
        });
        dataFineformattedTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(2==2) { //sei libero
                    limiteIscrittiSpinner.requestFocus();
                }
            }
        });
        creaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int risposta = JOptionPane.showConfirmDialog(creazioneHackathonPanel,
                        "Sei sicuro dei dati inseriti?",
                        "Conferma",
                        JOptionPane.INFORMATION_MESSAGE);
                if(risposta==JOptionPane.OK_OPTION){
                    OrganizzatoreView.main(null);
                    frameCreazioneHackathon.dispose();
                }
            }
        });
    }

    public static void main(String[] args) {
        frameCreazioneHackathon = new JFrame("Creazione Hackathon");
        frameCreazioneHackathon.setContentPane(new CreazioneHackathon().creazioneHackathonPanel);
        frameCreazioneHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameCreazioneHackathon.setPreferredSize(new Dimension(400, 450));
        frameCreazioneHackathon.setResizable(false);
        frameCreazioneHackathon.pack();
        frameCreazioneHackathon.setLocationRelativeTo(null);
        frameCreazioneHackathon.setVisible(true);
    }

}
