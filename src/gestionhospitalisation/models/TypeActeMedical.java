package gestionhospitalisation.models;

public enum TypeActeMedical {
    HOSPITALISATION_COMPLETE("Hospitalisation complète"),
    HOSPITALISATION_JOURNALIERE("Hospitalisation journalière"),
    CONSULTATION("Consultation"),
    AUTRE("Autre type");

    private final String libelle;

    TypeActeMedical(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}