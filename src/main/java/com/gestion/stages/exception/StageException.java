package com.gestion.stages.exception;

/**
 * Exception levée lors de problèmes liés au suivi des stages.
 */
public class StageException extends GestionStagesException {

    public static final String CODE_INTROUVABLE          = "STG_001";
    public static final String CODE_DEJA_EN_COURS        = "STG_002";
    public static final String CODE_STATUT_INVALIDE      = "STG_003";
    public static final String CODE_NOTE_INVALIDE        = "STG_004";
    public static final String CODE_RAPPORT_MANQUANT     = "STG_005";
    public static final String CODE_ATTESTATION_INTERDITE = "STG_006";

    public StageException(String message) {
        super("STG_ERR", message);
    }

    public StageException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public StageException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static StageException introuvable(int id) {
        return new StageException(CODE_INTROUVABLE,
                "Aucun stage trouvé avec l'ID : " + id);
    }

    public static StageException dejaEnCours(String nomEtudiant) {
        return new StageException(CODE_DEJA_EN_COURS,
                "L'étudiant " + nomEtudiant + " a déjà un stage en cours.");
    }

    public static StageException noteInvalide() {
        return new StageException(CODE_NOTE_INVALIDE,
                "La note doit être comprise entre 0 et 20.");
    }

    public static StageException rapportManquant() {
        return new StageException(CODE_RAPPORT_MANQUANT,
                "Le rapport de stage doit être soumis avant la validation.");
    }

    public static StageException attestationInterdite(String statut) {
        return new StageException(CODE_ATTESTATION_INTERDITE,
                "L'attestation ne peut être générée que pour un stage terminé ou validé. Statut actuel : " + statut);
    }

    public static StageException statutInvalide(String operation, String statutActuel) {
        return new StageException(CODE_STATUT_INVALIDE,
                "Impossible d'effectuer '" + operation + "' avec le statut actuel : " + statutActuel);
    }
}
