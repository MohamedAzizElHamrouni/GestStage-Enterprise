package com.gestion.stages.model.enums;

public enum StatutCandidature {
    EN_ATTENTE("En Attente"),
    ACCEPTEE("Acceptée"),
    REFUSEE("Refusée"),
    ANNULEE("Annulée");

    private final String libelle;

    StatutCandidature(String libelle) {
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
