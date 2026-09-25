package com.gestion.stages.service;

import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.EtudiantException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Etudiant;
import com.gestion.stages.model.OffreStage;
import com.gestion.stages.model.enums.NiveauEtude;
import com.gestion.stages.model.enums.StatutStage;

import java.util.List;

/**
 * Interface du service de gestion des étudiants.
 * Couvre le profil, la recherche, le classement et les suggestions d'offres.
 */
public interface IEtudiantService {

    /**
     * Recherche un étudiant par son identifiant.
     * @throws EtudiantException si l'étudiant est introuvable
     */
    Etudiant trouverParId(int id) throws EtudiantException;

    /**
     * Recherche le profil étudiant associé à un compte utilisateur.
     * @throws EtudiantException si aucun profil n'est trouvé
     */
    Etudiant trouverParUtilisateur(int idUtilisateur) throws EtudiantException;

    /**
     * Met à jour le profil d'un étudiant.
     * @throws EtudiantException   si l'étudiant est introuvable
     * @throws ValidationException si les données sont invalides (ex: moyenne hors plage)
     * @throws DatabaseException   en cas d'erreur base de données
     */
    boolean modifier(Etudiant e) throws EtudiantException, ValidationException, DatabaseException;

    /**
     * Supprime définitivement un étudiant.
     * @throws EtudiantException si l'étudiant est introuvable
     */
    boolean supprimer(int id) throws EtudiantException, DatabaseException;

    /** Retourne la liste complète de tous les étudiants. */
    List<Etudiant> listerTous();

    /** Recherche des étudiants par spécialité (recherche partielle). */
    List<Etudiant> rechercherParSpecialite(String specialite);

    /** Retourne les étudiants d'un niveau d'étude donné. */
    List<Etudiant> rechercherParNiveau(NiveauEtude niveau);

    /** Filtre les étudiants selon leur statut de stage. */
    List<Etudiant> rechercherParStatut(StatutStage statut);

    /** Recherche globale sur nom, prénom, matricule et spécialité. */
    List<Etudiant> rechercherGlobal(String terme);

    /** Retourne tous les étudiants triés par moyenne décroissante. */
    List<Etudiant> classementParMoyenne();

    /**
     * Suggère des offres de stage pertinentes pour un étudiant
     * en fonction de sa spécialité et de ses compétences.
     */
    List<OffreStage> suggererOffres(Etudiant etudiant);

    /**
     * Met à jour le statut de stage d'un étudiant.
     * @throws EtudiantException si l'étudiant est introuvable
     */
    boolean changerStatut(int idEtudiant, StatutStage statut) throws EtudiantException, DatabaseException;
}
