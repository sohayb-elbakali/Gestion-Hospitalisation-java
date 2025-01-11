package gestionhospitalisation;

import gestionhospitalisation.gui.MainFrame;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Run the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set the Look and Feel to the system default
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    // Create and display the main frame
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);

                    // Center the frame on screen
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int x = (screenSize.width - mainFrame.getWidth()) / 2;
                    int y = (screenSize.height - mainFrame.getHeight()) / 2;
                    mainFrame.setLocation(x, y);

                    // Make the frame visible
                    mainFrame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Erreur lors du démarrage de l'application: " + e.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}