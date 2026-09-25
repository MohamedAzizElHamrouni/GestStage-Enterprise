package com.gestion.stages.service;

import com.gestion.stages.dao.EncadrantDAO;
import com.gestion.stages.dao.EntrepriseDAO;
import com.gestion.stages.dao.EtudiantDAO;
import com.gestion.stages.dao.UtilisateurDAO;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.UtilisateurException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Encadrant;
import com.gestion.stages.model.Entreprise;
import com.gestion.stages.model.Etudiant;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.model.enums.Role;
import com.gestion.stages.util.PasswordUtils;

import java.util.List;

/**
 * Implémentation du service de gestion des utilisateurs.
 */
public class UtilisateurService implements IUtilisateurService {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final EtudiantDAO    etudiantDAO    = new EtudiantDAO();
    private final EntrepriseDAO  entrepriseDAO  = new EntrepriseDAO();
    private final EncadrantDAO   encadrantDAO   = new EncadrantDAO();

    @Override
    public Utilisateur creerUtilisateur(Utilisateur u)
            throws UtilisateurException, ValidationException, DatabaseException {
        validerEmail(u.getEmail());

        try {
            if (utilisateurDAO.emailExiste(u.getEmail())) {
                throw UtilisateurException.emailDejaUtilise(u.getEmail());
            }
            return utilisateurDAO.creer(u);
        } catch (UtilisateurException e) {
            throw e;
        } catch (Exception e) {
            throw DatabaseException.enregistrementEchoue("Utilisateur", e);
        }
    }

    @Override
    public Utilisateur creerEtudiantComplet(Utilisateur u, Etudiant etudiant)
            throws UtilisateurException, ValidationException, DatabaseException {
        u.setRole(Role.ETUDIANT);
        creerUtilisateur(u);
        try {
            etudiant.setIdUtilisateur(u.getId());
            etudiantDAO.creer(etudiant);
        } catch (Exception e) {
            throw DatabaseException.enregistrementEchoue("Etudiant", e);
        }
        return u;
    }

    @Override
    public Utilisateur creerEntrepriseComplete(Utilisateur u, Entreprise entreprise)
            throws UtilisateurException, ValidationException, DatabaseException {
        u.setRole(Role.ENTREPRISE);
        creerUtilisateur(u);
        try {
            entreprise.setIdUtilisateur(u.getId());
            entrepriseDAO.creer(entreprise);
        } catch (Exception e) {
            throw DatabaseException.enregistrementEchoue("Entreprise", e);
        }
        return u;
    }

    @Override
    public Utilisateur creerEncadrantComplet(Utilisateur u, Encadrant encadrant)
            throws UtilisateurException, ValidationException, DatabaseException {
        u.setRole(Role.ENCADRANT);
        creerUtilisateur(u);
        try {
            encadrant.setIdUtilisateur(u.getId());
            encadrantDAO.creer(encadrant);
        } catch (Exception e) {
            throw DatabaseException.enregistrementEchoue("Encadrant", e);
        }
        return u;
    }

    @Override
    public boolean modifierUtilisateur(Utilisateur u)
            throws UtilisateurException, DatabaseException {
        trouverParId(u.getId());
        try {
            return utilisateurDAO.modifier(u);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("modifier utilisateur", e);
        }
    }

    @Override
    public boolean changerMotDePasse(int id, String ancienMdp, String nouveauMdp)
            throws UtilisateurException, ValidationException, DatabaseException {
        Utilisateur u = trouverParId(id);
        if (!ancienMdp.equals(u.getMotDePasse())) {
            throw UtilisateurException.motDePasseIncorrect();
        }
        try {
            u.setMotDePasse(nouveauMdp);
            return utilisateurDAO.modifier(u);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("changer mot de passe", e);
        }
    }

    @Override
    public boolean desactiverUtilisateur(int id)
            throws UtilisateurException, DatabaseException {
        trouverParId(id);
        try {
            return utilisateurDAO.desactiver(id);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("désactiver utilisateur", e);
        }
    }

    @Override
    public boolean supprimerUtilisateur(int id)
            throws UtilisateurException, DatabaseException {
        trouverParId(id);
        try {
            return utilisateurDAO.supprimer(id);
        } catch (Exception e) {
            throw DatabaseException.suppressionEchouee("Utilisateur", id, e);
        }
    }

    @Override
    public Utilisateur trouverParId(int id) throws UtilisateurException {
        try {
            Utilisateur u = utilisateurDAO.trouverParId(id);
            if (u == null) throw UtilisateurException.introuvable(id);
            return u;
        } catch (UtilisateurException e) {
            throw e;
        } catch (Exception e) {
            throw new UtilisateurException(UtilisateurException.CODE_INTROUVABLE,
                    "Erreur lors de la recherche de l'utilisateur ID=" + id, e);
        }
    }

    @Override
    public List<Utilisateur> listerTous() {
        return utilisateurDAO.listerTous();
    }

    @Override
    public List<Utilisateur> listerParRole(Role role) {
        return utilisateurDAO.listerParRole(role);
    }

    @Override
    public List<Utilisateur> rechercher(String terme) {
        return utilisateurDAO.rechercher(terme);
    }

    @Override
    public String reinitialiserMotDePasse(int id)
            throws UtilisateurException, DatabaseException {
        Utilisateur u = trouverParId(id);
        String mdpTemp = PasswordUtils.genererMotDePasseTemporaire();
        try {
            u.setMotDePasse(mdpTemp);
            utilisateurDAO.modifier(u);
        } catch (Exception e) {
            throw DatabaseException.requeteEchouee("réinitialiser mot de passe", e);
        }
        return mdpTemp;
    }

    // ===== Validations privées =====

    private void validerEmail(String email) throws ValidationException {
        if (email == null || email.isBlank()) {
            throw ValidationException.champVide("email");
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]{2,}$")) {
            throw ValidationException.formatEmailInvalide(email);
        }
    }
}
