package gestionhospitalisation.dao;

import gestionhospitalisation.models.ActeMedical;
import gestionhospitalisation.models.TypeActeMedical;
import gestionhospitalisation.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActeMedicalDAO {

    public void ajouter(ActeMedical acte) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM actemedical WHERE idactemedical = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, acte.getIDActeMedical());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("L'acte médical avec l'ID " + acte.getIDActeMedical() + " existe déjà.");
            }

            String insertSql = "INSERT INTO actemedical (idactemedical, iddossiermedical, idpatient, " +
                    "typeactemedical, dateactemedical, medecinactemedical) " +
                    "VALUES (?, ?, ?, ?::type_acte_medical, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, acte.getIDActeMedical());
                pstmt.setInt(2, acte.getIDDossierMedical());
                pstmt.setInt(3, acte.getIDPatient());
                pstmt.setString(4, acte.getTypeActeMedical().name()); 
                pstmt.setDate(5, acte.getDateActeMedical());
                pstmt.setString(6, acte.getMedecinActeMedical());

                pstmt.executeUpdate();
            }
        }
    }

    public void modifier(ActeMedical acte) throws SQLException {
        String updateSql = "UPDATE actemedical SET " +
                "iddossiermedical = ?, " +
                "idpatient = ?, " +
                "typeactemedical = ?::type_acte_medical, " +
                "dateactemedical = ?, " +
                "medecinactemedical = ? " +
                "WHERE idactemedical = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            pstmt.setInt(1, acte.getIDDossierMedical());
            pstmt.setInt(2, acte.getIDPatient());
            pstmt.setString(3, acte.getTypeActeMedical().name());
            pstmt.setDate(4, acte.getDateActeMedical());
            pstmt.setString(5, acte.getMedecinActeMedical());
            pstmt.setInt(6, acte.getIDActeMedical());

            pstmt.executeUpdate();
        }
    }

    public void supprimer(int idActeMedical) throws SQLException {
        String sql = "DELETE FROM actemedical WHERE idactemedical = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idActeMedical);
            pstmt.executeUpdate();
        }
    }

    public List<ActeMedical> listerTous() throws SQLException {
        List<ActeMedical> actesMedicaux = new ArrayList<>();
        String sql = "SELECT a.*, p.nom_prenom_patient " +
                "FROM actemedical a " +
                "LEFT JOIN patient p ON a.idpatient = p.id_patient " +
                "ORDER BY a.idactemedical";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                actesMedicaux.add(extraireActeMedical(rs));
            }
        }
        return actesMedicaux;
    }

    public List<ActeMedical> rechercherParNomPatient(String nomPatient) throws SQLException {
        List<ActeMedical> actesMedicaux = new ArrayList<>();

        String sql = "SELECT a.*, p.nom_prenom_patient " +
                "FROM actemedical a " +
                "JOIN patient p ON a.idpatient = p.id_patient " +
                "WHERE LOWER(p.nom_prenom_patient) LIKE LOWER(?) " +
                "ORDER BY a.idactemedical";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nomPatient + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    actesMedicaux.add(extraireActeMedical(rs));
                }
            }
        }

        return actesMedicaux;
    }

    private ActeMedical extraireActeMedical(ResultSet rs) throws SQLException {
        ActeMedical acteMedical = new ActeMedical();

        acteMedical.setIDDossierMedical(rs.getInt("iddossiermedical"));
        acteMedical.setIDActeMedical(rs.getInt("idactemedical"));
        acteMedical.setIDPatient(rs.getInt("idpatient"));
        acteMedical.setTypeActeMedical(TypeActeMedical.valueOf(rs.getString("typeactemedical")));
        acteMedical.setDateActeMedical(rs.getDate("dateactemedical"));
        acteMedical.setMedecinActeMedical(rs.getString("medecinactemedical"));
        acteMedical.setNomPatient(rs.getString("nom_prenom_patient"));

        return acteMedical;
    }
}