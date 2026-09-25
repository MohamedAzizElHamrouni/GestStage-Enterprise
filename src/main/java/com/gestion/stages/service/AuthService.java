package com.gestion.stages.service;

import com.gestion.stages.dao.LogConnexionDAO;
import com.gestion.stages.dao.UtilisateurDAO;
import com.gestion.stages.exception.AuthException;
import com.gestion.stages.model.LogConnexion;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.model.enums.Role;

/**
 * Implémentation du service d'authentification.
 */
public class AuthService implements IAuthService {

    private static final int MAX_TENTATIVES  = 5;
    private static final int FENETRE_MINUTES = 15;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final LogConnexionDAO logDAO        = new LogConnexionDAO();

    private Utilisateur utilisateurConnecte = null;

    @Override
    public Utilisateur connecter(String email, String motDePasse) throws AuthException {
        // Protection brute force
        int echecs = logDAO.compterEchecsRecents(email, FENETRE_MINUTES);
        if (echecs >= MAX_TENTATIVES) {
            throw AuthException.compteBloque(FENETRE_MINUTES);
        }

        Utilisateur u;
        try {
            u = utilisateurDAO.trouverParEmail(email);
        } catch (Exception e) {
            enregistrerLog(0, "LOGIN_ERREUR_DB:" + email, false);
            throw new AuthException(AuthException.CODE_IDENTIFIANTS_INVALIDES,
                    "Erreur lors de la vérification des identifiants.", e);
        }

        if (u == null) {
            enregistrerLog(0, "LOGIN_ECHEC_EMAIL_INCONNU:" + email, false);
            throw AuthException.identifiantsInvalides();
        }

        if (!u.isActif()) {
            enregistrerLog(u.getId(), "LOGIN_ECHEC_COMPTE_INACTIF", false);
            throw AuthException.compteInactif();
        }

        if (!motDePasse.equals(u.getMotDePasse())) {
            enregistrerLog(u.getId(), "LOGIN_ECHEC_MDP", false);
            int restants = MAX_TENTATIVES - echecs - 1;
            throw AuthException.identifiantsInvalides(Math.max(0, restants));
        }

        utilisateurConnecte = u;
        enregistrerLog(u.getId(), "LOGIN_SUCCES", true);
        return u;
    }

    @Override
    public void deconnecter() {
        if (utilisateurConnecte != null) {
            enregistrerLog(utilisateurConnecte.getId(), "LOGOUT", true);
            utilisateurConnecte = null;
        }
    }

    @Override
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    @Override
    public boolean estConnecte() {
        return utilisateurConnecte != null;
    }

    @Override
    public boolean aRole(Role role) {
        return utilisateurConnecte != null && utilisateurConnecte.getRole() == role;
    }

    @Override
    public void verifierPermission(Role roleRequis) throws AuthException {
        if (utilisateurConnecte == null) {
            throw AuthException.nonConnecte();
        }
        if (!aRole(roleRequis)) {
            throw AuthException.accesRefuse(roleRequis.getLibelle());
        }
    }

    private void enregistrerLog(int idUtilisateur, String action, boolean succes) {
        try {
            LogConnexion log = new LogConnexion(idUtilisateur, action, succes);
            logDAO.enregistrer(log);
        } catch (Exception ignored) {
            // Les logs ne doivent jamais bloquer l'application
        }
    }
}
