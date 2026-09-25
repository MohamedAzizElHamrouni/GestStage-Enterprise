package com.gestion.stages.model.enums;

public enum Role {
    ADMIN("Administrateur"),
    ETUDIANT("Étudiant"),
    ENTREPRISE("Entreprise"),
    ENCADRANT("Encadrant");

    private final String libelle;

    Role(String libelle) {
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
