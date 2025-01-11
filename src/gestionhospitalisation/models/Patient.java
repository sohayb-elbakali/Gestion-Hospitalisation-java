package gestionhospitalisation.models;

import java.time.LocalDateTime;

public class Patient {
    private int idPatient;
    private String ncin;
    private String nomPrenomPatient;
    private String adressePatient;
    private String telPatient;
    private String mutuellePatient;
    private TypeMutuelle typeMutuellePatient;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    public Patient() {
        this.dateCreation = LocalDateTime.now();
    }

    // Existing getters and setters
    public int getIdPatient() {
        return idPatient;
    }
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public String getNcin() {
        return ncin;
    }
    public void setNcin(String ncin) {
        this.ncin = ncin;
    }

    public String getNomPrenomPatient() {
        return nomPrenomPatient;
    }
    public void setNomPrenomPatient(String nomPrenomPatient) {
        this.nomPrenomPatient = nomPrenomPatient;
    }

    public String getAdressePatient() {
        return adressePatient;
    }
    public void setAdressePatient(String adressePatient) {
        this.adressePatient = adressePatient;
    }

    public String getTelPatient() {
        return telPatient;
    }
    public void setTelPatient(String telPatient) {
        this.telPatient = telPatient;
    }

    public String getMutuellePatient() {
        return mutuellePatient;
    }
    public void setMutuellePatient(String mutuellePatient) {
        this.mutuellePatient = mutuellePatient;
    }

    public TypeMutuelle getTypeMutuellePatient() {
        return typeMutuellePatient;
    }
    public void setTypeMutuellePatient(TypeMutuelle typeMutuellePatient) {
        this.typeMutuellePatient = typeMutuellePatient;
    }

    // Getters and setters for dates
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public LocalDateTime getDateModification() {
        return dateModification;
    }
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

}