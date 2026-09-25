package com.gestion.stages.model.enums;

public enum StatutOffre {
    EN_ATTENTE("En Attente de Validation"),
    VALIDEE("Validée"),
    REJETEE("Rejetée"),
    EXPIREE("Expirée"),
    POURVUE("Pourvue");

    private final String libelle;

    StatutOffre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
