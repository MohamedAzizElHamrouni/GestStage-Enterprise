package com.gestion.stages.exception;

/**
 * Exception levée lors de problèmes liés à la gestion des étudiants.
 */
public class EtudiantException extends GestionStagesException {

    public static final String CODE_INTROUVABLE         = "ETU_001";
    public static final String CODE_MATRICULE_EXISTE    = "ETU_002";
    public static final String CODE_STATUT_INVALIDE     = "ETU_003";
    public static final String CODE_DEJA_EN_STAGE       = "ETU_004";
    public static final String CODE_PROFIL_INCOMPLET    = "ETU_005";

    public EtudiantException(String message) {
        super("ETU_ERR", message);
    }

    public EtudiantException(String codeErreur, String message) {
        super(codeErreur, message);
    }

    public EtudiantException(String codeErreur, String message, Throwable cause) {
        super(codeErreur, message, cause);
    }

    public static EtudiantException introuvable(int id) {
        return new EtudiantException(CODE_INTROUVABLE,
                "Aucun étudiant trouvé avec l'ID : " + id);
    }

    public static EtudiantException introuvableParMatricule(String matricule) {
        return new EtudiantException(CODE_INTROUVABLE,
                "Aucun étudiant trouvé avec le matricule : " + matricule);
    }

    public static EtudiantException matriculeDejaUtilise(String matricule) {
        return new EtudiantException(CODE_MATRICULE_EXISTE,
                "Le matricule '" + matricule + "' est déjà utilisé.");
    }

    public static EtudiantException dejaEnStage(String nomEtudiant) {
        return new EtudiantException(CODE_DEJA_EN_STAGE,
                "L'étudiant " + nomEtudiant + " est déjà en stage.");
    }

    public static EtudiantException profilIncomplet(String champ) {
        return new EtudiantException(CODE_PROFIL_INCOMPLET,
                "Le profil étudiant est incomplet. Champ manquant : " + champ);
    }
}
