package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VisualizzaHackathon {
    private static JFrame frameVisualizzaHackathon;
    private JPanel mainPanel;
    private JLabel titoloLabel;
    private JTextField titoloTextField;
    private JTextField organizzatoreTextField;
    private JLabel organizzatoreLabel;
    private JButton button1;
    private JScrollBar scrollBar1;
    private JPanel listPanel;

    public JFrame getFrameVisualizzaHackathon() {return frameVisualizzaHackathon;}

    VisualizzaHackathon(Controller controller, Utente utente, ArrayList<model.Hackathon> hackathons) {
        titoloTextField.addActionListener(e -> organizzatoreTextField.requestFocus());
        organizzatoreTextField.addActionListener(e ->
                controller.precedentiHackathon(VisualizzaHackathon.this, titoloTextField.getText(), organizzatoreTextField.getText().trim(), utente)
        );
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.precedentiHackathon(VisualizzaHackathon.this, titoloTextField.getText(), organizzatoreTextField.getText().trim(), utente);
            }
        });

        JPanel panelFiltro = new JPanel(new GridBagLayout());
        GridBagConstraints gbcFiltro = new GridBagConstraints();
        gbcFiltro.insets = new Insets(6, 8, 6, 8);
        gbcFiltro.fill = GridBagConstraints.HORIZONTAL;
        gbcFiltro.weightx = 0.4;

        // dai una dimensione preferita ai textfield così pack() non li schiaccia
        Dimension preferredFieldSize = new Dimension(200, 24);
        titoloTextField.setPreferredSize(preferredFieldSize);
        organizzatoreTextField.setPreferredSize(preferredFieldSize);

        // Colonna 0: titolo (label sopra campo)
        JPanel panelTitolo = new JPanel();
        panelTitolo.setLayout(new BoxLayout(panelTitolo, BoxLayout.Y_AXIS));
        panelTitolo.add(titoloLabel);
        panelTitolo.add(Box.createVerticalStrut(4));
        panelTitolo.add(titoloTextField);

        gbcFiltro.gridx = 0;
        gbcFiltro.gridy = 0;
        panelFiltro.add(panelTitolo, gbcFiltro);

        // Colonna 1: organizzatore (label sopra campo)
        JPanel panelOrganizzatore = new JPanel();
        panelOrganizzatore.setLayout(new BoxLayout(panelOrganizzatore, BoxLayout.Y_AXIS));
        panelOrganizzatore.add(organizzatoreLabel);
        panelOrganizzatore.add(Box.createVerticalStrut(4));
        panelOrganizzatore.add(organizzatoreTextField);

        gbcFiltro.gridx = 1;
        panelFiltro.add(panelOrganizzatore, gbcFiltro);

        // Colonna 2: pulsante di ricerca (centra verticalmente il bottone)
        JPanel panelRicerca = new JPanel();
        panelRicerca.setLayout(new BoxLayout(panelRicerca, BoxLayout.Y_AXIS));
        panelRicerca.add(Box.createVerticalGlue());
        panelRicerca.add(button1);
        panelRicerca.add(Box.createVerticalGlue());

        gbcFiltro.gridx = 2;
        gbcFiltro.weightx = 0.2;
        panelFiltro.add(panelRicerca, gbcFiltro);

        // --- AREA LISTA HACKATHON ---
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        if (hackathons != null) {
            for (model.Hackathon h : hackathons) {
                // riga con GridBagLayout per un controllo preciso su tre colonne
                JPanel rowPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbcRow = new GridBagConstraints();
                gbcRow.insets = new Insets(6, 8, 6, 8);
                gbcRow.fill = GridBagConstraints.HORIZONTAL;
                gbcRow.gridy = 0;

                // Colonna 0: titolo (allineato a sinistra)
                JLabel labelTitolo = new JLabel(h.getTitolo());
                labelTitolo.setHorizontalAlignment(SwingConstants.LEFT);
                gbcRow.gridx = 0;
                gbcRow.weightx = 0.5;
                gbcRow.anchor = GridBagConstraints.WEST;
                rowPanel.add(labelTitolo, gbcRow);

                // Colonna 1: organizzatore (centrato)
                JLabel labelOrganizzatore = new JLabel(h.getOrganizzatore());
                labelOrganizzatore.setHorizontalAlignment(SwingConstants.CENTER);
                gbcRow.gridx = 1;
                gbcRow.weightx = 0.3;
                gbcRow.anchor = GridBagConstraints.CENTER;
                rowPanel.add(labelOrganizzatore, gbcRow);

                // Colonna 2: date + bottone (destra, verticalmente centrati)
                JPanel panelDateButton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                JLabel labelDate = new JLabel(h.getDataInizio() + " - " + h.getDataFine());
                labelDate.setHorizontalAlignment(SwingConstants.RIGHT);
                JButton buttonVisualizza = new JButton("Visualizza");
                buttonVisualizza.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        controller.guardaHackathon(utente, h);
                    }
                });
                panelDateButton.add(labelDate);
                panelDateButton.add(buttonVisualizza);

                gbcRow.gridx = 2;
                gbcRow.weightx = 0.2;
                gbcRow.anchor = GridBagConstraints.EAST;
                rowPanel.add(panelDateButton, gbcRow);

                // assicurati che tutti i componenti della riga siano centrati verticalmente
                labelTitolo.setAlignmentY(Component.CENTER_ALIGNMENT);
                labelOrganizzatore.setAlignmentY(Component.CENTER_ALIGNMENT);
                panelDateButton.setAlignmentY(Component.CENTER_ALIGNMENT);

                // bordo e separatore
                rowPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                listPanel.add(rowPanel);
                listPanel.add(Box.createRigidArea(new Dimension(0, 4)));
                listPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }

        // ScrollPane per la lista
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Layout principale: filtro in alto e lista al centro
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panelFiltro, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Forza il ridisegno se stai modificando la GUI a runtime
        mainPanel.revalidate();
        mainPanel.repaint();
    }




    public static void main(Controller controller, Utente utente, ArrayList<model.Hackathon> hackathons) {
        frameVisualizzaHackathon = new JFrame("hackathons");
        frameVisualizzaHackathon.setContentPane(new VisualizzaHackathon(controller, utente, hackathons).mainPanel);
        frameVisualizzaHackathon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameVisualizzaHackathon.setPreferredSize(new Dimension(600, 300));
        frameVisualizzaHackathon.setResizable(true);
        frameVisualizzaHackathon.pack();
        frameVisualizzaHackathon.setLocationRelativeTo(null);
        frameVisualizzaHackathon.setVisible(true);
    }
}
