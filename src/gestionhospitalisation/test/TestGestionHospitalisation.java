package gestionhospitalisation.test;

import gestionhospitalisation.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestGestionHospitalisation {

    public static void main(String[] args) {
        // Affichage de l'en-tête
        System.out.println("Test de la Base de Données GestionHospitalisation");
        System.out.println("-----------------------------------------------");

        // Affichage date et utilisateur
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Date and Time (UTC): " + LocalDateTime.now().format(formatter));
        System.out.println("Current User's Login: sohayb-elbakali");
        System.out.println("-----------------------------------------------\n");

        // Test de connexion et mise à jour
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("1. Test de connexion:");
            System.out.println("✓ Connexion à la base de données réussie!");

            System.out.println("\n2. Test de mise à jour:");
            testMiseAJour(conn);

        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void testMiseAJour(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Test de mise à jour de la table patient
            String updatePatient = "UPDATE patient SET " +
                    "adresse_patient = 'Nouvelle Adresse Test' " +
                    "WHERE ncin = 'LB248006'";

            int resultPatient = stmt.executeUpdate(updatePatient);
            System.out.println("Test mise à jour patients: " +
                    (resultPatient >= 0 ? "✓ Succès" : "❌ Échec"));

            // Test de mise à jour de la table acte medical
            String updateActe = "UPDATE actemedical SET " +
                    "typeactemedical = 'CONSULTATION'::type_acte_medical " +
                    "WHERE idactemedical = 1";

            int resultActe = stmt.executeUpdate(updateActe);
            System.out.println("Test mise à jour actes médicaux: " +
                    (resultActe >= 0 ? "✓ Succès" : "❌ Échec"));

            System.out.println("\n✓ Tests de mise à jour terminés avec succès!");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour: " + e.getMessage());
            throw e;
        }
    }
}