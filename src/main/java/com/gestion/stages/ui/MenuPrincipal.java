package com.gestion.stages.ui;

import com.gestion.stages.dao.EncadrantDAO;
import com.gestion.stages.dao.EntrepriseDAO;
import com.gestion.stages.exception.AuthException;
import com.gestion.stages.model.Encadrant;
import com.gestion.stages.model.Entreprise;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.model.enums.Role;
import com.gestion.stages.service.AuthService;
import com.gestion.stages.service.IAuthService;
import com.gestion.stages.service.IOffreStageService;
import com.gestion.stages.service.OffreStageService;
import com.gestion.stages.util.ConsoleUtils;

public class MenuPrincipal {

    private final IAuthService      authService  = new AuthService();
    private final IOffreStageService offreService = new OffreStageService();
    private final EntrepriseDAO     entrepriseDAO = new EntrepriseDAO();
    private final EncadrantDAO      encadrantDAO  = new EncadrantDAO();

    public void demarrer() {
        try {
            int expires = offreService.expireOffresObsoletes();
            if (expires > 0) ConsoleUtils.afficherInfo(expires + " offre(s) expirée(s) automatiquement.");
        } catch (Exception ignored) {}

        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.viderEcran();
            afficherBanniereAccueil();
            System.out.println("  1. Se connecter");
            System.out.println("  0. Quitter");
            ConsoleUtils.afficherSeparateur();

            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 1);
            if (choix == 1) login();
            else continuer = false;
        }
        System.out.println(ConsoleUtils.CYAN + "\nMerci d'avoir utilisé le système. Au revoir !" + ConsoleUtils.RESET);
    }

    private void login() {
        ConsoleUtils.afficherSousTitre("Authentification");
        String email     = ConsoleUtils.lireChaineMandatoire("Email : ");
        String motDePasse = ConsoleUtils.lireMotDePasse("Mot de passe : ");

        try {
            Utilisateur u = authService.connecter(email, motDePasse);
            ConsoleUtils.afficherSucces("Bienvenue, " + u.getNomComplet() + " (" + u.getRole().getLibelle() + ")");
            ConsoleUtils.attendreTouche();
            ouvrirMenu(u);
            authService.deconnecter();
        } catch (AuthException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
            ConsoleUtils.attendreTouche();
        }
    }

    private void ouvrirMenu(Utilisateur u) {
        if (u.getRole() == Role.ADMIN) {
            new MenuAdmin().afficher();
        } else if (u.getRole() == Role.ETUDIANT) {
            new MenuEtudiant(u).afficher();
        } else if (u.getRole() == Role.ENTREPRISE) {
            Entreprise entreprise = entrepriseDAO.trouverParUtilisateur(u.getId());
            new MenuEntreprise(u, entreprise).afficher();
        } else if (u.getRole() == Role.ENCADRANT) {
            Encadrant encadrant = encadrantDAO.trouverParUtilisateur(u.getId());
            new MenuEncadrant(u, encadrant).afficher();
        } else {
            ConsoleUtils.afficherErreur("Rôle non reconnu : " + u.getRole());
        }
    }

    private void afficherBanniereAccueil() {
        System.out.println(ConsoleUtils.BOLD + ConsoleUtils.BLUE);
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     SYSTÈME DE GESTION DES STAGES UNIVERSITAIRES     ║");
        System.out.println("║          Java Console Application — v2.0             ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println(ConsoleUtils.RESET);
    }
}
