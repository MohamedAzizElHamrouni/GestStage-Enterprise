package com.gestion.stages.service;

import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.UtilisateurException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Encadrant;
import com.gestion.stages.model.Entreprise;
import com.gestion.stages.model.Etudiant;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.model.enums.Role;

import java.util.List;

/**
 * Interface du service de gestion des utilisateurs.
 * Couvre le CRUD, la création de comptes liés (étudiant, entreprise, encadrant),
 * la sécurité et la gestion des mots de passe.
 */
public interface IUtilisateurService {

    /**
     * Crée un compte utilisateur simple (ex: ADMIN).
     * @throws UtilisateurException si l'email existe déjà
     * @throws ValidationException  si le mot de passe est trop faible
     * @throws DatabaseException    si une erreur JDBC survient
     */
    Utilisateur creerUtilisateur(Utilisateur u)
            throws UtilisateurException, ValidationException, DatabaseException;

    /**
     * Crée un compte utilisateur ÉTUDIANT et le profil étudiant associé.
     * @throws UtilisateurException si l'email est déjà utilisé
     * @throws ValidationException  si les données sont invalides
     * @throws DatabaseException    en cas d'erreur base de données
     */
    Utilisateur creerEtudiantComplet(Utilisateur u, Etudiant etudiant)
            throws UtilisateurException, ValidationException, DatabaseException;

    /**
     * Crée un compte utilisateur ENTREPRISE et le profil entreprise associé.
     */
    Utilisateur creerEntrepriseComplete(Utilisateur u, Entreprise entreprise)
            throws UtilisateurException, ValidationException, DatabaseException;

    /**
     * Crée un compte utilisateur ENCADRANT et le profil encadrant associé.
     */
    Utilisateur creerEncadrantComplet(Utilisateur u, Encadrant encadrant)
            throws UtilisateurException, ValidationException, DatabaseException;

    /**
     * Modifie les informations d'un utilisateur existant.
     * @throws UtilisateurException si l'utilisateur est introuvable
     * @throws DatabaseException    en cas d'erreur base de données
     */
    boolean modifierUtilisateur(Utilisateur u)
            throws UtilisateurException, DatabaseException;

    /**
     * Change le mot de passe d'un utilisateur après vérification de l'ancien.
     * @throws UtilisateurException si l'utilisateur est introuvable ou l'ancien mot de passe est incorrect
     * @throws ValidationException  si le nouveau mot de passe est trop faible
     */
    boolean changerMotDePasse(int id, String ancienMdp, String nouveauMdp)
            throws UtilisateurException, ValidationException, DatabaseException;

    /**
     * Désactive (suppression logique) un compte utilisateur.
     * @throws UtilisateurException si l'utilisateur est introuvable
     */
    boolean desactiverUtilisateur(int id) throws UtilisateurException, DatabaseException;

    /**
     * Supprime définitivement un compte utilisateur.
     * @throws UtilisateurException si l'utilisateur est introuvable
     */
    boolean supprimerUtilisateur(int id) throws UtilisateurException, DatabaseException;

    /**
     * Recherche un utilisateur par son identifiant.
     * @throws UtilisateurException si l'utilisateur est introuvable
     */
    Utilisateur trouverParId(int id) throws UtilisateurException;

    /** Retourne tous les utilisateurs (actifs et inactifs). */
    List<Utilisateur> listerTous();

    /** Retourne tous les utilisateurs ayant un rôle donné. */
    List<Utilisateur> listerParRole(Role role);

    /** Recherche des utilisateurs par nom, prénom ou email. */
    List<Utilisateur> rechercher(String terme);

    /**
     * Réinitialise le mot de passe d'un utilisateur et retourne le mot de passe temporaire en clair.
     * @throws UtilisateurException si l'utilisateur est introuvable
     */
    String reinitialiserMotDePasse(int id) throws UtilisateurException, DatabaseException;
}
