package gestionhospitalisation.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainFrame extends JFrame   {

    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Bleu plus foncé
    private static final Color ACCENT_COLOR = new Color(66, 165, 245);     // Bleu clair
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Gris très clair
    private static final Color TEXT_COLOR = new Color(33, 33, 33);          // Presque noir
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;            // Texte blanc pour les boutons
    private static final Font WELCOME_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private JTabbedPane tabbedPane;
    private PatientPanel patientPanel;
    private ActeMedicalPanel acteMedicalPanel;
    private JLabel statusLabel;
    private JLabel dateLabel;
    private Timer timer;
    private JPanel dashboardPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private void setWindowIcon() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/hopital.png"));
            if (icon != null) {
                setIconImage(icon.getImage());
            } else {
                System.err.println("Icon not found at: images/hopital.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading icon: " + e.getMessage());
        }
    }


    public MainFrame() {
        setTitle("Gestion Hospitalisation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        initComponents();
        setupTimer();
        setLocationRelativeTo(null);
        setWindowIcon();

    }

    private void initComponents() {
        // Panel principal avec CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Création du dashboard
        createDashboard();

        // Création des autres panels
        createMainContent();

        // Ajout des panels au CardLayout
        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(createMainContent(), "MAIN_CONTENT");

        // Création du menu et de la barre de statut
        setJMenuBar(createStyledMenuBar());
        add(contentPanel, BorderLayout.CENTER);
        add(createStyledStatusBar(), BorderLayout.SOUTH);

        // Afficher le dashboard en premier
        cardLayout.show(contentPanel, "DASHBOARD");
    }

    private void createDashboard() {
        dashboardPanel = new JPanel();
        dashboardPanel.setBackground(BACKGROUND_COLOR);
        dashboardPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Message de bienvenue
        JLabel welcomeLabel = new JLabel("Bienvenue dans le Système de Gestion Hospitalière");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 50, 0);
        dashboardPanel.add(welcomeLabel, gbc);

        // Bouton Gestion des Patients
        JButton patientButton = createStyledButton("Gestion des Patients", "patient.png");
        patientButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
            cardLayout.show(contentPanel, "MAIN_CONTENT");
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        dashboardPanel.add(patientButton, gbc);

        // Bouton Gestion des Actes Médicaux
        JButton acteButton = createStyledButton("Gestion des Actes Médicaux", "actemedical.png");
        acteButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
            cardLayout.show(contentPanel, "MAIN_CONTENT");
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        dashboardPanel.add(acteButton, gbc);
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(320, 150));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + iconPath));
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Icône non trouvée: " + iconPath);
        }

        return button;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        tabbedPane = createStyledTabbedPane();
        patientPanel = new PatientPanel();
        acteMedicalPanel = new ActeMedicalPanel();

        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Actes Médicaux", acteMedicalPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Ajout du bouton retour
        JButton backButton = new JButton("Retour au Dashboard");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "DASHBOARD"));
        backButton.setFont(REGULAR_FONT);
        mainPanel.add(backButton, BorderLayout.NORTH);

        return mainPanel;
    }


    private JTabbedPane createStyledTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(TITLE_FONT);
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(TEXT_COLOR);

        // Style personnalisé pour les onglets
        UIManager.put("TabbedPane.selected", ACCENT_COLOR);
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
        UIManager.put("TabbedPane.shadow", new Color(0, 0, 0, 0));
        UIManager.put("TabbedPane.darkShadow", new Color(0, 0, 0, 0));
        UIManager.put("TabbedPane.light", new Color(0, 0, 0, 0));
        UIManager.put("TabbedPane.highlight", new Color(0, 0, 0, 0));

        return tabbedPane;
    }

    private JMenuBar createStyledMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(PRIMARY_COLOR);
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        // Menu Fichier
        JMenu fileMenu = createStyledMenu("Fichier", 'F');
        JMenuItem exitItem = createStyledMenuItem("Quitter", 'Q');
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitItem.addActionListener(e -> confirmExit());
        fileMenu.add(exitItem);

        // Menu Aide
        JMenu helpMenu = createStyledMenu("Aide", 'A');
        JMenuItem aboutItem = createStyledMenuItem("À propos", 'P');
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JMenu createStyledMenu(String text, char mnemonic) {
        JMenu menu = new JMenu(text);
        menu.setMnemonic(mnemonic);
        menu.setFont(REGULAR_FONT);
        menu.setForeground(Color.BLUE);
        return menu;
    }

    private JMenuItem createStyledMenuItem(String text, char mnemonic) {
        JMenuItem item = new JMenuItem(text, mnemonic);
        item.setFont(REGULAR_FONT);
        item.setBackground(Color.WHITE);
        return item;
    }

    private JPanel createStyledStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ACCENT_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        statusLabel = new JLabel("Système de Gestion Hospitalière");
        statusLabel.setFont(REGULAR_FONT);
        statusLabel.setForeground(TEXT_COLOR);

        dateLabel = new JLabel();
        dateLabel.setFont(REGULAR_FONT);
        dateLabel.setForeground(TEXT_COLOR);

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(dateLabel, BorderLayout.EAST);

        return statusBar;
    }

    private void setupTimer() {
        timer = new Timer(1000, e -> updateDateTime());
        timer.start();
    }

    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateLabel.setText(sdf.format(new Date()) + " | Utilisateur: sohayb-elbakali");
    }

    private void confirmExit() {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir quitter l'application?",
                "Confirmer la fermeture",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showAboutDialog() {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        JOptionPane.showMessageDialog(
                this,
                "Application de Gestion Hospitalisation\n" +
                        "Version 1.0\n" +
                        "© 2025 Tous droits réservés\n\n" +
                        "Développé par Sohayb El Bakali  \n" +
                        "Contact: sohaybelbakali@gmail.com",
                "À propos",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

