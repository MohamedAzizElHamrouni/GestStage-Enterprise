package com.gestion.stages.model.enums;

public enum NiveauEtude {
    L1("Licence 1"),
    L2("Licence 2"),
    L3("Licence 3"),
    M1("Master 1"),
    M2("Master 2"),
    DOCTORAT("Doctorat");

    private final String libelle;

    NiveauEtude(String libelle) {
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
