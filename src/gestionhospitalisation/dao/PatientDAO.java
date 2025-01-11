package gestionhospitalisation.dao;

import gestionhospitalisation.models.Patient;
import gestionhospitalisation.models.TypeMutuelle;
import gestionhospitalisation.utils.DatabaseConnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;


public class PatientDAO {

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
            .optionalEnd()
            .toFormatter();
    
    // CRUD Opertations 

    public void ajouter(Patient patient) throws SQLException {
        String sql = "INSERT INTO patient (ncin, nom_prenom_patient, adresse_patient, " +
                "tel_patient, mutuelle_patient, type_mutuelle_patient, date_creation) " +
                "VALUES (?, ?, ?, ?, ?::mutuelle_type, ?::type_mutuelle, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, patient.getNcin());
            pstmt.setString(2, patient.getNomPrenomPatient());
            pstmt.setString(3, patient.getAdressePatient());
            pstmt.setString(4, patient.getTelPatient());

            
            pstmt.setString(5, patient.getMutuellePatient().toUpperCase());

          
            if ("NON".equalsIgnoreCase(patient.getMutuellePatient()) || patient.getTypeMutuellePatient() == null) {
                pstmt.setNull(6, Types.OTHER);
            } else {
                pstmt.setString(6, patient.getTypeMutuellePatient().name());
            }

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setIdPatient(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void modifier(Patient patient) throws SQLException {
        String sql = "UPDATE patient SET ncin = ?, nom_prenom_patient = ?, " +
                "adresse_patient = ?, tel_patient = ?, mutuelle_patient = ?::mutuelle_type, " +
                "type_mutuelle_patient = ?::type_mutuelle, date_modification = CURRENT_TIMESTAMP " +
                "WHERE id_patient = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getNcin());
            pstmt.setString(2, patient.getNomPrenomPatient());
            pstmt.setString(3, patient.getAdressePatient());
            pstmt.setString(4, patient.getTelPatient());

            pstmt.setString(5, patient.getMutuellePatient().toUpperCase());
            if ("NON".equalsIgnoreCase(patient.getMutuellePatient()) || patient.getTypeMutuellePatient() == null) {
                pstmt.setNull(6, Types.OTHER);
            } else {
                pstmt.setString(6, patient.getTypeMutuellePatient().name());
            }
            pstmt.setInt(7, patient.getIdPatient());

            pstmt.executeUpdate();
        }
    }

    public void supprimer(int idPatient) throws SQLException {
        String sql = "DELETE FROM patient WHERE id_patient = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPatient);
            pstmt.executeUpdate();
        }
    }

    public List<Patient> listerTous() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT *, " +
                "TO_CHAR(date_creation, 'YYYY-MM-DD HH24:MI:SS.US') as creation_str, " +
                "TO_CHAR(date_modification, 'YYYY-MM-DD HH24:MI:SS.US') as modification_str " +
                "FROM patient ORDER BY date_creation DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setIdPatient(rs.getInt("id_patient"));
                patient.setNcin(rs.getString("ncin"));
                patient.setNomPrenomPatient(rs.getString("nom_prenom_patient"));
                patient.setAdressePatient(rs.getString("adresse_patient"));
                patient.setTelPatient(rs.getString("tel_patient"));
                patient.setMutuellePatient(rs.getString("mutuelle_patient"));

               
                String typeMutuelleStr = rs.getString("type_mutuelle_patient");
                if (typeMutuelleStr != null) {
                    patient.setTypeMutuellePatient(TypeMutuelle.valueOf(typeMutuelleStr));
                } else {
                    patient.setTypeMutuellePatient(null);
                }

                String creationStr = rs.getString("creation_str");
                if (creationStr != null) {
                    patient.setDateCreation(LocalDateTime.parse(creationStr, formatter));
                }

                String modificationStr = rs.getString("modification_str");
                if (modificationStr != null) {
                    patient.setDateModification(LocalDateTime.parse(modificationStr, formatter));
                }

                patients.add(patient);
            }
        }
        return patients;
    }

   

    public List<Patient> rechercherParCIN(String cin) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient WHERE ncin LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + cin + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setIdPatient(rs.getInt("id_patient"));
                    patient.setNcin(rs.getString("ncin"));
                    patient.setNomPrenomPatient(rs.getString("nom_prenom_patient"));
                    patient.setAdressePatient(rs.getString("adresse_patient"));
                    patient.setTelPatient(rs.getString("tel_patient"));
                    patient.setMutuellePatient(rs.getString("mutuelle_patient"));

                    String typeMutuelleStr = rs.getString("type_mutuelle_patient");
                    if (typeMutuelleStr != null) {
                        patient.setTypeMutuellePatient(TypeMutuelle.valueOf(typeMutuelleStr));
                    } else {
                        patient.setTypeMutuellePatient(null);
                    }

                    String creationStr = rs.getString("date_creation");
                    if (creationStr != null) {
                        patient.setDateCreation(LocalDateTime.parse(creationStr, formatter));
                    }

                    String modificationStr = rs.getString("date_modification");
                    if (modificationStr != null) {
                        patient.setDateModification(LocalDateTime.parse(modificationStr, formatter));
                    }

                    patients.add(patient);
                }
            }
        }
        return patients;
    }
}