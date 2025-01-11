package gestionhospitalisation.models;

public enum TypeMutuelle {
    CNOPS("CNOPS"),
    CNSS("CNSS"),
    ASSURANCE("ASSURANCE"),
    AUTRE("AUTRE");

    private final String label;

    TypeMutuelle(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

}