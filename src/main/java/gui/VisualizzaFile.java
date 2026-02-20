package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * La classe {@code VisualizzaFile} gestisce l'interfaccia grafica per la visualizzazione
 * sequenziale di contenuti testuali memorizzati in una lista.
 * <p>
 * Permette all'utente di scorrere tra i vari file (rappresentati come stringhe)
 * utilizzando pulsanti di navigazione "destra" e "sinistra".
*/
public class VisualizzaFile {
    private static JFrame frameVisualizzaFile;
    private JPanel mainPanel;
    private JButton destraButton;
    private JButton sinistraButton;

    /**
     * Costruisce una nuova istanza della schermata di visualizzazione.
     * Configura l'area di testo, carica il contenuto del file corrente e
     * gestisce la visibilità e la logica dei pulsanti di navigazione.
     *
     * @param controller Il controller per la gestione della logica applicativa.
     * @param files      La lista di stringhe contenente i dati dei file da visualizzare.
     * @param cnt        L'indice corrente del file da mostrare all'interno della lista.
     */
    public VisualizzaFile(Controller controller, ArrayList<String> files, int cnt) {

        JTextArea textArea = new JTextArea(files.get(cnt));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Configurazione del layout del pannello principale
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Nasconde il pulsante "indietro" se siamo sul primo file
        if (cnt == 0) {
            sinistraButton.setVisible(false);
        }

        // Nasconde il pulsante "avanti" se siamo sull'ultimo file
        if (files.size() == cnt) {
            destraButton.setVisible(false);
        }

        // Listener per la navigazione all'indietro
        sinistraButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cnt > 0) {
                    VisualizzaFile.frameVisualizzaFile.dispose();
                    VisualizzaFile.main(controller, files, cnt - 1);
                }
            }
        });

        // Listener per la navigazione in avanti
        destraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cnt < files.size() - 1) {
                    VisualizzaFile.frameVisualizzaFile.dispose();
                    VisualizzaFile.main(controller, files, cnt + 1);
                }
            }
        });
    }

    /**
     * Metodo statico per inizializzare e mostrare la finestra di visualizzazione.
     * Verifica se la lista dei file è vuota prima di procedere con la creazione del frame.
     *
     * @param controller Il controller per la gestione della logica.
     * @param file       La lista dei contenuti da visualizzare.
     * @param cnt        L'indice di partenza.
     */
    public static void main(Controller controller, ArrayList<String> file, int cnt) {
        if (file == null || file.isEmpty()) {
            // Nessun file trovato: mostra un messaggio di avviso
            JOptionPane.showMessageDialog(null, "File assenti", "File non trovati",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        frameVisualizzaFile = new JFrame("File");
        frameVisualizzaFile.setContentPane(new VisualizzaFile(controller, file, cnt).mainPanel);
        frameVisualizzaFile.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameVisualizzaFile.setPreferredSize(new Dimension(450, 300));
        frameVisualizzaFile.setResizable(true);
        frameVisualizzaFile.pack();
        frameVisualizzaFile.setLocationRelativeTo(null);
        frameVisualizzaFile.setVisible(true);
    }
}
