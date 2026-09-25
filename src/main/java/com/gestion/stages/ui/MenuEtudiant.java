package com.gestion.stages.ui;

import com.gestion.stages.exception.*;
import com.gestion.stages.model.*;
import com.gestion.stages.model.enums.NiveauEtude;
import com.gestion.stages.service.*;
import com.gestion.stages.util.ConsoleUtils;
import com.gestion.stages.util.ExportUtils;

import java.util.List;

public class MenuEtudiant {

    private final IEtudiantService    etudiantService    = new EtudiantService();
    private final IOffreStageService  offreService       = new OffreStageService();
    private final ICandidatureService candidatureService = new CandidatureService();
    private final IStageService       stageService       = new StageService();

    private final Utilisateur utilisateur;
    private Etudiant etudiant;

    public MenuEtudiant(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        try {
            this.etudiant = etudiantService.trouverParUtilisateur(utilisateur.getId());
        } catch (EtudiantException e) {
            this.etudiant = null;
        }
    }

    public void afficher() {
        if (etudiant == null) {
            ConsoleUtils.afficherErreur("Profil étudiant introuvable. Contactez l'administration.");
            ConsoleUtils.attendreTouche();
            return;
        }
        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.viderEcran();
            ConsoleUtils.afficherTitre("ESPACE ÉTUDIANT — " + utilisateur.getNomComplet());
            System.out.println("  1. Mon profil");
            System.out.println("  2. Modifier mon profil");
            System.out.println("  3. Offres disponibles");
            System.out.println("  4. Rechercher des offres");
            System.out.println("  5. Offres suggérées pour moi");
            System.out.println("  6. Postuler à une offre");
            System.out.println("  7. Mes candidatures");
            System.out.println("  8. Mon stage en cours");
            System.out.println("  9. Générer mon CV (PDF)");
            System.out.println("  0. Déconnexion");
            ConsoleUtils.afficherSeparateur();
            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 9);
            switch (choix) {
                case 1: afficherProfil(); break;
                case 2: modifierProfil(); break;
                case 3: voirOffres(); break;
                case 4: rechercherOffres(); break;
                case 5: voirSuggestions(); break;
                case 6: postuler(); break;
                case 7: voirCandidatures(); break;
                case 8: voirStage(); break;
                case 9: genererCV(); break;
                case 0: continuer = false; break;
            }
        }
    }

    private void afficherProfil() {
        ConsoleUtils.afficherTitre("MON PROFIL");
        System.out.println("Matricule   : " + etudiant.getMatricule());
        System.out.println("Nom complet : " + etudiant.getNomComplet());
        System.out.println("Email       : " + etudiant.getEmail());
        System.out.println("Téléphone   : " + etudiant.getTelephone());
        System.out.println("Spécialité  : " + etudiant.getSpecialite());
        System.out.println("Niveau      : " + (etudiant.getNiveau() != null ? etudiant.getNiveau().getLibelle() : "N/A"));
        System.out.println("Moyenne     : " + String.format("%.2f/20", etudiant.getMoyenne()));
        System.out.println("Compétences : " + etudiant.getCompetences());
        System.out.println("Statut stage: " + etudiant.getStatutStage().getLibelle());
        System.out.println("Score auto  : " + etudiant.calculerScore() + "/100");
        ConsoleUtils.attendreTouche();
    }

    private void modifierProfil() {
        ConsoleUtils.afficherSousTitre("Modifier mon profil");
        System.out.print("Téléphone (actuel: " + etudiant.getTelephone() + ") : ");
        String tel  = ConsoleUtils.getScanner().nextLine().trim();
        System.out.print("Compétences (actuel: " + etudiant.getCompetences() + ") : ");
        String comp = ConsoleUtils.getScanner().nextLine().trim();
        System.out.print("CV / Expériences : ");
        String cv   = ConsoleUtils.getScanner().nextLine().trim();
        String niv  = ConsoleUtils.lireChaine("Niveau (L1/L2/L3/M1/M2/DOCTORAT) : ").toUpperCase();

        if (!tel.isEmpty())  etudiant.setTelephone(tel);
        if (!comp.isEmpty()) etudiant.setCompetences(comp);
        if (!cv.isEmpty())   etudiant.setCv(cv);
        try { if (!niv.isEmpty()) etudiant.setNiveau(NiveauEtude.valueOf(niv)); } catch (Exception ignored) {}

        try {
            etudiantService.modifier(etudiant);
            ConsoleUtils.afficherSucces("Profil mis à jour avec succès.");
        } catch (EtudiantException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation - " + e.getChamp() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void voirOffres() {
        ConsoleUtils.afficherSousTitre("Offres de stage disponibles");
        List<OffreStage> offres = offreService.listerValidees();
        if (offres.isEmpty()) { ConsoleUtils.afficherInfo("Aucune offre disponible."); }
        offres.forEach(o -> {
            System.out.println(o);
            System.out.println("    Compétences requises : " + o.getCompetencesRequises());
        });
        ConsoleUtils.attendreTouche();
    }

    private void rechercherOffres() {
        String terme = ConsoleUtils.lireChaineMandatoire("Rechercher (titre, domaine, compétences, entreprise) : ");
        List<OffreStage> offres = offreService.rechercherGlobal(terme);
        ConsoleUtils.afficherInfo(offres.size() + " résultat(s)");
        offres.forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }

    private void voirSuggestions() {
        ConsoleUtils.afficherSousTitre("Offres suggérées pour votre profil");
        List<OffreStage> suggestions = etudiantService.suggererOffres(etudiant);
        if (suggestions.isEmpty()) ConsoleUtils.afficherInfo("Aucune suggestion disponible.");
        else suggestions.forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }

    private void postuler() {
        ConsoleUtils.afficherSousTitre("Postuler à une offre");
        int idOffre = ConsoleUtils.lireEntier("ID de l'offre : ");
        try {
            OffreStage offre = offreService.trouverParId(idOffre);
            System.out.println("Offre : " + offre);
            System.out.println("\nRédigez votre lettre de motivation :");
            String lettre = ConsoleUtils.getScanner().nextLine().trim();
            candidatureService.postuler(etudiant.getIdEtudiant(), idOffre, lettre);
            ConsoleUtils.afficherSucces("Candidature envoyée avec succès !");
        } catch (OffreStageException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (CandidatureException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void voirCandidatures() {
        ConsoleUtils.afficherSousTitre("Mes candidatures");
        List<Candidature> candidatures = candidatureService.listerParEtudiant(etudiant.getIdEtudiant());
        if (candidatures.isEmpty()) {
            ConsoleUtils.afficherInfo("Aucune candidature enregistrée.");
        } else {
            candidatures.forEach(c -> {
                System.out.println(c);
                if (c.getCommentaireEntreprise() != null && !c.getCommentaireEntreprise().isEmpty())
                    System.out.println("    Commentaire : " + c.getCommentaireEntreprise());
            });
            int id = ConsoleUtils.lireEntier("\nID candidature à annuler (0 = non) : ");
            if (id > 0) {
                try {
                    candidatureService.annuler(id);
                    ConsoleUtils.afficherSucces("Candidature annulée.");
                } catch (CandidatureException e) {
                    ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                } catch (DatabaseException e) {
                    ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                }
            }
        }
        ConsoleUtils.attendreTouche();
    }

    private void voirStage() {
        ConsoleUtils.afficherSousTitre("Mon stage en cours");
        Stage stage = stageService.trouverStageActifEtudiant(etudiant.getIdEtudiant());
        if (stage == null) {
            ConsoleUtils.afficherInfo("Vous n'avez pas de stage en cours.");
        } else {
            System.out.println(stage);
            System.out.println("Rapport soumis : " + (stage.getRapport() != null ? "Oui" : "Non"));
            System.out.println("Note obtenue   : " + String.format("%.1f/20", stage.getNoteMoyenne()));
            System.out.println("\n  1. Soumettre / mettre à jour le rapport   0. Retour");
            if (ConsoleUtils.lireEntierEntre("Choix : ", 0, 1) == 1) {
                System.out.println("Saisissez votre rapport :");
                String rapport = ConsoleUtils.getScanner().nextLine();
                try {
                    stageService.soumettreRapport(stage.getIdStage(), rapport);
                    ConsoleUtils.afficherSucces("Rapport soumis avec succès.");
                } catch (StageException e) {
                    ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                } catch (DatabaseException e) {
                    ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                }
            }
        }
        ConsoleUtils.attendreTouche();
    }

    private void genererCV() {
        try {
            String fichier = ExportUtils.exporterCVEtudiant(etudiant);
            ConsoleUtils.afficherSucces("CV généré : " + fichier);
        } catch (Exception e) {
            ConsoleUtils.afficherErreur("Erreur génération CV : " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }
}
