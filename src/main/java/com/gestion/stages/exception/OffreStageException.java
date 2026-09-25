package com.gestion.stages.exception;

/**
 * Exception levée lors de problèmes liés aux offres de stage.
 */
public class OffreStageException extends GestionStagesException {

    public static final String CODE_INTROUVABLE       = "OFF_001";
    public static final String CODE_NON_DISPONIBLE    = "OFF_002";
    public static final String CODE_PLUS_DE_PLACES    = "OFF_003";
    public static final String CODE_DEJA_VALIDEE      = "OFF_004";
    public static final String CODE_EXPIREE           = "OFF_005";
    public static final String CODE_STATUT_INVALIDE   = "OFF_006";

    public OffreStageException(String message) {
        super("OFF_ERR", message);
    }

    public OffreStageException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public OffreStageException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static OffreStageException introuvable(int id) {
        return new OffreStageException(CODE_INTROUVABLE,
                "Aucune offre de stage trouvée avec l'ID : " + id);
    }

    public static OffreStageException nonDisponible(String statut) {
        return new OffreStageException(CODE_NON_DISPONIBLE,
                "Cette offre n'est pas disponible (statut actuel : " + statut + ").");
    }

    public static OffreStageException plusDePlaces(String titreOffre) {
        return new OffreStageException(CODE_PLUS_DE_PLACES,
                "Plus aucune place disponible pour l'offre : '" + titreOffre + "'.");
    }

    public static OffreStageException expiree(String titreOffre) {
        return new OffreStageException(CODE_EXPIREE,
                "L'offre '" + titreOffre + "' a expiré.");
    }

    public static OffreStageException dejaValidee(int id) {
        return new OffreStageException(CODE_DEJA_VALIDEE,
                "L'offre ID " + id + " est déjà validée.");
    }
}
