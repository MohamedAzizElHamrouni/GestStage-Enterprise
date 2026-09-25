package com.gestion.stages.ui;

import com.gestion.stages.exception.*;
import com.gestion.stages.model.*;
import com.gestion.stages.model.enums.StatutStage;
import com.gestion.stages.service.*;
import com.gestion.stages.util.ConsoleUtils;

import java.util.List;

public class MenuEntreprise {

    private final IOffreStageService  offreService       = new OffreStageService();
    private final ICandidatureService candidatureService = new CandidatureService();
    private final IEtudiantService    etudiantService    = new EtudiantService();

    private final Utilisateur utilisateur;
    private final Entreprise  entreprise;

    public MenuEntreprise(Utilisateur utilisateur, Entreprise entreprise) {
        this.utilisateur = utilisateur;
        this.entreprise  = entreprise;
    }

    public void afficher() {
        if (entreprise == null) {
            ConsoleUtils.afficherErreur("Profil entreprise introuvable. Contactez l'administration.");
            ConsoleUtils.attendreTouche();
            return;
        }
        if (!entreprise.isValide()) {
            ConsoleUtils.afficherInfo("Votre compte est en attente de validation par l'administration.");
            ConsoleUtils.attendreTouche();
            return;
        }
        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.viderEcran();
            ConsoleUtils.afficherTitre("ESPACE ENTREPRISE — " + entreprise.getNom());
            System.out.println("  1. Mon profil entreprise");
            System.out.println("  2. Publier une offre de stage");
            System.out.println("  3. Mes offres de stage");
            System.out.println("  4. Candidatures reçues");
            System.out.println("  5. Gérer une candidature");
            System.out.println("  6. Étudiants disponibles");
            System.out.println("  0. Déconnexion");
            ConsoleUtils.afficherSeparateur();
            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 6);
            switch (choix) {
                case 1: afficherProfil(); break;
                case 2: publierOffre(); break;
                case 3: voirOffres(); break;
                case 4: voirCandidatures(); break;
                case 5: gererCandidature(); break;
                case 6: voirEtudiants(); break;
                case 0: continuer = false; break;
            }
        }
    }

    private void afficherProfil() {
        ConsoleUtils.afficherTitre("MON PROFIL ENTREPRISE");
        System.out.println("ID          : " + entreprise.getIdEntreprise());
        System.out.println("Nom         : " + entreprise.getNom());
        System.out.println("Domaine     : " + entreprise.getDomaine());
        System.out.println("Adresse     : " + entreprise.getAdresse());
        System.out.println("Téléphone   : " + entreprise.getTelephone());
        System.out.println("Email       : " + entreprise.getEmail());
        System.out.println("Responsable : " + entreprise.getResponsableRH());
        System.out.println("Note        : " + String.format("%.1f/5", entreprise.getNote()));
        System.out.println("Description : " + entreprise.getDescription());
        ConsoleUtils.attendreTouche();
    }

    private void publierOffre() {
        ConsoleUtils.afficherSousTitre("Publier une offre de stage");
        OffreStage offre = new OffreStage();
        offre.setIdEntreprise(entreprise.getIdEntreprise());
        offre.setTitre(ConsoleUtils.lireChaineMandatoire("Titre de l'offre : "));
        offre.setDescription(ConsoleUtils.lireChaineMandatoire("Description : "));
        offre.setDomaine(ConsoleUtils.lireChaineMandatoire("Domaine : "));
        offre.setDureeEnMois(ConsoleUtils.lireEntier("Durée (mois) : "));
        offre.setType(ConsoleUtils.lireChaine("Type (PFE / stage d'été / obligatoire) : "));
        offre.setDateDebut(ConsoleUtils.lireDate("Date de début"));
        offre.setDateFin(ConsoleUtils.lireDate("Date de fin"));
        offre.setDateLimite(ConsoleUtils.lireDate("Date limite de candidature"));
        offre.setRemuneration(ConsoleUtils.lireDouble("Rémunération mensuelle (DA, 0 si non rémunéré) : "));
        offre.setCompetencesRequises(ConsoleUtils.lireChaine("Compétences requises (virgule) : "));
        offre.setNiveauRequis(ConsoleUtils.lireChaine("Niveau requis (ex: M1, M2) : "));
        offre.setNombrePlaces(ConsoleUtils.lireEntier("Nombre de places : "));
        try {
            offreService.publier(offre);
            ConsoleUtils.afficherSucces("Offre publiée (ID: " + offre.getIdOffre() + "). En attente de validation admin.");
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation - " + e.getChamp() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void voirOffres() {
        ConsoleUtils.afficherSousTitre("Mes offres de stage");
        List<OffreStage> offres = offreService.listerParEntreprise(entreprise.getIdEntreprise());
        if (offres.isEmpty()) ConsoleUtils.afficherInfo("Aucune offre publiée.");
        offres.forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }

    private void voirCandidatures() {
        ConsoleUtils.afficherSousTitre("Candidatures reçues");
        List<OffreStage> offres = offreService.listerParEntreprise(entreprise.getIdEntreprise());
        for (OffreStage offre : offres) {
            List<Candidature> candidatures = candidatureService.listerParOffre(offre.getIdOffre());
            if (!candidatures.isEmpty()) {
                System.out.println("\nOffre : " + offre.getTitre() + " (" + candidatures.size() + " candidature(s))");
                candidatures.forEach(System.out::println);
            }
        }
        ConsoleUtils.attendreTouche();
    }

    private void gererCandidature() {
        int id = ConsoleUtils.lireEntier("ID de la candidature à traiter : ");
        try {
            Candidature c = candidatureService.trouverParId(id);
            System.out.println(c);
            System.out.println("  1. Accepter   2. Refuser   0. Annuler");
            int choix = ConsoleUtils.lireEntierEntre("Choix : ", 0, 2);
            if (choix == 0) return;
            String commentaire = ConsoleUtils.lireChaine("Commentaire (optionnel) : ");
            if (choix == 1) {
                candidatureService.accepter(id, commentaire);
                ConsoleUtils.afficherSucces("Candidature acceptée.");
            } else {
                candidatureService.refuser(id, commentaire);
                ConsoleUtils.afficherSucces("Candidature refusée.");
            }
        } catch (CandidatureException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void voirEtudiants() {
        ConsoleUtils.afficherSousTitre("Étudiants disponibles (sans stage)");
        etudiantService.rechercherParStatut(StatutStage.SANS_STAGE).forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }
}
