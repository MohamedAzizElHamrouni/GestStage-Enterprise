package com.gestion.stages.service;

import com.gestion.stages.exception.CandidatureException;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.OffreStageException;
import com.gestion.stages.model.Candidature;

import java.util.List;

/**
 * Interface du service de gestion des candidatures.
 * Couvre la soumission, le traitement (accepter/refuser) et le suivi.
 */
public interface ICandidatureService {

    /**
     * Permet à un étudiant de postuler à une offre de stage.
     * @throws CandidatureException si l'étudiant a déjà postulé à cette offre
     * @throws OffreStageException  si l'offre n'existe pas ou n'est pas disponible
     * @throws DatabaseException    en cas d'erreur base de données
     */
    Candidature postuler(int idEtudiant, int idOffre, String lettreMotivation)
            throws CandidatureException, OffreStageException, DatabaseException;

    /**
     * Accepte une candidature avec un commentaire optionnel.
     * @throws CandidatureException si la candidature est introuvable ou déjà traitée
     */
    boolean accepter(int idCandidature, String commentaire)
            throws CandidatureException, DatabaseException;

    /**
     * Refuse une candidature avec un commentaire optionnel.
     * @throws CandidatureException si la candidature est introuvable ou déjà traitée
     */
    boolean refuser(int idCandidature, String commentaire)
            throws CandidatureException, DatabaseException;

    /**
     * Permet à un étudiant d'annuler sa candidature (uniquement si encore EN_ATTENTE).
     * @throws CandidatureException si l'annulation n'est pas possible dans l'état actuel
     */
    boolean annuler(int idCandidature) throws CandidatureException, DatabaseException;

    /**
     * Supprime définitivement une candidature.
     * @throws CandidatureException si la candidature est introuvable
     */
    boolean supprimer(int id) throws CandidatureException, DatabaseException;

    /**
     * Recherche une candidature par son identifiant.
     * @throws CandidatureException si la candidature est introuvable
     */
    Candidature trouverParId(int id) throws CandidatureException;

    /** Retourne toutes les candidatures d'un étudiant donné. */
    List<Candidature> listerParEtudiant(int idEtudiant);

    /** Retourne toutes les candidatures reçues pour une offre donnée. */
    List<Candidature> listerParOffre(int idOffre);

    /** Retourne toutes les candidatures (vue admin). */
    List<Candidature> listerTous();
}
