package com.gestion.stages.service;

import com.gestion.stages.dao.OffreStageDAO;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.OffreStageException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.OffreStage;
import com.gestion.stages.model.enums.StatutOffre;

import java.time.LocalDate;
import java.util.List;

/**
 * Implémentation du service de gestion des offres de stage.
 */
public class OffreStageService implements IOffreStageService {

    private final OffreStageDAO offreDAO = new OffreStageDAO();

    @Override
    public OffreStage publier(OffreStage offre)
            throws ValidationException, DatabaseException {
        validerOffre(offre);
        offre.setStatut(StatutOffre.EN_ATTENTE);
        offre.setDatePublication(LocalDate.now());
        try {
            return offreDAO.creer(offre);
        } catch (Exception e) {
            throw DatabaseException.enregistrementEchoue("OffreStage", e);
        }
    }

    @Override
    public boolean modifier(OffreStage offre)
            throws OffreStageException, ValidationException, DatabaseException {
        trouverParId(offre.getIdOffre());
        validerOffre(offre);
        try {
            return offreDAO.modifier(offre);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("modifier offre", e);
        }
    }

    @Override
    public boolean valider(int id) throws OffreStageException, DatabaseException {
        OffreStage offre = trouverParId(id);
        if (offre.getStatut() == StatutOffre.VALIDEE) {
            throw OffreStageException.dejaValidee(id);
        }
        try {
            return offreDAO.changerStatut(id, StatutOffre.VALIDEE);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("valider offre", e);
        }
    }

    @Override
    public boolean rejeter(int id) throws OffreStageException, DatabaseException {
        trouverParId(id);
        try {
            return offreDAO.changerStatut(id, StatutOffre.REJETEE);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("rejeter offre", e);
        }
    }

    @Override
    public boolean expirer(int id) throws OffreStageException, DatabaseException {
        trouverParId(id);
        try {
            return offreDAO.changerStatut(id, StatutOffre.EXPIREE);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("expirer offre", e);
        }
    }

    @Override
    public boolean supprimer(int id) throws OffreStageException, DatabaseException {
        trouverParId(id);
        try {
            return offreDAO.supprimer(id);
        } catch (Exception e) {
            throw DatabaseException.suppressionEchouee("OffreStage", id, e);
        }
    }

    @Override
    public OffreStage trouverParId(int id) throws OffreStageException {
        try {
            OffreStage offre = offreDAO.trouverParId(id);
            if (offre == null) throw OffreStageException.introuvable(id);
            return offre;
        } catch (OffreStageException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OffreStageException(OffreStageException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche de l'offre ID=" + id, ex);
        }
    }

    @Override
    public List<OffreStage> listerTous() {
        return offreDAO.listerTous();
    }

    @Override
    public List<OffreStage> listerValidees() {
        return offreDAO.listerValidees();
    }

    @Override
    public List<OffreStage> listerParEntreprise(int idEntreprise) {
        return offreDAO.listerParEntreprise(idEntreprise);
    }

    @Override
    public List<OffreStage> listerEnAttenteValidation() {
        return offreDAO.listerEnAttenteValidation();
    }

    @Override
    public List<OffreStage> rechercherParDomaine(String domaine) {
        return offreDAO.rechercherParDomaine(domaine);
    }

    @Override
    public List<OffreStage> rechercherGlobal(String terme) {
        return offreDAO.rechercherGlobal(terme);
    }

    @Override
    public int expireOffresObsoletes() {
        List<OffreStage> offres = offreDAO.listerValidees();
        int count = 0;
        LocalDate aujourdhui = LocalDate.now();
        for (OffreStage o : offres) {
            if (o.getDateLimite() != null && o.getDateLimite().isBefore(aujourdhui)) {
                try {
                    offreDAO.changerStatut(o.getIdOffre(), StatutOffre.EXPIREE);
                    count++;
                } catch (Exception ignored) {}
            }
        }
        return count;
    }

    // ===== Validations privées =====

    private void validerOffre(OffreStage offre) throws ValidationException {
        if (offre.getTitre() == null || offre.getTitre().isBlank()) {
            throw ValidationException.champVide("titre");
        }
        if (offre.getDureeEnMois() <= 0) {
            throw ValidationException.valeurHorsPlage("duréeEnMois", 1, 24);
        }
        if (offre.getNombrePlaces() <= 0) {
            throw ValidationException.valeurHorsPlage("nombrePlaces", 1, 100);
        }
        if (offre.getDateDebut() != null && offre.getDateFin() != null
                && offre.getDateDebut().isAfter(offre.getDateFin())) {
            throw ValidationException.dateDebutApresDateFin();
        }
    }
}
