package com.gestion.stages.service;

import com.gestion.stages.exception.AuthException;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.model.enums.Role;

/**
 * Interface du service d'authentification.
 * Gère la connexion, déconnexion, session et contrôle d'accès.
 */
public interface IAuthService {

    /**
     * Authentifie un utilisateur par email et mot de passe.
     * @throws AuthException si les identifiants sont invalides, le compte est inactif ou bloqué
     */
    Utilisateur connecter(String email, String motDePasse) throws AuthException;

    /**
     * Déconnecte l'utilisateur actuellement connecté et enregistre le log.
     */
    void deconnecter();

    /**
     * Retourne l'utilisateur actuellement connecté en session.
     * @return l'utilisateur connecté, ou null si personne n'est connecté
     */
    Utilisateur getUtilisateurConnecte();

    /**
     * Vérifie si une session est active.
     */
    boolean estConnecte();

    /**
     * Vérifie si l'utilisateur connecté possède le rôle spécifié.
     */
    boolean aRole(Role role);

    /**
     * Vérifie que l'utilisateur connecté a le rôle requis.
     * @throws AuthException si l'accès est refusé
     */
    void verifierPermission(Role roleRequis) throws AuthException;
}
