package com.gestion.stages.exception;

/**
 * Exception levée lors de problèmes d'authentification.
 * Ex : mauvais mot de passe, compte bloqué, accès non autorisé.
 */
public class AuthException extends GestionStagesException {

    public static final String CODE_IDENTIFIANTS_INVALIDES = "AUTH_001";
    public static final String CODE_COMPTE_INACTIF         = "AUTH_002";
    public static final String CODE_COMPTE_BLOQUE          = "AUTH_003";
    public static final String CODE_ACCES_REFUSE           = "AUTH_004";
    public static final String CODE_SESSION_EXPIREE        = "AUTH_005";

    public AuthException(String message) {
        super("AUTH_ERR", message);
    }

    public AuthException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public AuthException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static AuthException identifiantsInvalides() {
        return new AuthException(CODE_IDENTIFIANTS_INVALIDES, "Email ou mot de passe incorrect.");
    }

    public static AuthException identifiantsInvalides(int tentativesRestantes) {
        return new AuthException(CODE_IDENTIFIANTS_INVALIDES,
                "Mot de passe incorrect. " + tentativesRestantes + " tentative(s) restante(s).");
    }

    public static AuthException compteInactif() {
        return new AuthException(CODE_COMPTE_INACTIF, "Ce compte est désactivé. Contactez l'administration.");
    }

    public static AuthException compteBloque(int minutes) {
        return new AuthException(CODE_COMPTE_BLOQUE,
                "Compte temporairement bloqué. Trop de tentatives. Réessayez dans " + minutes + " minutes.");
    }

    public static AuthException accesRefuse(String roleRequis) {
        return new AuthException(CODE_ACCES_REFUSE,
                "Accès refusé. Rôle requis : " + roleRequis + ".");
    }

    public static AuthException nonConnecte() {
        return new AuthException(CODE_SESSION_EXPIREE, "Vous n'êtes pas connecté.");
    }
}
