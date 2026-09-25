package com.gestion.stages.exception;

/**
 * Exception levée lors de la validation des données saisies.
 * Utilisée pour les vérifications de format, longueur, plage de valeurs, etc.
 */
public class ValidationException extends GestionStagesException {

    public static final String CODE_CHAMP_VIDE           = "VAL_001";
    public static final String CODE_FORMAT_EMAIL         = "VAL_002";
    public static final String CODE_MOT_DE_PASSE_FAIBLE  = "VAL_003";
    public static final String CODE_VALEUR_HORS_PLAGE    = "VAL_004";
    public static final String CODE_DATE_INVALIDE        = "VAL_005";
    public static final String CODE_LONGUEUR_INVALIDE    = "VAL_006";

    private final String champ;

    public ValidationException(String message) {
        super("VAL_ERR", message);
        this.champ = null;
    }

    public ValidationException(String codeErreur, String champ, String message) {
        super(codeErreur, message);
        this.champ = champ;
    }

    public String getChamp() {
        return champ;
    }

    public static ValidationException champVide(String nomChamp) {
        return new ValidationException(CODE_CHAMP_VIDE, nomChamp,
                "Le champ '" + nomChamp + "' est obligatoire et ne peut pas être vide.");
    }

    public static ValidationException formatEmailInvalide(String email) {
        return new ValidationException(CODE_FORMAT_EMAIL, "email",
                "L'adresse email '" + email + "' n'est pas valide.");
    }

    public static ValidationException motDePasseFaible() {
        return new ValidationException(CODE_MOT_DE_PASSE_FAIBLE, "motDePasse",
                "Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule et un chiffre.");
    }

    public static ValidationException valeurHorsPlage(String champ, double min, double max) {
        return new ValidationException(CODE_VALEUR_HORS_PLAGE, champ,
                "La valeur de '" + champ + "' doit être entre " + min + " et " + max + ".");
    }

    public static ValidationException dateDebutApresDateFin() {
        return new ValidationException(CODE_DATE_INVALIDE, "date",
                "La date de début ne peut pas être postérieure à la date de fin.");
    }

    public static ValidationException longueurInvalide(String champ, int min, int max) {
        return new ValidationException(CODE_LONGUEUR_INVALIDE, champ,
                "Le champ '" + champ + "' doit contenir entre " + min + " et " + max + " caractères.");
    }
}
