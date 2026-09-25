package com.gestion.stages.exception;

/**
 * Exception de base pour toute l'application.
 * Toutes les exceptions personnalisées héritent de cette classe.
 */
public class GestionStagesException extends Exception {

    private final String codeErreur;

    public GestionStagesException(String message) {
        super(message);
        this.codeErreur = "ERR_GENERAL";
    }

    public GestionStagesException(String codeErreur, String message) {
        super(message);
        this.codeErreur = codeErreur;
    }

    public GestionStagesException(String codeErreur, String message, Throwable cause) {
        super(message, cause);
        this.codeErreur = codeErreur;
    }

    public String getCodeErreur() {
        return codeErreur;
    }

    @Override
    public String toString() {
        return "[" + codeErreur + "] " + getMessage();
    }
}
