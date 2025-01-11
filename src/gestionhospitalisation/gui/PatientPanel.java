package gestionhospitalisation.gui;

import gestionhospitalisation.dao.PatientDAO;
import gestionhospitalisation.models.Patient;
import gestionhospitalisation.models.TypeMutuelle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientPanel extends JPanel implements IPanel  {
    private final PatientDAO patientDAO = new PatientDAO();
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField ncinField, nomPrenomField, adresseField, telField, searchFieldByCIN;
    private JComboBox<String> mutuelleCombo;
    private JComboBox<TypeMutuelle> typeMutuelleCombo;
    private JLabel dateTimeLabel;
    private Timer timer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private JButton btnAjouter, btnModifier, btnSupprimer, btnEffacer, searchButtonByCIN;

    public PatientPanel() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
        setupTimer();
        loadPatients();
    }

    private void setupTimer() {
        updateDateTime();
        timer = new Timer(1000, e -> updateDateTime());
        timer.start();
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        dateTimeLabel.setText(String.format("<html><b>Date et Heure:</b> %s | <b>Utilisateur:</b> sohayb-elbakali</html>", now.format(formatter)));
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(HEADER_COLOR);
        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateTimeLabel.setForeground(Color.BLACK);

        JPanel searchCINPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchCINPanel.setBackground(HEADER_COLOR);
        searchCINPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel searchLabelCIN = new JLabel("Rechercher par NCIN:");
        searchLabelCIN.setForeground(Color.WHITE);
        searchFieldByCIN = new JTextField(20);
        searchButtonByCIN = new JButton("OK");
        styleButton(searchButtonByCIN, SEARCH_BUTTON_COLOR);
        searchButtonByCIN.addActionListener(e -> searchByCIN());
        searchFieldByCIN.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) searchByCIN();
            }
        });
        searchCINPanel.add(searchLabelCIN); searchCINPanel.add(searchFieldByCIN); searchCINPanel.add(searchButtonByCIN);
        topPanel.add(searchCINPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Information Patient"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ncinField = new JTextField(30); // Increased column size
        nomPrenomField = new JTextField(30); // Increased column size
        adresseField = new JTextField(30); // Increased column size
        telField = new JTextField(30); // Increased column size

        mutuelleCombo = new JComboBox<>(new String[]{"OUI", "NON"});
        typeMutuelleCombo = new JComboBox<>(TypeMutuelle.values());
        typeMutuelleCombo.setEnabled(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("NCIN:"), gbc);
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nom et Prénom:"), gbc);
        gbc.gridy = 2;
        formPanel.add(new JLabel("Adresse:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(ncinField, gbc);
        gbc.gridy = 1;
        formPanel.add(nomPrenomField, gbc);
        gbc.gridy = 2;
        formPanel.add(adresseField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridy = 1;
        formPanel.add(new JLabel("Mutuelle:"), gbc);
        gbc.gridy = 2;
        formPanel.add(new JLabel("Type Mutuelle:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        formPanel.add(telField, gbc);
        gbc.gridy = 1;
        formPanel.add(mutuelleCombo, gbc);
        gbc.gridy = 2;
        formPanel.add(typeMutuelleCombo, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setBackground(btnPanel_Color);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        btnAjouter = new JButton("Ajouter"); btnModifier = new JButton("Modifier"); btnSupprimer = new JButton("Supprimer"); btnEffacer = new JButton("Effacer");
        styleButton(btnAjouter, ADD_BUTTON_COLOR); styleButton(btnModifier, EDIT_BUTTON_COLOR); styleButton(btnSupprimer, DELETE_BUTTON_COLOR); styleButton(btnEffacer, CLEAR_BUTTON_COLOR);
        Dimension buttonSize = new Dimension(120, 35);
        btnAjouter.setPreferredSize(buttonSize); btnModifier.setPreferredSize(buttonSize); btnSupprimer.setPreferredSize(buttonSize); btnEffacer.setPreferredSize(buttonSize);
        buttonsPanel.add(Box.createHorizontalStrut(10)); buttonsPanel.add(btnAjouter); buttonsPanel.add(Box.createHorizontalStrut(10));
        buttonsPanel.add(btnModifier); buttonsPanel.add(Box.createHorizontalStrut(10)); buttonsPanel.add(btnSupprimer);
        buttonsPanel.add(Box.createHorizontalStrut(10)); buttonsPanel.add(btnEffacer); buttonsPanel.add(Box.createHorizontalStrut(10));

        String[] columns = {"ID","NCIN","Nom et Prénom","Adresse","Téléphone","Mutuelle","Type Mutuelle","Date Création","Date Modification"};
        tableModel = new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } };
        patientTable = new JTable(tableModel);
        styleTable(patientTable);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH); mainPanel.add(formPanel, BorderLayout.CENTER); mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.NORTH); add(scrollPane, BorderLayout.CENTER);
        setupListeners();
    }

    private void setupListeners() {
        btnAjouter.addActionListener(e -> ajouterPatient());
        btnModifier.addActionListener(e -> modifierPatient());
        btnSupprimer.addActionListener(e -> supprimerPatient());
        btnEffacer.addActionListener(e -> clearForm());
        mutuelleCombo.addActionListener(e -> updateTypeMutuelleField());
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patientTable.getSelectedRow() != -1) loadPatientToForm();
        });
    }

    private void updateTypeMutuelleField() {
        boolean isMutuelle = "OUI".equals(mutuelleCombo.getSelectedItem());
        typeMutuelleCombo.setEnabled(isMutuelle);
        if (!isMutuelle) typeMutuelleCombo.setSelectedIndex(0);
    }

    private void loadPatients() {
        try {
            tableModel.setRowCount(0);
            for (Patient patient : patientDAO.listerTous()) {
                tableModel.addRow(new Object[]{
                        patient.getIdPatient(), patient.getNcin(), patient.getNomPrenomPatient(), patient.getAdressePatient(),
                        patient.getTelPatient(), patient.getMutuellePatient(), patient.getTypeMutuellePatient(),
                        patient.getDateCreation() != null ? patient.getDateCreation().format(formatter) : "",
                        patient.getDateModification() != null ? patient.getDateModification().format(formatter) : ""
                });
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des patients", e);
        }
    }

    private void ajouterPatient() {
        if (!validateForm()) return;
        try {
            Patient patient = new Patient();
            setPatientFields(patient);
            patientDAO.ajouter(patient);
            loadPatients(); clearForm(); showSuccess("Patient ajouté avec succès!");
        } catch (SQLException e) {
            showError("Erreur lors de l'ajout du patient", e);
        }
    }

    private void modifierPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) { showWarning("Veuillez sélectionner un patient à modifier."); return; }
        if (!validateForm()) return;
        try {
            Patient patient = new Patient();
            patient.setIdPatient((Integer) patientTable.getValueAt(selectedRow, 0));
            setPatientFields(patient);
            patientDAO.modifier(patient);
            loadPatients(); clearForm(); showSuccess("Patient modifié avec succès!");
        } catch (SQLException e) {
            showError("Erreur lors de la modification du patient", e);
        }
    }

    private void supprimerPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) { showWarning("Veuillez sélectionner un patient à supprimer."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,"Êtes-vous sûr de vouloir supprimer ce patient?","Confirmation de suppression",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Integer idPatient = (Integer) patientTable.getValueAt(selectedRow, 0);
                patientDAO.supprimer(idPatient);
                loadPatients(); clearForm(); showSuccess("Patient supprimé avec succès!");
            } catch (SQLException e) {
                if ("23503".equals(e.getSQLState())) {
                    showError("Erreur : Ce patient est lié à des actes médicaux et ne peut pas être supprimé.", e);
                } else {
                    showError("Erreur lors de la suppression du patient.", e);
                }
            }
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        if (ncinField.getText().trim().isEmpty()) errors.append("- Le NCIN est obligatoire\n");
        if (nomPrenomField.getText().trim().isEmpty()) errors.append("- Le nom et prénom sont obligatoires\n");
        if (adresseField.getText().trim().isEmpty()) errors.append("- L'adresse est obligatoire\n");
        if (telField.getText().trim().isEmpty()) errors.append("- Le numéro de téléphone est obligatoire\n");
        else if (!telField.getText().trim().matches("^[0-9]{10}$")) errors.append("- Le numéro de téléphone doit contenir 10 chiffres\n");
        if ("OUI".equals(mutuelleCombo.getSelectedItem()) && typeMutuelleCombo.getSelectedItem() == null)
            errors.append("- Le type de mutuelle est obligatoire quand la mutuelle est 'OUI'\n");
        if (errors.length() > 0) { showWarning("Erreurs :\n" + errors); return false; }
        return true;
    }

    private void setPatientFields(Patient patient) {
        patient.setNcin(ncinField.getText().trim());
        patient.setNomPrenomPatient(nomPrenomField.getText().trim());
        patient.setAdressePatient(adresseField.getText().trim());
        patient.setTelPatient(telField.getText().trim());
        patient.setMutuellePatient(mutuelleCombo.getSelectedItem().toString());
        if ("OUI".equals(mutuelleCombo.getSelectedItem())) {
            patient.setTypeMutuellePatient((TypeMutuelle) typeMutuelleCombo.getSelectedItem());
        } else patient.setTypeMutuellePatient(null);
    }

    private void loadPatientToForm() {
        int selectedRow = patientTable.getSelectedRow();
        ncinField.setText(patientTable.getValueAt(selectedRow, 1).toString());
        nomPrenomField.setText(patientTable.getValueAt(selectedRow, 2).toString());
        adresseField.setText(patientTable.getValueAt(selectedRow, 3).toString());
        telField.setText(patientTable.getValueAt(selectedRow, 4).toString());
        mutuelleCombo.setSelectedItem(patientTable.getValueAt(selectedRow, 5).toString());
        Object typeMutuelleObj = patientTable.getValueAt(selectedRow, 6);
        if (typeMutuelleObj != null) typeMutuelleCombo.setSelectedItem(TypeMutuelle.valueOf(typeMutuelleObj.toString()));
        else typeMutuelleCombo.setSelectedIndex(0);
        updateTypeMutuelleField();
    }

    private void searchByCIN() {
        String cin = searchFieldByCIN.getText().trim();
        if (cin.isEmpty()) { loadPatients(); return; }
        try {
            tableModel.setRowCount(0);
            List<Patient> patients = patientDAO.rechercherParCIN(cin);
            for (Patient p : patients) {
                tableModel.addRow(new Object[] {
                        p.getIdPatient(), p.getNcin(), p.getNomPrenomPatient(), p.getAdressePatient(), p.getTelPatient(),
                        p.getMutuellePatient(), p.getTypeMutuellePatient(),
                        p.getDateCreation() != null ? p.getDateCreation().format(formatter) : "",
                        p.getDateModification() != null ? p.getDateModification().format(formatter) : ""
                });
            }
            if (patients.isEmpty()) JOptionPane.showMessageDialog(this,"Aucun patient trouvé pour le CIN: " + cin,
                    "Recherche",JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            showError("Erreur lors de la recherche par CIN", e);
        }
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setGridColor(new Color(200, 240, 240));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(135, 206, 250));
        header.setForeground(new Color(60, 60, 60));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBorder(BorderFactory.createLineBorder(new Color(200, 240, 240)));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFoc, row, col);
                if (isSel) { c.setBackground(new Color(173, 216, 230)); c.setForeground(new Color(60,60,60)); }
                else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
                    c.setForeground(new Color(60, 60, 60));
                }
                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                }
                return c;
            }
        });
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(120);
        table.getColumnModel().getColumn(8).setPreferredWidth(120);
    }

    private void clearForm() {
        ncinField.setText(""); nomPrenomField.setText(""); adresseField.setText(""); telField.setText("");
        mutuelleCombo.setSelectedIndex(0); typeMutuelleCombo.setSelectedIndex(0);
        patientTable.clearSelection(); updateTypeMutuelleField();
    }


    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Avertissement", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, message + "\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}