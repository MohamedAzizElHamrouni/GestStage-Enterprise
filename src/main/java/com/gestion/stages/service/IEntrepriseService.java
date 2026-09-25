package com.gestion.stages.service;

import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.EntrepriseException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Entreprise;

import java.util.List;

/**
 * Interface du service de gestion des entreprises.
 * Couvre le CRUD, la validation admin, la notation et la recherche.
 */
public interface IEntrepriseService {

    /**
     * Recherche une entreprise par son identifiant.
     * @throws EntrepriseException si l'entreprise est introuvable
     */
    Entreprise trouverParId(int id) throws EntrepriseException;

    /**
     * Recherche le profil entreprise associé à un compte utilisateur.
     * @throws EntrepriseException si aucun profil n'est trouvé
     */
    Entreprise trouverParUtilisateur(int idUtilisateur) throws EntrepriseException;

    /**
     * Met à jour le profil d'une entreprise.
     * @throws EntrepriseException si l'entreprise est introuvable
     * @throws ValidationException si les données sont invalides
     */
    boolean modifier(Entreprise e) throws EntrepriseException, ValidationException, DatabaseException;

    /**
     * Valide le compte d'une entreprise (accès accordé par l'admin).
     * @throws EntrepriseException si l'entreprise est introuvable
     */
    boolean valider(int id) throws EntrepriseException, DatabaseException;

    /**
     * Rejette ou révoque la validation d'une entreprise.
     * @throws EntrepriseException si l'entreprise est introuvable
     */
    boolean rejeter(int id) throws EntrepriseException, DatabaseException;

    /**
     * Attribue une note à une entreprise.
     * @throws EntrepriseException si l'entreprise est introuvable
     * @throws ValidationException si la note est hors de la plage [0, 5]
     */
    boolean noter(int id, double note) throws EntrepriseException, ValidationException, DatabaseException;

    /**
     * Supprime définitivement une entreprise.
     * @throws EntrepriseException si l'entreprise est introuvable
     */
    boolean supprimer(int id) throws EntrepriseException, DatabaseException;

    /** Retourne toutes les entreprises (validées ou non). */
    List<Entreprise> listerTous();

    /** Retourne uniquement les entreprises validées par l'admin. */
    List<Entreprise> listerValidees();

    /** Retourne les entreprises en attente de validation. */
    List<Entreprise> listerEnAttenteValidation();

    /** Recherche des entreprises par domaine d'activité. */
    List<Entreprise> rechercherParDomaine(String domaine);

    /** Retourne les entreprises validées triées par note décroissante. */
    List<Entreprise> classementParNote();
}
