package com.gestion.stages.exception;

/**
 * Exception levée lors de problèmes liés aux candidatures.
 */
public class CandidatureException extends GestionStagesException {

    public static final String CODE_INTROUVABLE          = "CAND_001";
    public static final String CODE_DOUBLON              = "CAND_002";
    public static final String CODE_STATUT_INVALIDE      = "CAND_003";
    public static final String CODE_DEJA_TRAITEE         = "CAND_004";
    public static final String CODE_ANNULATION_INTERDITE = "CAND_005";

    public CandidatureException(String message) {
        super("CAND_ERR", message);
    }

    public CandidatureException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public CandidatureException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static CandidatureException introuvable(int id) {
        return new CandidatureException(CODE_INTROUVABLE,
                "Aucune candidature trouvée avec l'ID : " + id);
    }

    public static CandidatureException doublon(String titreOffre) {
        return new CandidatureException(CODE_DOUBLON,
                "Vous avez déjà postulé à l'offre : '" + titreOffre + "'.");
    }

    public static CandidatureException dejaTraitee(int id, String statut) {
        return new CandidatureException(CODE_DEJA_TRAITEE,
                "La candidature ID " + id + " a déjà été traitée (statut : " + statut + ").");
    }

    public static CandidatureException annulationInterdite(String statut) {
        return new CandidatureException(CODE_ANNULATION_INTERDITE,
                "Impossible d'annuler une candidature au statut : " + statut + ".");
    }
}
