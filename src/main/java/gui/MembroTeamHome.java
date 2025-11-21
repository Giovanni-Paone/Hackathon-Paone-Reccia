package gui;

import controller.Controller;
import model.MembroTeam;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MembroTeamHome {
    private static JFrame frameMembroTeamHome;
    private JPanel membroTeamHomePanel;
    private JLabel hackathonLabel;
    private JButton attualeButton;
    private JButton precedentiButton;
    private JButton fileButton;
    private JButton invitaButton;
    private JLabel teamLabel;
    private JPanel membriPanelContainer;
    private JPanel filePanel;

    public MembroTeamHome(Controller controller, MembroTeam utente) {
        teamLabel.setText("Team: " + utente.TEAM.NOME_TEAM);

        // aggiungiamo le JLabel dei membri dentro il panel già presente nel designer
        membriPanelContainer.setLayout(new BoxLayout(membriPanelContainer, BoxLayout.Y_AXIS));
        for (String membro : utente.TEAM.partecipanti) {
            JLabel membroLabel = new JLabel(membro);
            membroLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            membriPanelContainer.add(membroLabel);
        }

        // aggiorniamo la GUI
        membriPanelContainer.revalidate();
        membriPanelContainer.repaint();

        precedentiButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VisualizzaHackathon.main(controller, utente, null);
            }
        });
        attualeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.guardaHackathon(MembroTeamHome.this, utente);
            }
        });
        invitaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CercaUtenti.main(controller, utente, null);
            }
        });
        fileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.visualizzaFile(utente.TEAM.NOME_TEAM, null);
            }
        });


        filePanel.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    // accetto la drop come copia
                    evt.acceptDrop(DnDConstants.ACTION_COPY);

                    // controlla che siano file
                    if (!evt.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        JOptionPane.showMessageDialog(membroTeamHomePanel,
                                "Qui puoi solo trascinare file dal filesystem.", "Formato non supportato",
                                JOptionPane.WARNING_MESSAGE);
                        evt.dropComplete(false);
                        return;
                    }

                    @SuppressWarnings("unchecked")
                    java.util.List<File> droppedFiles = (java.util.List<File>) evt.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    if (droppedFiles == null || droppedFiles.isEmpty()) {
                        evt.dropComplete(false);
                        return;
                    }

                    StringBuilder okFiles = new StringBuilder();
                    StringBuilder errFiles = new StringBuilder();

                    // iteriamo su tutti i file trascinati (multi-file support)
                    for (File file : droppedFiles) {
                        // semplice check sull'estensione: solo .txt (puoi adattare)
                        if (!file.getName().toLowerCase().endsWith(".txt")) {
                            errFiles.append(file.getName()).append(" → estensione non supportata\n");
                            continue;
                        }

                        String content;
                        try {
                            // legge il file come testo UTF-8 (Java 11+)
                            content = java.nio.file.Files.readString(file.toPath(), java.nio.charset.StandardCharsets.UTF_8);
                        } catch (java.io.IOException ioe) {
                            // errore di lettura: può essere MalformedInput se non è UTF-8
                            Throwable cause = ioe.getCause();
                            if (cause instanceof java.nio.charset.MalformedInputException || ioe instanceof java.nio.charset.MalformedInputException) {
                                errFiles.append(file.getName()).append(" → non è testo UTF-8 leggibile\n");
                            } else {
                                errFiles.append(file.getName()).append(" → errore lettura: ").append(ioe.getMessage()).append("\n");
                            }
                            continue;
                        }

                        // <<< QUI: salva content nel DB tramite DAO (in background preferibilmente)
                        boolean savedOk = controller.saveFile(utente.TEAM, file.getName(), content);
                        try {
                            // ESEMPIO (devi sostituire con la chiamata reale al tuo DAO):
                            // DAO_Team dao = new DAO_Team();
                            // savedOk = dao.saveFileText(hackathonTitle, teamName, file.getName(), content);
                            savedOk = true; // temporaneo per test
                        } catch (Exception dbEx) {
                            dbEx.printStackTrace();
                            errFiles.append(file.getName()).append(" → errore salvataggio DB\n");
                            continue;
                        }

                        if (savedOk) okFiles.append(file.getName()).append("\n");
                        else errFiles.append(file.getName()).append(" → salvataggio fallito\n");
                    }

                    // mostro risultato riassuntivo all'utente
                    StringBuilder msg = new StringBuilder();
                    if (okFiles.length() > 0) msg.append("Caricati:\n").append(okFiles).append("\n");
                    if (errFiles.length() > 0) msg.append("Errori:\n").append(errFiles);
                    JOptionPane.showMessageDialog(membroTeamHomePanel, msg.length() == 0 ? "Nessun file elaborato." : msg.toString());

                    evt.dropComplete(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(membroTeamHomePanel, "Errore durante il caricamento dei file.", "Errore", JOptionPane.ERROR_MESSAGE);
                    try { evt.dropComplete(false); } catch (Exception ignored) {}
                }
            }
        });

    }


    public static void main(Controller controller, MembroTeam utente) {
        frameMembroTeamHome = new JFrame("Hackathon");
        frameMembroTeamHome.setContentPane(new MembroTeamHome(controller, utente).membroTeamHomePanel);
        frameMembroTeamHome.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameMembroTeamHome.setPreferredSize(new Dimension(450, 400));
        frameMembroTeamHome.setResizable(false);
        frameMembroTeamHome.pack();
        frameMembroTeamHome.setLocationRelativeTo(null);
        frameMembroTeamHome.setVisible(true);
    }
}
