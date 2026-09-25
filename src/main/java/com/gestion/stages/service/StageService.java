package com.gestion.stages.service;

import com.gestion.stages.dao.EtudiantDAO;
import com.gestion.stages.dao.StageDAO;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.StageException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Etudiant;
import com.gestion.stages.model.Stage;
import com.gestion.stages.model.enums.StatutStage;

import java.util.List;

/**
 * Implémentation du service de suivi des stages.
 */
public class StageService implements IStageService {

    private final StageDAO    stageDAO    = new StageDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();

    @Override
    public Stage creer(Stage stage)
            throws StageException, ValidationException, DatabaseException {
        validerDates(stage);

        // Vérifier que l'étudiant n'est pas déjà en stage
        Stage existant = stageDAO.trouverStageActifEtudiant(stage.getIdEtudiant());
        if (existant != null) {
            Etudiant e = etudiantDAO.trouverParId(stage.getIdEtudiant());
            String nom = e != null ? e.getNomComplet() : "ID#" + stage.getIdEtudiant();
            throw StageException.dejaEnCours(nom);
        }

        try {
            Stage s = stageDAO.creer(stage);
            mettreAJourStatutEtudiant(stage.getIdEtudiant(), StatutStage.EN_COURS);
            return s;
        } catch (Exception ex) {
            throw DatabaseException.enregistrementEchoue("Stage", ex);
        }
    }

    @Override
    public boolean modifier(Stage stage)
            throws StageException, ValidationException, DatabaseException {
        trouverParId(stage.getIdStage());
        validerDates(stage);
        try {
            return stageDAO.modifier(stage);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("modifier stage", e);
        }
    }

    @Override
    public boolean terminer(int idStage) throws StageException, DatabaseException {
        Stage s = trouverParId(idStage);
        if (s.getStatut() != StatutStage.EN_COURS) {
            throw StageException.statutInvalide("terminer", s.getStatut().getLibelle());
        }
        try {
            boolean ok = stageDAO.changerStatut(idStage, StatutStage.TERMINE);
            if (ok) mettreAJourStatutEtudiant(s.getIdEtudiant(), StatutStage.TERMINE);
            return ok;
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("terminer stage", ex);
        }
    }

    @Override
    public boolean valider(int idStage) throws StageException, DatabaseException {
        Stage s = trouverParId(idStage);
        if (s.getStatut() != StatutStage.TERMINE) {
            throw StageException.statutInvalide("valider", s.getStatut().getLibelle());
        }
        try {
            boolean ok = stageDAO.changerStatut(idStage, StatutStage.VALIDE);
            if (ok) mettreAJourStatutEtudiant(s.getIdEtudiant(), StatutStage.VALIDE);
            return ok;
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("valider stage", ex);
        }
    }

    @Override
    public boolean noter(int idStage, double noteEncadrant, double noteEntreprise,
                         String commentaireEncadrant, String commentaireEntreprise)
            throws StageException, ValidationException, DatabaseException {
        Stage s = trouverParId(idStage);
        validerNote("noteEncadrant", noteEncadrant);
        validerNote("noteEntreprise", noteEntreprise);
        s.setNoteEncadrant(noteEncadrant);
        s.setNoteEntreprise(noteEntreprise);
        s.setCommentaireEncadrant(commentaireEncadrant);
        s.setCommentaireEntreprise(commentaireEntreprise);
        try {
            return stageDAO.modifier(s);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("noter stage", e);
        }
    }

    @Override
    public boolean soumettreRapport(int idStage, String rapport)
            throws StageException, DatabaseException {
        Stage s = trouverParId(idStage);
        if (rapport == null || rapport.isBlank()) {
            throw new StageException(StageException.CODE_RAPPORT_MANQUANT,
                    "Le contenu du rapport ne peut pas être vide.");
        }
        s.setRapport(rapport);
        try {
            return stageDAO.modifier(s);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("soumettre rapport", e);
        }
    }

    @Override
    public boolean marquerAttestationGeneree(int idStage)
            throws StageException, DatabaseException {
        Stage s = trouverParId(idStage);
        if (s.getStatut() != StatutStage.VALIDE && s.getStatut() != StatutStage.TERMINE) {
            throw StageException.attestationInterdite(s.getStatut().getLibelle());
        }
        s.setAttestationGeneree(true);
        try {
            return stageDAO.modifier(s);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("marquer attestation", e);
        }
    }

    @Override
    public boolean supprimer(int id) throws StageException, DatabaseException {
        trouverParId(id);
        try {
            return stageDAO.supprimer(id);
        } catch (Exception e) {
            throw DatabaseException.suppressionEchouee("Stage", id, e);
        }
    }

    @Override
    public Stage trouverParId(int id) throws StageException {
        try {
            Stage s = stageDAO.trouverParId(id);
            if (s == null) throw StageException.introuvable(id);
            return s;
        } catch (StageException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new StageException(StageException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche du stage ID=" + id, ex);
        }
    }

    @Override
    public Stage trouverStageActifEtudiant(int idEtudiant) {
        try {
            return stageDAO.trouverStageActifEtudiant(idEtudiant);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public List<Stage> listerTous() {
        return stageDAO.listerTous();
    }

    @Override
    public List<Stage> listerParEtudiant(int idEtudiant) {
        return stageDAO.listerParEtudiant(idEtudiant);
    }

    @Override
    public List<Stage> listerParEncadrant(int idEncadrant) {
        return stageDAO.listerParEncadrant(idEncadrant);
    }

    @Override
    public List<Stage> listerParStatut(StatutStage statut) {
        return stageDAO.listerParStatut(statut);
    }

    @Override
    public void afficherStatistiques() {
        System.out.println("  Stages en cours   : " + stageDAO.compterParStatut(StatutStage.EN_COURS));
        System.out.println("  Stages terminés   : " + stageDAO.compterParStatut(StatutStage.TERMINE));
        System.out.println("  Stages validés    : " + stageDAO.compterParStatut(StatutStage.VALIDE));
        System.out.println("  Stages abandonnés : " + stageDAO.compterParStatut(StatutStage.ABANDONNE));
    }

    // ===== Validations et helpers privés =====

    private void validerDates(Stage stage) throws ValidationException {
        if (stage.getDateDebut() != null && stage.getDateFin() != null
                && stage.getDateDebut().isAfter(stage.getDateFin())) {
            throw ValidationException.dateDebutApresDateFin();
        }
    }

    private void validerNote(String champ, double note) throws ValidationException {
        if (note < 0 || note > 20) {
            throw ValidationException.valeurHorsPlage(champ, 0, 20);
        }
    }

    private void mettreAJourStatutEtudiant(int idEtudiant, StatutStage statut) {
        try {
            Etudiant e = etudiantDAO.trouverParId(idEtudiant);
            if (e != null) { e.setStatutStage(statut); etudiantDAO.modifier(e); }
        } catch (Exception ignored) {}
    }
}
