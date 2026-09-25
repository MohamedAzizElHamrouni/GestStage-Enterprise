package com.gestion.stages.model.enums;

public enum StatutStage {
    SANS_STAGE("Sans Stage"),
    EN_COURS("En Cours"),
    TERMINE("Terminé"),
    VALIDE("Validé"),
    ABANDONNE("Abandonné");

    private final String libelle;

    StatutStage(String libelle) {
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
