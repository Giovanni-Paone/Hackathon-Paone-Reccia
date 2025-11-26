package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VisualizzaFile {
    private static JFrame frameVisualizzaFile;
    private JPanel mainPanel;
    private JButton button1;
    private JButton button2;

    public VisualizzaFile(Controller controller, ArrayList<String> file, int cnt) {

        JTextArea textArea = new JTextArea(file.get(cnt));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Aggiungo al pannello principale (suppongo sia BorderLayout)
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Pulsante indietro
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cnt > 0) {
                    VisualizzaFile.frameVisualizzaFile.dispose();
                    VisualizzaFile.main(controller, file, cnt - 1);
                }
            }
        });

        // Pulsante avanti
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cnt < file.size() - 1) {
                    VisualizzaFile.frameVisualizzaFile.dispose();
                    VisualizzaFile.main(controller, file, cnt + 1);
                }
            }
        });
    }


    public static void main(Controller controller, ArrayList<String> file, int cnt) {
        if (file == null || file.isEmpty()) {
            // nessun file trovato
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
