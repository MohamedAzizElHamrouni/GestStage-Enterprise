package com.gestion.stages.exception;

/**
 * Exception levée lors de problèmes liés à la gestion des utilisateurs.
 */
public class UtilisateurException extends GestionStagesException {

    public static final String CODE_INTROUVABLE       = "USER_001";
    public static final String CODE_EMAIL_EXISTE      = "USER_002";
    public static final String CODE_MOT_DE_PASSE      = "USER_003";
    public static final String CODE_ROLE_INVALIDE     = "USER_004";
    public static final String CODE_DEJA_INACTIF      = "USER_005";

    public UtilisateurException(String message) {
        super("USER_ERR", message);
    }

    public UtilisateurException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public UtilisateurException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static UtilisateurException introuvable(int id) {
        return new UtilisateurException(CODE_INTROUVABLE,
                "Aucun utilisateur trouvé avec l'ID : " + id);
    }

    public static UtilisateurException introuvableParEmail(String email) {
        return new UtilisateurException(CODE_INTROUVABLE,
                "Aucun utilisateur trouvé avec l'email : " + email);
    }

    public static UtilisateurException emailDejaUtilise(String email) {
        return new UtilisateurException(CODE_EMAIL_EXISTE,
                "Un utilisateur avec l'email '" + email + "' existe déjà.");
    }

    public static UtilisateurException motDePasseIncorrect() {
        return new UtilisateurException(CODE_MOT_DE_PASSE,
                "L'ancien mot de passe saisi est incorrect.");
    }
}
