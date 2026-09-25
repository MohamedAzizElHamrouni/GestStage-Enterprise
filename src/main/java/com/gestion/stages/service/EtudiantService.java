package com.gestion.stages.service;

import com.gestion.stages.dao.EtudiantDAO;
import com.gestion.stages.dao.OffreStageDAO;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.EtudiantException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Etudiant;
import com.gestion.stages.model.OffreStage;
import com.gestion.stages.model.enums.NiveauEtude;
import com.gestion.stages.model.enums.StatutStage;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de gestion des étudiants.
 */
public class EtudiantService implements IEtudiantService {

    private final EtudiantDAO   etudiantDAO = new EtudiantDAO();
    private final OffreStageDAO offreDAO    = new OffreStageDAO();

    @Override
    public Etudiant trouverParId(int id) throws EtudiantException {
        try {
            Etudiant e = etudiantDAO.trouverParId(id);
            if (e == null) throw EtudiantException.introuvable(id);
            return e;
        } catch (EtudiantException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EtudiantException(EtudiantException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche de l'étudiant ID=" + id, ex);
        }
    }

    @Override
    public Etudiant trouverParUtilisateur(int idUtilisateur) throws EtudiantException {
        try {
            Etudiant e = etudiantDAO.trouverParUtilisateur(idUtilisateur);
            if (e == null) throw new EtudiantException(EtudiantException.CODE_INTROUVABLE,
                    "Aucun profil étudiant trouvé pour l'utilisateur ID=" + idUtilisateur);
            return e;
        } catch (EtudiantException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EtudiantException(EtudiantException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche du profil étudiant.", ex);
        }
    }

    @Override
    public boolean modifier(Etudiant e)
            throws EtudiantException, ValidationException, DatabaseException {
        trouverParId(e.getIdEtudiant());
        validerMoyenne(e.getMoyenne());
        try {
            return etudiantDAO.modifier(e);
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("modifier étudiant", ex);
        }
    }

    @Override
    public boolean supprimer(int id) throws EtudiantException, DatabaseException {
        trouverParId(id);
        try {
            return etudiantDAO.supprimer(id);
        } catch (Exception ex) {
            throw DatabaseException.suppressionEchouee("Etudiant", id, ex);
        }
    }

    @Override
    public List<Etudiant> listerTous() {
        return etudiantDAO.listerTous();
    }

    @Override
    public List<Etudiant> rechercherParSpecialite(String specialite) {
        return etudiantDAO.rechercherParSpecialite(specialite);
    }

    @Override
    public List<Etudiant> rechercherParNiveau(NiveauEtude niveau) {
        return etudiantDAO.rechercherParNiveau(niveau);
    }

    @Override
    public List<Etudiant> rechercherParStatut(StatutStage statut) {
        return etudiantDAO.rechercherParStatut(statut);
    }

    @Override
    public List<Etudiant> rechercherGlobal(String terme) {
        return etudiantDAO.rechercherGlobal(terme);
    }

    @Override
    public List<Etudiant> classementParMoyenne() {
        return etudiantDAO.classementParMoyenne();
    }

    @Override
    public List<OffreStage> suggererOffres(Etudiant etudiant) {
        List<OffreStage> offresValidees = offreDAO.listerValidees();
        List<OffreStage> suggestions    = new ArrayList<>();

        String specialite  = etudiant.getSpecialite()  != null ? etudiant.getSpecialite().toLowerCase()  : "";
        String competences = etudiant.getCompetences() != null ? etudiant.getCompetences().toLowerCase() : "";

        for (OffreStage offre : offresValidees) {
            boolean pertinente = false;

            if (offre.getDomaine() != null) {
                String domaineL = offre.getDomaine().toLowerCase();
                for (String mot : specialite.split(" ")) {
                    if (!mot.isBlank() && domaineL.contains(mot)) { pertinente = true; break; }
                }
            }

            if (!pertinente && offre.getCompetencesRequises() != null) {
                String reqL = offre.getCompetencesRequises().toLowerCase();
                for (String comp : competences.split(",")) {
                    if (!comp.isBlank() && reqL.contains(comp.trim())) { pertinente = true; break; }
                }
            }

            if (pertinente) suggestions.add(offre);
        }

        if (suggestions.isEmpty() && !offresValidees.isEmpty()) {
            return offresValidees.subList(0, Math.min(5, offresValidees.size()));
        }
        return suggestions;
    }

    @Override
    public boolean changerStatut(int idEtudiant, StatutStage statut)
            throws EtudiantException, DatabaseException {
        Etudiant e = trouverParId(idEtudiant);
        e.setStatutStage(statut);
        try {
            return etudiantDAO.modifier(e);
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("changer statut étudiant", ex);
        }
    }

    // ===== Validations privées =====

    private void validerMoyenne(double moyenne) throws ValidationException {
        if (moyenne < 0 || moyenne > 20) {
            throw ValidationException.valeurHorsPlage("moyenne", 0, 20);
        }
    }
}
