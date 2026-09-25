package com.gestion.stages.service;

import com.gestion.stages.dao.CandidatureDAO;
import com.gestion.stages.dao.OffreStageDAO;
import com.gestion.stages.exception.CandidatureException;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.OffreStageException;
import com.gestion.stages.model.Candidature;
import com.gestion.stages.model.OffreStage;
import com.gestion.stages.model.enums.StatutCandidature;
import com.gestion.stages.model.enums.StatutOffre;

import java.util.List;

/**
 * Implémentation du service de gestion des candidatures.
 */
public class CandidatureService implements ICandidatureService {

    private final CandidatureDAO candidatureDAO = new CandidatureDAO();
    private final OffreStageDAO  offreDAO       = new OffreStageDAO();

    @Override
    public Candidature postuler(int idEtudiant, int idOffre, String lettreMotivation)
            throws CandidatureException, OffreStageException, DatabaseException {

        // Vérification existence et disponibilité de l'offre
        OffreStage offre;
        try {
            offre = offreDAO.trouverParId(idOffre);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("rechercher offre", e);
        }
        if (offre == null) throw OffreStageException.introuvable(idOffre);
        if (offre.getStatut() != StatutOffre.VALIDEE) {
            throw OffreStageException.nonDisponible(offre.getStatut().getLibelle());
        }
        if (offre.getNombrePlaces() <= 0) {
            throw OffreStageException.plusDePlaces(offre.getTitre());
        }

        // Vérification doublon
        try {
            if (candidatureDAO.candidatureExiste(idEtudiant, idOffre)) {
                throw CandidatureException.doublon(offre.getTitre());
            }
        } catch (CandidatureException ex) {
            throw ex;
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("vérifier doublon candidature", ex);
        }

        Candidature c = new Candidature();
        c.setIdEtudiant(idEtudiant);
        c.setIdOffre(idOffre);
        c.setLettreMotivation(lettreMotivation);
        try {
            return candidatureDAO.creer(c);
        } catch (Exception e) {
            throw DatabaseException.enregistrementEchoue("Candidature", e);
        }
    }

    @Override
    public boolean accepter(int idCandidature, String commentaire)
            throws CandidatureException, DatabaseException {
        Candidature c = trouverParId(idCandidature);
        verifierStatutModifiable(c);
        try {
            return candidatureDAO.changerStatut(idCandidature, StatutCandidature.ACCEPTEE, commentaire);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("accepter candidature", e);
        }
    }

    @Override
    public boolean refuser(int idCandidature, String commentaire)
            throws CandidatureException, DatabaseException {
        Candidature c = trouverParId(idCandidature);
        verifierStatutModifiable(c);
        try {
            return candidatureDAO.changerStatut(idCandidature, StatutCandidature.REFUSEE, commentaire);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("refuser candidature", e);
        }
    }

    @Override
    public boolean annuler(int idCandidature) throws CandidatureException, DatabaseException {
        Candidature c = trouverParId(idCandidature);
        if (c.getStatut() != StatutCandidature.EN_ATTENTE) {
            throw CandidatureException.annulationInterdite(c.getStatut().getLibelle());
        }
        try {
            return candidatureDAO.changerStatut(idCandidature, StatutCandidature.ANNULEE, "Annulée par l'étudiant");
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("annuler candidature", e);
        }
    }

    @Override
    public boolean supprimer(int id) throws CandidatureException, DatabaseException {
        trouverParId(id);
        try {
            return candidatureDAO.supprimer(id);
        } catch (Exception e) {
            throw DatabaseException.suppressionEchouee("Candidature", id, e);
        }
    }

    @Override
    public Candidature trouverParId(int id) throws CandidatureException {
        try {
            Candidature c = candidatureDAO.trouverParId(id);
            if (c == null) throw CandidatureException.introuvable(id);
            return c;
        } catch (CandidatureException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CandidatureException(CandidatureException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche de la candidature ID=" + id, ex);
        }
    }

    @Override
    public List<Candidature> listerParEtudiant(int idEtudiant) {
        return candidatureDAO.listerParEtudiant(idEtudiant);
    }

    @Override
    public List<Candidature> listerParOffre(int idOffre) {
        return candidatureDAO.listerParOffre(idOffre);
    }

    @Override
    public List<Candidature> listerTous() {
        return candidatureDAO.listerTous();
    }

    // ===== Validations privées =====

    private void verifierStatutModifiable(Candidature c) throws CandidatureException {
        if (c.getStatut() != StatutCandidature.EN_ATTENTE) {
            throw CandidatureException.dejaTraitee(c.getIdCandidature(), c.getStatut().getLibelle());
        }
    }
}
