package com.gestion.stages.exception;

/**
 * Exception levée lors de problèmes liés à la gestion des entreprises.
 */
public class EntrepriseException extends GestionStagesException {

    public static final String CODE_INTROUVABLE        = "ENT_001";
    public static final String CODE_NON_VALIDEE        = "ENT_002";
    public static final String CODE_DEJA_VALIDEE       = "ENT_003";
    public static final String CODE_NOTE_INVALIDE      = "ENT_004";
    public static final String CODE_SUPPRESSION_REFUS  = "ENT_005";

    public EntrepriseException(String message) {
        super("ENT_ERR", message);
    }

    public EntrepriseException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public EntrepriseException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static EntrepriseException introuvable(int id) {
        return new EntrepriseException(CODE_INTROUVABLE,
                "Aucune entreprise trouvée avec l'ID : " + id);
    }

    public static EntrepriseException nonValidee(String nomEntreprise) {
        return new EntrepriseException(CODE_NON_VALIDEE,
                "L'entreprise '" + nomEntreprise + "' n'a pas encore été validée par l'administration.");
    }

    public static EntrepriseException dejaValidee(String nomEntreprise) {
        return new EntrepriseException(CODE_DEJA_VALIDEE,
                "L'entreprise '" + nomEntreprise + "' est déjà validée.");
    }

    public static EntrepriseException noteInvalide() {
        return new EntrepriseException(CODE_NOTE_INVALIDE,
                "La note doit être comprise entre 0 et 5.");
    }
}
