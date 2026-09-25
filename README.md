# Gestion des Stages Universitaires
### Application Java Console — POO + MySQL JDBC

---

## Description

Application console Java complète pour la gestion des stages universitaires. Aucune interface graphique, aucun framework web — pure Java POO avec persistance MySQL via JDBC.

---

## Architecture du projet

```
java-internship-app/
├── pom.xml                          # Configuration Maven + dépendances
├── config.properties                # Configuration base de données
├── sql/
│   ├── schema.sql                   # Schéma complet (tables + vues)
│   └── data_exemple.sql             # Données de test
└── src/com/gestion/stages/
    ├── Main.java                    # Point d'entrée
    ├── config/
    │   └── DatabaseConfig.java      # Connexion MySQL JDBC
    ├── model/
    │   ├── Utilisateur.java
    │   ├── Etudiant.java
    │   ├── Entreprise.java
    │   ├── Encadrant.java
    │   ├── OffreStage.java
    │   ├── Candidature.java
    │   ├── Stage.java
    │   ├── LogConnexion.java
    │   └── enums/
    │       ├── Role.java            # ADMIN, ETUDIANT, ENTREPRISE, ENCADRANT
    │       ├── NiveauEtude.java     # L1, L2, L3, M1, M2, DOCTORAT
    │       ├── StatutStage.java
    │       ├── StatutCandidature.java
    │       └── StatutOffre.java
    ├── dao/                         # Couche accès données (JDBC)
    │   ├── UtilisateurDAO.java
    │   ├── EtudiantDAO.java
    │   ├── EntrepriseDAO.java
    │   ├── EncadrantDAO.java
    │   ├── OffreStageDAO.java
    │   ├── CandidatureDAO.java
    │   ├── StageDAO.java
    │   └── LogConnexionDAO.java
    ├── service/                     # Logique métier
    │   ├── AuthService.java         # Authentification + protection brute force
    │   ├── UtilisateurService.java
    │   ├── EtudiantService.java
    │   ├── OffreStageService.java
    │   ├── CandidatureService.java
    │   └── StageService.java
    ├── ui/                          # Menus console
    │   ├── MenuPrincipal.java       # Bannière + login
    │   ├── MenuAdmin.java           # Toutes fonctions admin
    │   ├── MenuEtudiant.java        # Espace étudiant
    │   ├── MenuEntreprise.java      # Espace entreprise
    │   └── MenuEncadrant.java       # Espace encadrant
    └── util/
        ├── PasswordUtils.java       # Hachage SHA-256 + sel
        ├── ConsoleUtils.java        # Saisie, couleurs ANSI, affichage
        └── ExportUtils.java         # Export PDF (iText) + CSV
```

---

## Prérequis

| Outil | Version minimale |
|-------|-----------------|
| JDK   | 11+             |
| Maven | 3.6+            |
| MySQL | 8.0+            |

---

## Installation et lancement

### 1. Préparer la base de données

```sql
-- Dans MySQL:
source sql/schema.sql;
source sql/data_exemple.sql;   -- (optionnel, données de test)
```

### 2. Configurer la connexion

Éditez `config.properties` :

```properties
db.url=jdbc:mysql://localhost:3306/gestion_stages?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=VotreMotDePasse
```

### 3. Compiler

```bash
mvn clean package
```

### 4. Lancer

```bash
java -jar target/gestion-stages-all.jar
```

---

## Modules implémentés

### 1. Gestion des Utilisateurs (ADMIN)
- CRUD complet : créer ADMIN, ÉTUDIANT, ENTREPRISE, ENCADRANT
- Modification, désactivation (suppression logique), suppression définitive
- Recherche et filtrage par rôle
- Réinitialisation de mot de passe (génération mot de passe temporaire)
- Hachage SHA-256 + sel aléatoire (PasswordUtils)
- Journaux de connexion (LogConnexionDAO)
- Protection brute force (5 tentatives / 15 minutes)

### 2. Gestion des Étudiants
- Profil complet (matricule, spécialité, niveau, CV, compétences, moyenne)
- Recherche par spécialité, niveau, statut
- Classement par moyenne
- Score automatique calculé (moyenne + nombre de compétences)
- Suggestions d'offres basées sur le profil
- Génération CV PDF automatique (iText)

### 3. Gestion des Entreprises
- Profil entreprise avec validation admin obligatoire
- Domaine d'activité, responsable RH
- Notation des entreprises (note /5)
- Classement par note

### 4. Offres de Stage
- Publication par l'entreprise (statut EN_ATTENTE)
- Validation/rejet par l'admin
- Expiration automatique selon date limite
- Recherche multicritère (domaine, compétences, titre, entreprise)

### 5. Candidatures
- Postuler avec lettre de motivation
- Détection de candidatures en doublon
- Accepter / refuser par l'entreprise avec commentaire
- Annulation par l'étudiant

### 6. Suivi des Stages
- Création du stage (lié à offre + encadrant)
- Mise à jour automatique du statut étudiant
- Soumission de rapport par l'étudiant
- Évaluation par l'encadrant (/20) et l'entreprise (/20)
- Note moyenne calculée
- Validation finale par l'admin
- Génération d'attestation PDF (iText)

### 7. Exports
- Étudiants → CSV et PDF (tableau formaté)
- Offres de stage → CSV
- Stages → CSV
- Attestation de stage → PDF
- CV étudiant → PDF

### 8. Sécurité
- Mots de passe hachés (SHA-256 + sel)
- Validation force du mot de passe (8 chars min, 1 majuscule, 1 chiffre)
- Protection contre les attaques brute force
- Journaux de connexion (succès/échec)
- Suppression logique (désactivation compte)

---

## Rôles et permissions

| Fonctionnalité                | ADMIN | ÉTUDIANT | ENTREPRISE | ENCADRANT |
|-------------------------------|:-----:|:--------:|:----------:|:---------:|
| Gérer les utilisateurs        |  ✅   |          |            |           |
| Valider entreprises/offres    |  ✅   |          |            |           |
| Statistiques globales         |  ✅   |          |            |           |
| Voir/modifier son profil      |  ✅   |    ✅    |     ✅     |    ✅     |
| Consulter offres disponibles  |       |    ✅    |            |           |
| Postuler à une offre          |       |    ✅    |            |           |
| Suggérer offres automatiques  |       |    ✅    |            |           |
| Générer son CV PDF            |       |    ✅    |            |           |
| Publier une offre             |       |          |     ✅     |           |
| Gérer les candidatures        |       |          |     ✅     |           |
| Évaluer un stage              |       |          |            |    ✅     |
| Générer attestation           |       |          |            |    ✅     |

---

## Dépendances Maven

| Dépendance          | Version | Usage                    |
|---------------------|---------|--------------------------|
| mysql-connector-j   | 8.3.0   | Driver JDBC MySQL        |
| itextpdf            | 5.5.13  | Génération PDF           |
| commons-csv         | 1.10.0  | Export CSV (Apache)      |
