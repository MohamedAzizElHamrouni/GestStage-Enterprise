# 🎓 GestStage — Système d'Information de Gestion des Stages

[![Java](https://img.shields.io/badge/Java-11%2B-orange?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven)](https://maven.apache.org/)
[![Database](https://img.shields.io/badge/Database-MySQL-4479A1?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![Architecture](https://img.shields.io/badge/Architecture-N--Tiers-blue?style=for-the-badge)]()

---

## 📌 Présentation

**GestStage** est une application console Java modulaire basée sur une architecture en couches (DAO / Service / UI). Elle centralise et automatise l'ensemble du cycle de vie des stages académiques en interconnectant les **Étudiants**, les **Entreprises**, les **Encadrants** et les **Administrateurs**.

---

## 🔥 Fonctionnalités Principales

* 🔑 **Authentification Sécurisée** : Connexion par rôles (`ADMIN`, `ETUDIANT`, `ENTREPRISE`, `ENCADRANT`), hachage des mots de passe (`PasswordUtils`) et journalisation des accès (`LogConnexionDAO`).
* 🎓 **Espace Étudiant** : Consultation du profil, recherche d'offres par niveau d'étude, postulation et suivi des candidatures.
* 🏢 **Espace Entreprise** : Publication/gestion d'offres de stage, traitement des candidatures et suivi des stagiaires.
* 👨‍🏫 **Espace Encadrant** : Suivi pédagogique des étudiants assignés et validation des conventions.
* 🛠️ **Espace Administrateur** : Gestion globale des comptes, paramètres système et supervision.
* 📄 **Exports PDF** : Génération automatique de bilans et de rapports officiels (`ExportUtils`).

---

## 🏗️ Structure du Projet

```text
JavaC/
├── 📜 pom.xml                       # Dépendances et build Maven[cite: 1]
├── 📜 config.properties             # Paramètres de connexion SQL[cite: 1]
├── 📂 sql/                          # Scripts SQL (schema.sql, data.sql)[cite: 1]
└── 📂 src/main/java/com/gestion/stages/
    ├── 🎬 Main.java                 # Point d'entrée de l'application[cite: 1]
    ├── ⚙️ config/                    # DatabaseConfig (JDBC Singleton)[cite: 1]
    ├── 🗄️ dao/                       # Interface d'accès aux données (JDBC)[cite: 1]
    ├── ⚠️ exception/                 # Exceptions personnalisées[cite: 1]
    ├── 📦 model/                    # Entités du domaine & Énumérations[cite: 1]
    ├── 🧠 service/                  # Services métier & interfaces[cite: 1]
    ├── 💻 ui/                       # Menus de la console interactifs[cite: 1]
    └── 🛠️ util/                     # Console, Sécurité, Export PDF[cite: 1]
⚙️ Prérequis & Installation
1. Prérequis
JDK 11 ou supérieur

Apache Maven 3.8+

Serveur MySQL 8.0+

2. Configuration de la Base de Données
Initialisez la base de données via les scripts fournis[cite: 1] :

Bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/data.sql
Ajustez vos identifiants dans config.properties[cite: 1] :

Properties
db.url=jdbc:mysql://localhost:3306/gestion_stages
db.user=root
db.password=votre_mot_de_passe
🚀 Exécution
Bash
# Compilation du projet
mvn clean compile

# Lancement de l'application
mvn exec:java -Dexec.mainClass="com.gestion.stages.Main"
Projet développé en Java avec architecture N-Tiers[cite: 1].


---

### 🔵 Option 2 : README Enterprise & Complet (Format Technique & Structure)

```markdown
# Application de Gestion des Stages (com.gestion.stages)

## Description Technique
Application Java Console conçue selon les principes de l'architecture N-Tiers découplée[cite: 1] :
- **Couche Présentation (UI)** : Interfaces console adaptées aux rôles (`MenuAdmin`, `MenuEtudiant`, `MenuEntreprise`, `MenuEncadrant`)[cite: 1].
- **Couche Service** : Logique métier abstraite par des interfaces (`IAuthService`, `ICandidatureService`, etc.)[cite: 1].
- **Couche DAO** : Persistance relationnelle directe JDBC (`EtudiantDAO`, `OffreStageDAO`, `StageDAO`, etc.)[cite: 1].
- **Sûreté & Sécurité** : Gestion des exceptions sur-mesure, salage/hachage des mots de passe et traçabilité des connexions[cite: 1].

---

## Rôles & Accès
1. **Administrateur** : Supervision globale, gestion des utilisateurs, audit et rapports[cite: 1].
2. **Étudiant** : Gestion du profil, postulation aux offres, consultation du statut de stage[cite: 1].
3. **Entreprise** : Publication des offres de stage, revue des candidatures, gestion des candidats retenus[cite: 1].
4. **Encadrant** : Suivi académique, évaluation et validation[cite: 1].

---

## Environnement & Stack
- **Langage** : Java 11 / 17
- **Gestionnaire de build** : Apache Maven
- **Base de données** : MySQL / PostgreSQL
- **Format d'exportation** : PDF (via `ExportUtils`)[cite: 1]

---

## Guide de Démarrage Rapide

### 1. Base de données
```bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/data.sql
2. Fichier de Configuration (src/main/resources/config.properties)
Properties
db.url=jdbc:mysql://localhost:3306/gestion_stages
db.user=root
db.password=root
3. Compilation et Exécution
Bash
mvn clean package
java -jar target/gestion-stages-1.0-SNAPSHOT.jar

---

### 🟠 Option 3 : README Minimaliste & Clean (Concis & Direct)

```markdown
# 🎓 GestStage Java Console

Plateforme de gestion globale des stages académiques et professionnels[cite: 1].

## 🚀 Démarrage Rapide

### Prérequis
- JDK 11+
- Maven
- MySQL

### Setup Base de Données
```bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/data.sql
Configuration (config.properties)
Properties
db.url=jdbc:mysql://localhost:3306/gestion_stages
db.user=root
db.password=votre_mot_de_passe
Lancement
Bash
mvn clean compile exec:java -Dexec.mainClass="com.gestion.stages.Main"
🛠️ Architecture
UI : com.gestion.stages.ui (Menus console interactifs)[cite: 1]

Services : com.gestion.stages.service (Logique métier)[cite: 1]

DAO : com.gestion.stages.dao (Requêtes SQL JDBC)[cite: 1]

Modèles : com.gestion.stages.model (Entités & Enums)[cite: 1]

Utils : com.gestion.stages.util (Sécurité, Exports PDF)[cite: 1]
