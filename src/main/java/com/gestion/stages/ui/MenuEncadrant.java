package com.gestion.stages.ui;

import java.util.List;

import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.StageException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Encadrant;
import com.gestion.stages.model.Stage;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.service.EtudiantService;
import com.gestion.stages.service.IEtudiantService;
import com.gestion.stages.service.IStageService;
import com.gestion.stages.service.StageService;
import com.gestion.stages.util.ConsoleUtils;
import com.gestion.stages.util.ExportUtils;

public class MenuEncadrant {

    private final IStageService   stageService   = new StageService();
    private final IEtudiantService etudiantService = new EtudiantService();

    private final Utilisateur utilisateur;
    private final Encadrant   encadrant;

    public MenuEncadrant(Utilisateur utilisateur, Encadrant encadrant) {
        this.utilisateur = utilisateur;
        this.encadrant   = encadrant;
    }

    public void afficher() {
        if (encadrant == null) {
            ConsoleUtils.afficherErreur("Profil encadrant introuvable. Contactez l'administration.");
            ConsoleUtils.attendreTouche();
            return;
        }
        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.viderEcran();
            ConsoleUtils.afficherTitre("ESPACE ENCADRANT — " + utilisateur.getNomComplet());
            System.out.println("  1. Mes étudiants en stage");
            System.out.println("  2. Détail d'un stage");
            System.out.println("  3. Évaluer un stage");
            System.out.println("  4. Terminer un stage");
            System.out.println("  5. Générer une attestation de stage (PDF)");
            System.out.println("  0. Déconnexion");
            ConsoleUtils.afficherSeparateur();
            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 5);
            switch (choix) {
                case 1: voirStages(); break;
                case 2: detailStage(); break;
                case 3: evaluerStage(); break;
                case 4: terminerStage(); break;
                case 5: genererAttestation(); break;
                case 0: continuer = false; break;
            }
        }
    }

    private void voirStages() {
        ConsoleUtils.afficherSousTitre("Mes étudiants en stage");
        List<Stage> stages = stageService.listerParEncadrant(encadrant.getIdEncadrant());
        if (stages.isEmpty()) ConsoleUtils.afficherInfo("Aucun étudiant en stage actuellement.");
        stages.forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }

    private void detailStage() {
        int id = ConsoleUtils.lireEntier("ID du stage : ");
        try {
            Stage s = stageService.trouverParId(id);
            System.out.println("\n" + "=".repeat(55));
            System.out.println("Stage ID       : " + s.getIdStage());
            System.out.println("Étudiant       : " + s.getNomEtudiant());
            System.out.println("Entreprise     : " + s.getNomEntreprise());
            System.out.println("Sujet          : " + s.getSujet());
            System.out.println("Période        : " + s.getDateDebut() + " → " + s.getDateFin());
            System.out.println("Statut         : " + s.getStatut().getLibelle());
            System.out.println("Rapport soumis : " + (s.getRapport() != null ? "Oui" : "Non"));
            if (s.getRapport() != null)
                System.out.println("Extrait rapport: " + s.getRapport().substring(0, Math.min(200, s.getRapport().length())) + "...");
            System.out.println("Note encadrant : " + s.getNoteEncadrant() + "/20");
            System.out.println("Note entreprise: " + s.getNoteEntreprise() + "/20");
            System.out.println("Note moyenne   : " + String.format("%.1f/20", s.getNoteMoyenne()));
            if (s.getCommentaireEncadrant() != null)
                System.out.println("Commentaire    : " + s.getCommentaireEncadrant());
        } catch (StageException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void evaluerStage() {
        int id = ConsoleUtils.lireEntier("ID du stage à évaluer : ");
        try {
            Stage s = stageService.trouverParId(id);
            double noteEnc = ConsoleUtils.lireDouble("Votre note (/20) : ");
            String commentaire = ConsoleUtils.lireChaine("Commentaire d'évaluation : ");
            stageService.noter(id, noteEnc, s.getNoteEntreprise(), commentaire, s.getCommentaireEntreprise());
            ConsoleUtils.afficherSucces("Évaluation enregistrée. Note encadrant : " + noteEnc + "/20");
        } catch (StageException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation - " + e.getChamp() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void terminerStage() {
        int id = ConsoleUtils.lireEntier("ID du stage à terminer : ");
        if (!ConsoleUtils.lireOuiNon("Confirmer la fin du stage ?")) return;
        try {
            stageService.terminer(id);
            ConsoleUtils.afficherSucces("Stage marqué comme terminé.");
        } catch (StageException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void genererAttestation() {
        int id = ConsoleUtils.lireEntier("ID du stage : ");
        try {
            Stage s = stageService.trouverParId(id);
            String fichier = ExportUtils.exporterAttestationStage(s);
            stageService.marquerAttestationGeneree(id);
            ConsoleUtils.afficherSucces("Attestation générée : " + fichier);
        } catch (StageException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }
}
