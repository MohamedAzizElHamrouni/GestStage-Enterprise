package com.gestion.stages.service;

import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.StageException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Stage;
import com.gestion.stages.model.enums.StatutStage;

import java.util.List;

/**
 * Interface du service de suivi des stages.
 * Couvre la création, l'évaluation, la validation et la génération d'attestations.
 */
public interface IStageService {

    /**
     * Crée un nouveau stage et met à jour le statut de l'étudiant.
     * @throws StageException      si l'étudiant est déjà en stage
     * @throws ValidationException si les dates sont invalides
     * @throws DatabaseException   en cas d'erreur base de données
     */
    Stage creer(Stage stage) throws StageException, ValidationException, DatabaseException;

    /**
     * Modifie les informations d'un stage existant.
     * @throws StageException      si le stage est introuvable
     * @throws ValidationException si les nouvelles données sont invalides
     */
    boolean modifier(Stage stage) throws StageException, ValidationException, DatabaseException;

    /**
     * Marque un stage comme terminé.
     * @throws StageException si le stage est introuvable ou non en cours
     */
    boolean terminer(int idStage) throws StageException, DatabaseException;

    /**
     * Valide définitivement un stage terminé (action admin ou encadrant).
     * @throws StageException si le stage est introuvable ou non terminé
     */
    boolean valider(int idStage) throws StageException, DatabaseException;

    /**
     * Enregistre les notes et commentaires d'évaluation d'un stage.
     * @throws StageException      si le stage est introuvable
     * @throws ValidationException si une note est hors de la plage [0, 20]
     */
    boolean noter(int idStage, double noteEncadrant, double noteEntreprise,
                  String commentaireEncadrant, String commentaireEntreprise)
            throws StageException, ValidationException, DatabaseException;

    /**
     * Enregistre le rapport de stage soumis par l'étudiant.
     * @throws StageException si le stage est introuvable
     */
    boolean soumettreRapport(int idStage, String rapport) throws StageException, DatabaseException;

    /**
     * Marque l'attestation d'un stage comme générée.
     * @throws StageException si le stage est introuvable ou son statut ne permet pas l'attestation
     */
    boolean marquerAttestationGeneree(int idStage) throws StageException, DatabaseException;

    /**
     * Supprime définitivement un stage.
     * @throws StageException si le stage est introuvable
     */
    boolean supprimer(int id) throws StageException, DatabaseException;

    /**
     * Recherche un stage par son identifiant.
     * @throws StageException si le stage est introuvable
     */
    Stage trouverParId(int id) throws StageException;

    /**
     * Recherche le stage en cours d'un étudiant.
     * @return le stage actif, ou null si l'étudiant n'est pas en stage
     */
    Stage trouverStageActifEtudiant(int idEtudiant);

    /** Retourne tous les stages (vue admin). */
    List<Stage> listerTous();

    /** Retourne tous les stages d'un étudiant. */
    List<Stage> listerParEtudiant(int idEtudiant);

    /** Retourne tous les stages suivis par un encadrant. */
    List<Stage> listerParEncadrant(int idEncadrant);

    /** Filtre les stages par statut. */
    List<Stage> listerParStatut(StatutStage statut);

    /** Affiche les statistiques globales sur les stages (console). */
    void afficherStatistiques();
}
