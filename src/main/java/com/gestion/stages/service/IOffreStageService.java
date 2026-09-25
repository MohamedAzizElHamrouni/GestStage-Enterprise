package com.gestion.stages.service;

import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.OffreStageException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.OffreStage;

import java.util.List;

/**
 * Interface du service de gestion des offres de stage.
 * Couvre la publication, la validation admin, la recherche et l'expiration.
 */
public interface IOffreStageService {

    /**
     * Publie une nouvelle offre de stage (statut initial : EN_ATTENTE).
     * @throws ValidationException si les données de l'offre sont invalides
     * @throws DatabaseException   en cas d'erreur base de données
     */
    OffreStage publier(OffreStage offre) throws ValidationException, DatabaseException;

    /**
     * Modifie une offre de stage existante.
     * @throws OffreStageException si l'offre est introuvable
     * @throws ValidationException si les nouvelles données sont invalides
     */
    boolean modifier(OffreStage offre) throws OffreStageException, ValidationException, DatabaseException;

    /**
     * Valide une offre de stage (rend l'offre visible aux étudiants).
     * @throws OffreStageException si l'offre est introuvable
     */
    boolean valider(int id) throws OffreStageException, DatabaseException;

    /**
     * Rejette une offre de stage soumise par une entreprise.
     * @throws OffreStageException si l'offre est introuvable
     */
    boolean rejeter(int id) throws OffreStageException, DatabaseException;

    /**
     * Marque une offre comme expirée manuellement.
     * @throws OffreStageException si l'offre est introuvable
     */
    boolean expirer(int id) throws OffreStageException, DatabaseException;

    /**
     * Supprime définitivement une offre de stage.
     * @throws OffreStageException si l'offre est introuvable
     */
    boolean supprimer(int id) throws OffreStageException, DatabaseException;

    /**
     * Recherche une offre par son identifiant.
     * @throws OffreStageException si l'offre est introuvable
     */
    OffreStage trouverParId(int id) throws OffreStageException;

    /** Retourne toutes les offres de stage (tous statuts). */
    List<OffreStage> listerTous();

    /** Retourne uniquement les offres validées et non expirées. */
    List<OffreStage> listerValidees();

    /** Retourne toutes les offres publiées par une entreprise donnée. */
    List<OffreStage> listerParEntreprise(int idEntreprise);

    /** Retourne les offres en attente de validation par l'admin. */
    List<OffreStage> listerEnAttenteValidation();

    /** Recherche des offres par domaine d'activité. */
    List<OffreStage> rechercherParDomaine(String domaine);

    /** Recherche globale sur titre, domaine, compétences requises et nom d'entreprise. */
    List<OffreStage> rechercherGlobal(String terme);

    /**
     * Expire automatiquement toutes les offres dont la date limite est dépassée.
     * @return le nombre d'offres expirées
     */
    int expireOffresObsoletes();
}
