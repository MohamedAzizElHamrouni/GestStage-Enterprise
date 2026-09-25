package com.gestion.stages.exception;

/**
 * Exception levée lors d'erreurs d'accès à la base de données.
 * Encapsule les SQLException et autres erreurs JDBC.
 */
public class DatabaseException extends GestionStagesException {

    public static final String CODE_CONNEXION_ECHOUEE = "DB_001";
    public static final String CODE_REQUETE_ECHOUEE   = "DB_002";
    public static final String CODE_CONTRAINTE        = "DB_003";
    public static final String CODE_ENREGISTREMENT    = "DB_004";
    public static final String CODE_SUPPRESSION       = "DB_005";

    public DatabaseException(String message) {
        super("DB_ERR", message);
    }

    public DatabaseException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public DatabaseException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static DatabaseException connexionEchouee(Throwable cause) {
        return new DatabaseException(CODE_CONNEXION_ECHOUEE,
                "Impossible de se connecter à la base de données : " + cause.getMessage(), cause);
    }

    public static DatabaseException requeteEchouee(String operation, Throwable cause) {
        return new DatabaseException(CODE_REQUETE_ECHOUEE,
                "Erreur lors de l'opération '" + operation + "' : " + cause.getMessage(), cause);
    }

    public static DatabaseException contrainteViolee(String detail, Throwable cause) {
        return new DatabaseException(CODE_CONTRAINTE,
                "Contrainte de base de données violée : " + detail, cause);
    }

    public static DatabaseException enregistrementEchoue(String entite, Throwable cause) {
        return new DatabaseException(CODE_ENREGISTREMENT,
                "Échec de l'enregistrement de l'entité '" + entite + "' : " + cause.getMessage(), cause);
    }

    public static DatabaseException suppressionEchouee(String entite, int id, Throwable cause) {
        return new DatabaseException(CODE_SUPPRESSION,
                "Échec de la suppression de '" + entite + "' ID=" + id + " : " + cause.getMessage(), cause);
    }
}
