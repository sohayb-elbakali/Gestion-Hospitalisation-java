package gestionhospitalisation.models;

import java.sql.Date;

public class ActeMedical {
    private Integer idDossierMedical;
    private Integer idActeMedical;
    private Integer idPatient;
    private String nomPatient;
    private TypeActeMedical typeActeMedical;
    private Date dateActeMedical;
    private String medecinActeMedical;

    // Getters
    public Integer getIDDossierMedical() { return idDossierMedical; }
    public Integer getIDActeMedical() { return idActeMedical; }
    public Integer getIDPatient() { return idPatient; }
    public String getNomPatient() { return nomPatient; }
    public TypeActeMedical getTypeActeMedical() { return typeActeMedical; }
    public Date getDateActeMedical() { return dateActeMedical; }
    public String getMedecinActeMedical() { return medecinActeMedical; }

    // Setters
    public void setIDDossierMedical(Integer idDossierMedical) { this.idDossierMedical = idDossierMedical; }
    public void setIDActeMedical(Integer idActeMedical) { this.idActeMedical = idActeMedical; }
    public void setIDPatient(Integer idPatient) { this.idPatient = idPatient; }
    public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }
    public void setTypeActeMedical(TypeActeMedical typeActeMedical) { this.typeActeMedical = typeActeMedical; }
    public void setDateActeMedical(Date dateActeMedical) { this.dateActeMedical = dateActeMedical; }
    public void setMedecinActeMedical(String medecinActeMedical) { this.medecinActeMedical = medecinActeMedical; }


}