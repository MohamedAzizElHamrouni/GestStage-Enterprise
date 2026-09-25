package com.gestion.stages.ui;

import java.util.List;

import com.gestion.stages.dao.LogConnexionDAO;
import com.gestion.stages.exception.DatabaseException;
import com.gestion.stages.exception.EntrepriseException;
import com.gestion.stages.exception.OffreStageException;
import com.gestion.stages.exception.StageException;
import com.gestion.stages.exception.UtilisateurException;
import com.gestion.stages.exception.ValidationException;
import com.gestion.stages.model.Encadrant;
import com.gestion.stages.model.Entreprise;
import com.gestion.stages.model.Etudiant;
import com.gestion.stages.model.OffreStage;
import com.gestion.stages.model.Stage;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.model.enums.NiveauEtude;
import com.gestion.stages.model.enums.Role;
import com.gestion.stages.model.enums.StatutStage;
import com.gestion.stages.service.CandidatureService;
import com.gestion.stages.service.EntrepriseService;
import com.gestion.stages.service.EtudiantService;
import com.gestion.stages.service.ICandidatureService;
import com.gestion.stages.service.IEntrepriseService;
import com.gestion.stages.service.IEtudiantService;
import com.gestion.stages.service.IOffreStageService;
import com.gestion.stages.service.IStageService;
import com.gestion.stages.service.IUtilisateurService;
import com.gestion.stages.service.OffreStageService;
import com.gestion.stages.service.StageService;
import com.gestion.stages.service.UtilisateurService;
import com.gestion.stages.util.ConsoleUtils;
import com.gestion.stages.util.ExportUtils;

public class MenuAdmin {

    private final IUtilisateurService utilisateurService = new UtilisateurService();
    private final IEtudiantService    etudiantService    = new EtudiantService();
    private final IEntrepriseService  entrepriseService  = new EntrepriseService();
    private final IOffreStageService  offreService       = new OffreStageService();
    private final ICandidatureService candidatureService = new CandidatureService();
    private final IStageService       stageService       = new StageService();
    private final LogConnexionDAO     logDAO             = new LogConnexionDAO();

    public void afficher() {
        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.viderEcran();
            ConsoleUtils.afficherTitre("MENU ADMINISTRATEUR");
            System.out.println("  1. Gestion des utilisateurs");
            System.out.println("  2. Gestion des étudiants");
            System.out.println("  3. Gestion des entreprises");
            System.out.println("  4. Validation des offres de stage");
            System.out.println("  5. Gestion des stages");
            System.out.println("  6. Statistiques générales");
            System.out.println("  7. Journaux de connexion");
            System.out.println("  8. Exports (CSV/PDF)");
            System.out.println("  0. Déconnexion");
            ConsoleUtils.afficherSeparateur();
            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 8);
            switch (choix) {
                case 1: menuUtilisateurs(); break;
                case 2: menuEtudiants(); break;
                case 3: menuEntreprises(); break;
                case 4: menuValidationOffres(); break;
                case 5: menuStages(); break;
                case 6: afficherStatistiques(); break;
                case 7: afficherLogs(); break;
                case 8: menuExports(); break;
                case 0: continuer = false; break;
            }
        }
    }

    // ===== UTILISATEURS =====
    private void menuUtilisateurs() {
        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.afficherTitre("GESTION DES UTILISATEURS");
            System.out.println("  1. Lister tous les utilisateurs");
            System.out.println("  2. Rechercher un utilisateur");
            System.out.println("  3. Créer ADMIN");
            System.out.println("  4. Créer ÉTUDIANT");
            System.out.println("  5. Créer ENTREPRISE");
            System.out.println("  6. Créer ENCADRANT");
            System.out.println("  7. Modifier un utilisateur");
            System.out.println("  8. Désactiver un utilisateur");
            System.out.println("  9. Réinitialiser mot de passe");
            System.out.println("  0. Retour");
            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 9);
            switch (choix) {
                case 1: listerUtilisateurs(); break;
                case 2: rechercherUtilisateur(); break;
                case 3: creerUtilisateurAdmin(); break;
                case 4: creerCompteEtudiant(); break;
                case 5: creerCompteEntreprise(); break;
                case 6: creerCompteEncadrant(); break;
                case 7: modifierUtilisateur(); break;
                case 8: desactiverUtilisateur(); break;
                case 9: reinitialiserMdp(); break;
                case 0: continuer = false; break;
            }
        }
    }

    private void listerUtilisateurs() {
        List<Utilisateur> liste = utilisateurService.listerTous();
        ConsoleUtils.afficherSousTitre("Liste des utilisateurs (" + liste.size() + ")");
        liste.forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }

    private void rechercherUtilisateur() {
        String terme = ConsoleUtils.lireChaineMandatoire("Terme de recherche : ");
        utilisateurService.rechercher(terme).forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }

    private void creerUtilisateurAdmin() {
        ConsoleUtils.afficherSousTitre("Créer un compte ADMIN");
        Utilisateur u = saisirUtilisateur(Role.ADMIN);
        try {
            utilisateurService.creerUtilisateur(u);
            ConsoleUtils.afficherSucces("Admin créé (ID: " + u.getId() + ")");
        } catch (UtilisateurException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void creerCompteEtudiant() {
        ConsoleUtils.afficherSousTitre("Créer compte étudiant");
        Utilisateur u = saisirUtilisateur(Role.ETUDIANT);
        Etudiant etudiant = saisirEtudiant();
        etudiant.setNom(u.getNom()); etudiant.setPrenom(u.getPrenom()); etudiant.setEmail(u.getEmail());
        try {
            utilisateurService.creerEtudiantComplet(u, etudiant);
            ConsoleUtils.afficherSucces("Compte étudiant créé (ID: " + u.getId() + ")");
        } catch (UtilisateurException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation - " + e.getChamp() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void creerCompteEntreprise() {
        ConsoleUtils.afficherSousTitre("Créer compte entreprise");
        Utilisateur u = saisirUtilisateur(Role.ENTREPRISE);
        Entreprise entreprise = saisirEntreprise();
        try {
            utilisateurService.creerEntrepriseComplete(u, entreprise);
            ConsoleUtils.afficherSucces("Compte entreprise créé (ID: " + u.getId() + ")");
        } catch (UtilisateurException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void creerCompteEncadrant() {
        ConsoleUtils.afficherSousTitre("Créer compte encadrant");
        Utilisateur u = saisirUtilisateur(Role.ENCADRANT);
        Encadrant enc = saisirEncadrant();
        enc.setNom(u.getNom()); enc.setPrenom(u.getPrenom()); enc.setEmail(u.getEmail());
        try {
            utilisateurService.creerEncadrantComplet(u, enc);
            ConsoleUtils.afficherSucces("Compte encadrant créé (ID: " + u.getId() + ")");
        } catch (UtilisateurException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void modifierUtilisateur() {
        int id = ConsoleUtils.lireEntier("ID utilisateur à modifier : ");
        try {
            Utilisateur u = utilisateurService.trouverParId(id);
            System.out.println("Actuel : " + u);
            System.out.print("Nouveau nom (laisser vide = inchangé) : ");
            String nom = ConsoleUtils.getScanner().nextLine().trim();
            System.out.print("Nouveau prénom : ");
            String prenom = ConsoleUtils.getScanner().nextLine().trim();
            if (!nom.isEmpty())    u.setNom(nom);
            if (!prenom.isEmpty()) u.setPrenom(prenom);
            utilisateurService.modifierUtilisateur(u);
            ConsoleUtils.afficherSucces("Utilisateur modifié avec succès.");
        } catch (UtilisateurException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void desactiverUtilisateur() {
        int id = ConsoleUtils.lireEntier("ID utilisateur à désactiver : ");
        if (!ConsoleUtils.lireOuiNon("Confirmer la désactivation ?")) { return; }
        try {
            utilisateurService.desactiverUtilisateur(id);
            ConsoleUtils.afficherSucces("Utilisateur désactivé (ID: " + id + ")");
        } catch (UtilisateurException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    private void reinitialiserMdp() {
        int id = ConsoleUtils.lireEntier("ID utilisateur : ");
        try {
            String mdpTemp = utilisateurService.reinitialiserMotDePasse(id);
            ConsoleUtils.afficherSucces("Mot de passe réinitialisé.");
            System.out.println("Mot de passe temporaire : " + ConsoleUtils.BOLD + mdpTemp + ConsoleUtils.RESET);
        } catch (UtilisateurException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    // ===== ÉTUDIANTS =====
    private void menuEtudiants() {
        ConsoleUtils.afficherTitre("GESTION DES ÉTUDIANTS");
        System.out.println("  1. Lister tous   2. Rechercher   3. Classement par moyenne");
        System.out.println("  4. Filtrer par statut   0. Retour");
        int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 4);
        try {
            switch (choix) {
                case 1:
                    etudiantService.listerTous().forEach(System.out::println);
                    break;
                case 2:
                    String t = ConsoleUtils.lireChaineMandatoire("Terme : ");
                    etudiantService.rechercherGlobal(t).forEach(System.out::println);
                    break;
                case 3:
                    etudiantService.classementParMoyenne().forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("1=SANS_STAGE  2=EN_COURS  3=TERMINE  4=VALIDE");
                    int s = ConsoleUtils.lireEntierEntre("Choix: ", 1, 4);
                    StatutStage[] st = {StatutStage.SANS_STAGE, StatutStage.EN_COURS, StatutStage.TERMINE, StatutStage.VALIDE};
                    etudiantService.rechercherParStatut(st[s - 1]).forEach(System.out::println);
                    break;
            }
        } catch (Exception e) {
            ConsoleUtils.afficherErreur(e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    // ===== ENTREPRISES =====
    private void menuEntreprises() {
        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.afficherTitre("GESTION DES ENTREPRISES");
            System.out.println("  1. Toutes les entreprises");
            System.out.println("  2. Entreprises en attente de validation");
            System.out.println("  3. Valider une entreprise");
            System.out.println("  4. Rejeter une entreprise");
            System.out.println("  5. Noter une entreprise");
            System.out.println("  6. Classement par note");
            System.out.println("  0. Retour");
            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 6);
            switch (choix) {
                case 1:
                    entrepriseService.listerTous().forEach(System.out::println);
                    ConsoleUtils.attendreTouche();
                    break;
                case 2:
                    entrepriseService.listerEnAttenteValidation().forEach(System.out::println);
                    ConsoleUtils.attendreTouche();
                    break;
                case 3: {
                    int id = ConsoleUtils.lireEntier("ID entreprise à valider : ");
                    try {
                        entrepriseService.valider(id);
                        ConsoleUtils.afficherSucces("Entreprise validée.");
                    } catch (EntrepriseException e) {
                        ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                    } catch (DatabaseException e) {
                        ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                    }
                    ConsoleUtils.attendreTouche();
                    break;
                }
                case 4: {
                    int id = ConsoleUtils.lireEntier("ID entreprise à rejeter : ");
                    try {
                        entrepriseService.rejeter(id);
                        ConsoleUtils.afficherSucces("Entreprise rejetée.");
                    } catch (EntrepriseException e) {
                        ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                    } catch (DatabaseException e) {
                        ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                    }
                    ConsoleUtils.attendreTouche();
                    break;
                }
                case 5: {
                    int id   = ConsoleUtils.lireEntier("ID entreprise : ");
                    double n = ConsoleUtils.lireDouble("Note (/5) : ");
                    try {
                        entrepriseService.noter(id, n);
                        ConsoleUtils.afficherSucces("Note enregistrée.");
                    } catch (EntrepriseException | ValidationException e) {
                        ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                    } catch (DatabaseException e) {
                        ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                    }
                    ConsoleUtils.attendreTouche();
                    break;
                }
                case 6:
                    entrepriseService.classementParNote().forEach(System.out::println);
                    ConsoleUtils.attendreTouche();
                    break;
                case 0:
                    continuer = false;
                    break;
            }
        }
    }

    // ===== VALIDATION OFFRES =====
    private void menuValidationOffres() {
        boolean continuer = true;
        while (continuer) {
            ConsoleUtils.afficherTitre("VALIDATION DES OFFRES");
            List<OffreStage> enAttente = offreService.listerEnAttenteValidation();
            ConsoleUtils.afficherInfo(enAttente.size() + " offre(s) en attente");
            enAttente.forEach(System.out::println);
            System.out.println("\n  1. Valider   2. Rejeter   3. Toutes les offres   0. Retour");
            int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 3);
            switch (choix) {
                case 1: {
                    int id = ConsoleUtils.lireEntier("ID offre à valider : ");
                    try {
                        offreService.valider(id);
                        ConsoleUtils.afficherSucces("Offre validée et publiée.");
                    } catch (OffreStageException e) {
                        ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                    } catch (DatabaseException e) {
                        ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                    }
                    ConsoleUtils.attendreTouche();
                    break;
                }
                case 2: {
                    int id = ConsoleUtils.lireEntier("ID offre à rejeter : ");
                    try {
                        offreService.rejeter(id);
                        ConsoleUtils.afficherSucces("Offre rejetée.");
                    } catch (OffreStageException e) {
                        ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                    } catch (DatabaseException e) {
                        ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                    }
                    ConsoleUtils.attendreTouche();
                    break;
                }
                case 3:
                    offreService.listerTous().forEach(System.out::println);
                    ConsoleUtils.attendreTouche();
                    break;
                case 0:
                    continuer = false;
                    break;
            }
        }
    }

    // ===== STAGES =====
    private void menuStages() {
        ConsoleUtils.afficherTitre("GESTION DES STAGES");
        System.out.println("  1. Lister tous   2. En cours   3. Valider un stage   4. Créer stage   0. Retour");
        int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 4);
        switch (choix) {
            case 1:
                stageService.listerTous().forEach(System.out::println);
                ConsoleUtils.attendreTouche();
                break;
            case 2:
                stageService.listerParStatut(StatutStage.EN_COURS).forEach(System.out::println);
                ConsoleUtils.attendreTouche();
                break;
            case 3: {
                int id = ConsoleUtils.lireEntier("ID du stage à valider : ");
                try {
                    stageService.valider(id);
                    ConsoleUtils.afficherSucces("Stage validé avec succès.");
                } catch (StageException e) {
                    ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
                } catch (DatabaseException e) {
                    ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
                }
                ConsoleUtils.attendreTouche();
                break;
            }
            case 4:
                creerStageManuellement();
                break;
        }
    }

    private void creerStageManuellement() {
        ConsoleUtils.afficherSousTitre("Créer un stage manuellement");
        Stage stage = new Stage();
        stage.setIdEtudiant(ConsoleUtils.lireEntier("ID Étudiant : "));
        stage.setIdEntreprise(ConsoleUtils.lireEntier("ID Entreprise : "));
        stage.setIdOffre(ConsoleUtils.lireEntier("ID Offre (0 si aucune) : "));
        stage.setIdEncadrant(ConsoleUtils.lireEntier("ID Encadrant : "));
        stage.setSujet(ConsoleUtils.lireChaineMandatoire("Sujet du stage : "));
        stage.setDateDebut(ConsoleUtils.lireDate("Date de début"));
        stage.setDateFin(ConsoleUtils.lireDate("Date de fin"));
        try {
            stageService.creer(stage);
            ConsoleUtils.afficherSucces("Stage créé (ID: " + stage.getIdStage() + ")");
        } catch (StageException e) {
            ConsoleUtils.afficherErreur("[" + e.getCodeErreur() + "] " + e.getMessage());
        } catch (ValidationException e) {
            ConsoleUtils.afficherErreur("[Validation - " + e.getChamp() + "] " + e.getMessage());
        } catch (DatabaseException e) {
            ConsoleUtils.afficherErreur("[Base de données] " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    // ===== STATISTIQUES =====
    private void afficherStatistiques() {
        ConsoleUtils.afficherTitre("STATISTIQUES GÉNÉRALES");
        System.out.println("\n  -- Utilisateurs --");
        System.out.println("  Total : " + utilisateurService.listerTous().size());
        for (Role r : Role.values())
            System.out.println("  " + r.getLibelle() + " : " + utilisateurService.listerParRole(r).size());
        System.out.println("\n  -- Étudiants --");
        for (StatutStage s : StatutStage.values())
            System.out.println("  " + s.getLibelle() + " : " + etudiantService.rechercherParStatut(s).size());
        System.out.println("\n  -- Offres --");
        System.out.println("  Total     : " + offreService.listerTous().size());
        System.out.println("  Validées  : " + offreService.listerValidees().size());
        System.out.println("  Attente   : " + offreService.listerEnAttenteValidation().size());
        System.out.println("\n  -- Stages --");
        stageService.afficherStatistiques();
        ConsoleUtils.attendreTouche();
    }

    // ===== LOGS =====
    private void afficherLogs() {
        ConsoleUtils.afficherTitre("JOURNAUX DE CONNEXION");
        logDAO.listerTous().forEach(System.out::println);
        ConsoleUtils.attendreTouche();
    }

    // ===== EXPORTS =====
    private void menuExports() {
        ConsoleUtils.afficherTitre("EXPORTS");
        System.out.println("  1. Étudiants CSV   2. Étudiants PDF   3. Offres CSV   4. Stages CSV   0. Retour");
        int choix = ConsoleUtils.lireEntierEntre("Votre choix : ", 0, 4);
        try {
            switch (choix) {
                case 1: ConsoleUtils.afficherSucces("Fichier: " + ExportUtils.exporterEtudiantsCSV(etudiantService.listerTous())); break;
                case 2: ConsoleUtils.afficherSucces("Fichier: " + ExportUtils.exporterEtudiantsPDF(etudiantService.listerTous())); break;
                case 3: ConsoleUtils.afficherSucces("Fichier: " + ExportUtils.exporterOffresCSV(offreService.listerTous())); break;
                case 4: ConsoleUtils.afficherSucces("Fichier: " + ExportUtils.exporterStagesCSV(stageService.listerTous())); break;
            }
        } catch (Exception e) {
            ConsoleUtils.afficherErreur("Erreur export : " + e.getMessage());
        }
        ConsoleUtils.attendreTouche();
    }

    // ===== HELPERS SAISIE =====
    private Utilisateur saisirUtilisateur(Role role) {
        Utilisateur u = new Utilisateur();
        u.setNom(ConsoleUtils.lireChaineMandatoire("Nom : "));
        u.setPrenom(ConsoleUtils.lireChaineMandatoire("Prénom : "));
        u.setEmail(ConsoleUtils.lireChaineMandatoire("Email : "));
        u.setMotDePasse(ConsoleUtils.lireMotDePasse("Mot de passe : "));
        u.setRole(role);
        return u;
    }

    private Etudiant saisirEtudiant() {
        Etudiant e = new Etudiant();
        e.setMatricule(ConsoleUtils.lireChaineMandatoire("Matricule : "));
        e.setTelephone(ConsoleUtils.lireChaine("Téléphone : "));
        e.setSpecialite(ConsoleUtils.lireChaineMandatoire("Spécialité : "));
        String niv = ConsoleUtils.lireChaine("Niveau (L1/L2/L3/M1/M2/DOCTORAT) : ").toUpperCase();
        try { e.setNiveau(NiveauEtude.valueOf(niv)); } catch (Exception ex) { e.setNiveau(NiveauEtude.L3); }
        e.setMoyenne(ConsoleUtils.lireDouble("Moyenne (/20) : "));
        e.setCompetences(ConsoleUtils.lireChaine("Compétences (virgule) : "));
        return e;
    }

    private Entreprise saisirEntreprise() {
        Entreprise en = new Entreprise();
        en.setNom(ConsoleUtils.lireChaineMandatoire("Nom entreprise : "));
        en.setDomaine(ConsoleUtils.lireChaineMandatoire("Domaine : "));
        en.setAdresse(ConsoleUtils.lireChaine("Adresse : "));
        en.setTelephone(ConsoleUtils.lireChaine("Téléphone : "));
        en.setEmail(ConsoleUtils.lireChaine("Email : "));
        en.setDescription(ConsoleUtils.lireChaine("Description : "));
        en.setResponsableRH(ConsoleUtils.lireChaine("Responsable RH : "));
        return en;
    }

    private Encadrant saisirEncadrant() {
        Encadrant enc = new Encadrant();
        enc.setTelephone(ConsoleUtils.lireChaine("Téléphone : "));
        enc.setDepartement(ConsoleUtils.lireChaineMandatoire("Département : "));
        enc.setSpecialite(ConsoleUtils.lireChaine("Spécialité : "));
        enc.setGrade(ConsoleUtils.lireChaine("Grade : "));
        return enc;
    }
}
