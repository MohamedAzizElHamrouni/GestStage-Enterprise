package com.gestion.stages.service;

import com.gestion.stages.dao.EntrepriseDAO;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.EntrepriseException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Entreprise;

import java.util.List;

/**
 * Implémentation du service de gestion des entreprises.
 */
public class EntrepriseService implements IEntrepriseService {

    private final EntrepriseDAO entrepriseDAO = new EntrepriseDAO();

    @Override
    public Entreprise trouverParId(int id) throws EntrepriseException {
        try {
            Entreprise en = entrepriseDAO.trouverParId(id);
            if (en == null) throw EntrepriseException.introuvable(id);
            return en;
        } catch (EntrepriseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EntrepriseException(EntrepriseException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche de l'entreprise ID=" + id, ex);
        }
    }

    @Override
    public Entreprise trouverParUtilisateur(int idUtilisateur) throws EntrepriseException {
        try {
            Entreprise en = entrepriseDAO.trouverParUtilisateur(idUtilisateur);
            if (en == null) throw new EntrepriseException(EntrepriseException.CODE_INTROUVABLE,
                    "Aucun profil entreprise trouvé pour l'utilisateur ID=" + idUtilisateur);
            return en;
        } catch (EntrepriseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EntrepriseException(EntrepriseException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche du profil entreprise.", ex);
        }
    }

    @Override
    public boolean modifier(Entreprise e)
            throws EntrepriseException, ValidationException, DatabaseException {
        trouverParId(e.getIdEntreprise());
        validerNom(e.getNom());
        try {
            return entrepriseDAO.modifier(e);
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("modifier entreprise", ex);
        }
    }

    @Override
    public boolean valider(int id) throws EntrepriseException, DatabaseException {
        Entreprise en = trouverParId(id);
        if (en.isValide()) throw EntrepriseException.dejaValidee(en.getNom());
        try {
            return entrepriseDAO.valider(id, true);
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("valider entreprise", ex);
        }
    }

    @Override
    public boolean rejeter(int id) throws EntrepriseException, DatabaseException {
        trouverParId(id);
        try {
            return entrepriseDAO.valider(id, false);
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("rejeter entreprise", ex);
        }
    }

    @Override
    public boolean noter(int id, double note)
            throws EntrepriseException, ValidationException, DatabaseException {
        trouverParId(id);
        if (note < 0 || note > 5) throw EntrepriseException.noteInvalide();
        try {
            return entrepriseDAO.noterEntreprise(id, note);
        } catch (Exception ex) {
            throw DatabaseException.requeteEchouee("noter entreprise", ex);
        }
    }

    @Override
    public boolean supprimer(int id) throws EntrepriseException, DatabaseException {
        trouverParId(id);
        try {
            return entrepriseDAO.supprimer(id);
        } catch (Exception ex) {
            throw DatabaseException.suppressionEchouee("Entreprise", id, ex);
        }
    }

    @Override
    public List<Entreprise> listerTous() {
        return entrepriseDAO.listerTous();
    }

    @Override
    public List<Entreprise> listerValidees() {
        return entrepriseDAO.listerValidees();
    }

    @Override
    public List<Entreprise> listerEnAttenteValidation() {
        return entrepriseDAO.listerEnAttenteValidation();
    }

    @Override
    public List<Entreprise> rechercherParDomaine(String domaine) {
        return entrepriseDAO.rechercherParDomaine(domaine);
    }

    @Override
    public List<Entreprise> classementParNote() {
        return entrepriseDAO.classementParNote();
    }

    // ===== Validations privées =====

    private void validerNom(String nom) throws ValidationException {
        if (nom == null || nom.isBlank()) throw ValidationException.champVide("nom");
        if (nom.length() < 2 || nom.length() > 200) {
            throw ValidationException.longueurInvalide("nom", 2, 200);
        }
    }
}
