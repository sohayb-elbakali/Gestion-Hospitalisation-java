package gestionhospitalisation.gui;

import gestionhospitalisation.dao.ActeMedicalDAO;
import gestionhospitalisation.models.ActeMedical;
import gestionhospitalisation.models.TypeActeMedical;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;


public class ActeMedicalPanel extends JPanel implements IPanel{
    // DAO
    private final ActeMedicalDAO acteMedicalDAO = new ActeMedicalDAO();

    // Swing components
    private JTable acteTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JButton searchButton;

    private JTextField idDossierMedicalField, idActeMedicalField, idPatientField,
            dateActeMedicalField, medecinActeMedicalField;
    private JComboBox<TypeActeMedical> typeActeMedicalCombo;

    private JButton btnAjouter, btnModifier, btnSupprimer, btnEffacer;

    public ActeMedicalPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        initComponents();
        loadActesMedicaux();
    }

    private void initComponents() {
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(HEADER_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel searchLabel = new JLabel("Rechercher par nom patient:");
        searchLabel.setForeground(TEXT_COLOR);
        searchField = new JTextField(20);
        searchButton = new JButton("Rechercher");
        styleButton(searchButton, SEARCH_BUTTON_COLOR);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchButton.addActionListener(e -> rechercherActes());
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) rechercherActes();
            }
        });

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        idDossierMedicalField = createStyledTextField();
        idActeMedicalField = createStyledTextField();
        idPatientField = createStyledTextField();
        dateActeMedicalField = createStyledTextField();
        medecinActeMedicalField = createStyledTextField();
        typeActeMedicalCombo = new JComboBox<>(TypeActeMedical.values());
        styleComboBox(typeActeMedicalCombo);

        int row = 0;
        addFormField(formPanel, "ID Dossier Médical:", idDossierMedicalField, gbc, row++);
        addFormField(formPanel, "ID Acte Médical:", idActeMedicalField, gbc, row++);
        addFormField(formPanel, "ID Patient:", idPatientField, gbc, row++);
        addFormField(formPanel, "Type Acte Médical:", typeActeMedicalCombo, gbc, row++);
        addFormField(formPanel, "Date Acte Médical:", dateActeMedicalField, gbc, row++);
        addFormField(formPanel, "Médecin Traitant:", medecinActeMedicalField, gbc, row++);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.setBackground(BTN_PANEL_COLOR);
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        btnEffacer = new JButton("Effacer");
        styleButton(btnAjouter, ADD_BUTTON_COLOR);
        styleButton(btnModifier, UPDATE_BUTTON_COLOR);
        styleButton(btnSupprimer, DELETE_BUTTON_COLOR);
        styleButton(btnEffacer, CLEAR_BUTTON_COLOR);
        buttonsPanel.add(btnAjouter);
        buttonsPanel.add(btnModifier);
        buttonsPanel.add(btnSupprimer);
        buttonsPanel.add(btnEffacer);

        btnAjouter.addActionListener(e -> ajouterActe());
        btnModifier.addActionListener(e -> modifierActe());
        btnSupprimer.addActionListener(e -> supprimerActe());
        btnEffacer.addActionListener(e -> clearForm());

        // Table
        String[] columns = {"ID Acte","ID Dossier","ID Patient","Type Acte","Date Acte","Médecin"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        acteTable = new JTable(tableModel);
        acteTable.setFont(new Font("Arial", Font.PLAIN, 12));
        acteTable.setRowHeight(25);
        acteTable.setIntercellSpacing(new Dimension(10, 10));
        acteTable.setGridColor(new Color(200, 240, 240));
        acteTable.setShowVerticalLines(true);
        acteTable.setShowHorizontalLines(true);
        acteTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        acteTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && acteTable.getSelectedRow() != -1) loadActeToForm();
        });

        JTableHeader header = acteTable.getTableHeader();
        header.setBackground(new Color(135, 206, 250));
        header.setForeground(new Color(60, 60, 60));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setReorderingAllowed(false);

        acteTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val,
                                                                     boolean isSel, boolean hasFoc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFoc, row, col);
                if (isSel) { c.setBackground(new Color(173, 216, 230)); c.setForeground(Color.BLACK); }
                else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(Color.BLACK);
                }
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(acteTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BTN_PANEL_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void rechercherActes() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) { loadActesMedicaux(); return; }
        try {
            tableModel.setRowCount(0);
            List<ActeMedical> actes = acteMedicalDAO.rechercherParNomPatient(searchTerm);
            for (ActeMedical acte : actes) {
                tableModel.addRow(new Object[]{
                        acte.getIDActeMedical(), acte.getIDDossierMedical(),
                        acte.getIDPatient(), acte.getTypeActeMedical(),
                        acte.getDateActeMedical(), acte.getMedecinActeMedical()
                });
            }
            if (actes.isEmpty()) {
                JOptionPane.showMessageDialog(this,"Aucun acte médical trouvé pour ce patient",
                        "Recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,"Erreur lors de la recherche: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadActesMedicaux() {
        try {
            tableModel.setRowCount(0);
            for (ActeMedical acte : acteMedicalDAO.listerTous()) {
                tableModel.addRow(new Object[]{
                        acte.getIDActeMedical(), acte.getIDDossierMedical(),
                        acte.getIDPatient(), acte.getTypeActeMedical(),
                        acte.getDateActeMedical(), acte.getMedecinActeMedical()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des actes médicaux: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterActe() {
        if (!validateFields()) return;
        try {
            acteMedicalDAO.ajouter(getActeFromForm());
            loadActesMedicaux();
            clearForm();
            JOptionPane.showMessageDialog(this, "Acte médical ajouté avec succès!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierActe() {
        if (!validateFields()) return;
        try {
            acteMedicalDAO.modifier(getActeFromForm());
            loadActesMedicaux();
            clearForm();
            JOptionPane.showMessageDialog(this, "Acte médical modifié avec succès!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerActe() {
        int selectedRow = acteTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un acte à supprimer.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,"Êtes-vous sûr de vouloir supprimer cet acte?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Integer idActe = (Integer) acteTable.getValueAt(selectedRow, 0);
                acteMedicalDAO.supprimer(idActe);
                loadActesMedicaux();
                clearForm();
                JOptionPane.showMessageDialog(this, "Acte médical supprimé avec succès!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadActeToForm() {
        int row = acteTable.getSelectedRow();
        if (row < 0) return;
        idActeMedicalField.setText(acteTable.getValueAt(row, 0).toString());
        idDossierMedicalField.setText(acteTable.getValueAt(row, 1).toString());
        idPatientField.setText(acteTable.getValueAt(row, 2).toString());
        typeActeMedicalCombo.setSelectedItem(acteTable.getValueAt(row, 3));
        dateActeMedicalField.setText(acteTable.getValueAt(row, 4).toString());
        medecinActeMedicalField.setText(acteTable.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        idDossierMedicalField.setText("");
        idActeMedicalField.setText("");
        idPatientField.setText("");
        dateActeMedicalField.setText("");
        medecinActeMedicalField.setText("");
        typeActeMedicalCombo.setSelectedIndex(0);
        acteTable.clearSelection();
    }

    private ActeMedical getActeFromForm() {
        ActeMedical acte = new ActeMedical();
        acte.setIDDossierMedical(Integer.parseInt(idDossierMedicalField.getText().trim()));
        acte.setIDActeMedical(Integer.parseInt(idActeMedicalField.getText().trim()));
        acte.setIDPatient(Integer.parseInt(idPatientField.getText().trim()));
        acte.setTypeActeMedical((TypeActeMedical) typeActeMedicalCombo.getSelectedItem());
        acte.setDateActeMedical(java.sql.Date.valueOf(dateActeMedicalField.getText().trim()));
        acte.setMedecinActeMedical(medecinActeMedicalField.getText().trim());
        return acte;
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();
        if (idDossierMedicalField.getText().trim().isEmpty())
            errors.append("- Le champ 'ID Dossier Médical' est obligatoire.\n");
        if (idActeMedicalField.getText().trim().isEmpty())
            errors.append("- Le champ 'ID Acte Médical' est obligatoire.\n");
        if (idPatientField.getText().trim().isEmpty())
            errors.append("- Le champ 'ID Patient' est obligatoire.\n");
        if (dateActeMedicalField.getText().trim().isEmpty()) {
            errors.append("- Le champ 'Date Acte Médical' est obligatoire.\n");
        } else {
            try {
                java.sql.Date.valueOf(dateActeMedicalField.getText().trim());
            } catch (IllegalArgumentException e) {
                errors.append("- Le champ 'Date Acte Médical' doit être au format 'YYYY-MM-DD'.\n");
            }
        }
        if (medecinActeMedicalField.getText().trim().isEmpty())
            errors.append("- Le champ 'Médecin Traitant' est obligatoire.\n");

        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this,"Les erreurs :\n" + errors,
                    "Champs manquants ou invalides", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }


    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                comboBox.getBorder(),
                BorderFactory.createEmptyBorder(5, 13, 5, 13)
        ));
    }

    private void addFormField(JPanel panel, String label, JComponent component,
                              GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }
}