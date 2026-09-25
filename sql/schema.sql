-- ============================================================
-- SCHEMA: Gestion des Stages Universitaires
-- Base de données: MySQL 8+
-- ============================================================

CREATE DATABASE IF NOT EXISTS gestion_stages
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestion_stages;

-- ============================================================
-- TABLE: utilisateurs
-- ============================================================
CREATE TABLE IF NOT EXISTS utilisateurs (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nom           VARCHAR(100) NOT NULL,
    prenom        VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe  VARCHAR(512) NOT NULL,
    role          ENUM('ADMIN','ETUDIANT','ENTREPRISE','ENCADRANT') NOT NULL,
    date_creation DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actif         BOOLEAN      NOT NULL DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_role  (role)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: etudiants
-- ============================================================
CREATE TABLE IF NOT EXISTS etudiants (
    id_etudiant   INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    matricule     VARCHAR(50)  NOT NULL UNIQUE,
    nom           VARCHAR(100) NOT NULL,
    prenom        VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    telephone     VARCHAR(20),
    specialite    VARCHAR(150),
    niveau        ENUM('L1','L2','L3','M1','M2','DOCTORAT'),
    cv            TEXT,
    competences   TEXT,
    moyenne       DECIMAL(4,2) NOT NULL DEFAULT 0.00,
    statut_stage  ENUM('SANS_STAGE','EN_COURS','TERMINE','VALIDE','ABANDONNE') NOT NULL DEFAULT 'SANS_STAGE',
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_specialite   (specialite),
    INDEX idx_statut_stage (statut_stage),
    INDEX idx_moyenne      (moyenne)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: encadrants
-- ============================================================
CREATE TABLE IF NOT EXISTS encadrants (
    id_encadrant  INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    nom           VARCHAR(100) NOT NULL,
    prenom        VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    telephone     VARCHAR(20),
    departement   VARCHAR(150),
    specialite    VARCHAR(150),
    grade         VARCHAR(100),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_departement (departement)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: entreprises
-- ============================================================
CREATE TABLE IF NOT EXISTS entreprises (
    id_entreprise  INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    nom            VARCHAR(200) NOT NULL,
    domaine        VARCHAR(150),
    adresse        VARCHAR(300),
    telephone      VARCHAR(20),
    email          VARCHAR(255),
    description    TEXT,
    responsable_rh VARCHAR(200),
    valide         BOOLEAN NOT NULL DEFAULT FALSE,
    note           DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_domaine (domaine),
    INDEX idx_valide  (valide)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: offres_stage
-- ============================================================
CREATE TABLE IF NOT EXISTS offres_stage (
    id_offre             INT AUTO_INCREMENT PRIMARY KEY,
    id_entreprise        INT NOT NULL,
    titre                VARCHAR(300) NOT NULL,
    description          TEXT,
    domaine              VARCHAR(150),
    duree_mois           INT NOT NULL DEFAULT 1,
    type                 VARCHAR(100),
    date_debut           DATE,
    date_fin             DATE,
    date_publication     DATE NOT NULL,
    date_limite          DATE,
    remuneration         DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    competences_requises TEXT,
    niveau_requis        VARCHAR(100),
    nombre_places        INT NOT NULL DEFAULT 1,
    statut               ENUM('EN_ATTENTE','VALIDEE','REJETEE','EXPIREE','POURVUE') NOT NULL DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (id_entreprise) REFERENCES entreprises(id_entreprise) ON DELETE CASCADE,
    INDEX idx_statut_offre    (statut),
    INDEX idx_domaine_offre   (domaine),
    INDEX idx_date_publication (date_publication)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: candidatures
-- ============================================================
CREATE TABLE IF NOT EXISTS candidatures (
    id_candidature       INT AUTO_INCREMENT PRIMARY KEY,
    id_etudiant          INT NOT NULL,
    id_offre             INT NOT NULL,
    date_candidature     DATE NOT NULL,
    statut               ENUM('EN_ATTENTE','ACCEPTEE','REFUSEE','ANNULEE') NOT NULL DEFAULT 'EN_ATTENTE',
    lettre_motivation    TEXT,
    commentaire_entreprise TEXT,
    FOREIGN KEY (id_etudiant) REFERENCES etudiants(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_offre)    REFERENCES offres_stage(id_offre) ON DELETE CASCADE,
    UNIQUE KEY uk_candidature (id_etudiant, id_offre),
    INDEX idx_statut_cand (statut)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: stages
-- ============================================================
CREATE TABLE IF NOT EXISTS stages (
    id_stage              INT AUTO_INCREMENT PRIMARY KEY,
    id_etudiant           INT NOT NULL,
    id_entreprise         INT NOT NULL,
    id_offre              INT,
    id_encadrant          INT,
    sujet                 VARCHAR(500),
    date_debut            DATE,
    date_fin              DATE,
    statut                ENUM('SANS_STAGE','EN_COURS','TERMINE','VALIDE','ABANDONNE') NOT NULL DEFAULT 'EN_COURS',
    rapport               TEXT,
    note_encadrant        DECIMAL(4,2) NOT NULL DEFAULT 0.00,
    note_entreprise       DECIMAL(4,2) NOT NULL DEFAULT 0.00,
    commentaire_encadrant TEXT,
    commentaire_entreprise TEXT,
    attestation_generee   BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (id_etudiant)   REFERENCES etudiants(id_etudiant)   ON DELETE CASCADE,
    FOREIGN KEY (id_entreprise) REFERENCES entreprises(id_entreprise),
    FOREIGN KEY (id_offre)      REFERENCES offres_stage(id_offre)   ON DELETE SET NULL,
    FOREIGN KEY (id_encadrant)  REFERENCES encadrants(id_encadrant) ON DELETE SET NULL,
    INDEX idx_statut_stage   (statut),
    INDEX idx_id_encadrant   (id_encadrant)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: logs_connexion
-- ============================================================
CREATE TABLE IF NOT EXISTS logs_connexion (
    id_log         INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL DEFAULT 0,
    date_heure     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action         VARCHAR(100),
    adresse_ip     VARCHAR(45),
    succes         BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_id_util_log  (id_utilisateur),
    INDEX idx_date_heure   (date_heure),
    INDEX idx_succes       (succes)
) ENGINE=InnoDB;

-- ============================================================
-- DONNÉES INITIALES: Compte administrateur par défaut
-- Mot de passe: Admin@2024  (haché avec SHA-256 + sel)
-- IMPORTANT: Changer ce mot de passe dès la première connexion !
-- ============================================================

-- Pour générer un vrai hash, lancez l'application et utilisez
-- la fonctionnalité de création de compte admin, ou utilisez
-- ce script pour créer l'admin initial avec le mot de passe "Admin@2024":
-- Le hash ci-dessous est un exemple; l'application génère le vrai hash.

-- Insertion d'un admin initial (utilisez l'application pour créer le vrai compte)
-- ou décommentez et adaptez après avoir généré le hash :
-- INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif)
-- VALUES ('Admin', 'Super', 'admin@universite.dz', '<HASH_GENERE>', 'ADMIN', true);

-- ============================================================
-- VUES UTILES
-- ============================================================

CREATE OR REPLACE VIEW v_etudiants_complets AS
SELECT
    e.id_etudiant,
    e.matricule,
    e.nom,
    e.prenom,
    e.email,
    e.telephone,
    e.specialite,
    e.niveau,
    e.moyenne,
    e.statut_stage,
    (e.moyenne * 5 + COALESCE(LENGTH(e.competences) - LENGTH(REPLACE(e.competences, ',', '')) + 1, 0) * 2) AS score,
    u.actif
FROM etudiants e
JOIN utilisateurs u ON e.id_utilisateur = u.id;

CREATE OR REPLACE VIEW v_offres_disponibles AS
SELECT
    o.*,
    en.nom AS nom_entreprise,
    en.domaine AS domaine_entreprise
FROM offres_stage o
JOIN entreprises en ON o.id_entreprise = en.id_entreprise
WHERE o.statut = 'VALIDEE'
  AND (o.date_limite IS NULL OR o.date_limite >= CURDATE());

CREATE OR REPLACE VIEW v_stages_en_cours AS
SELECT
    s.*,
    CONCAT(et.prenom, ' ', et.nom) AS nom_etudiant,
    et.specialite,
    en.nom AS nom_entreprise,
    CONCAT(enc.prenom, ' ', enc.nom) AS nom_encadrant
FROM stages s
JOIN etudiants   et  ON s.id_etudiant   = et.id_etudiant
JOIN entreprises en  ON s.id_entreprise = en.id_entreprise
LEFT JOIN encadrants enc ON s.id_encadrant = enc.id_encadrant
WHERE s.statut = 'EN_COURS';

CREATE OR REPLACE VIEW v_statistiques AS
SELECT
    (SELECT COUNT(*) FROM utilisateurs WHERE actif = true)           AS total_utilisateurs_actifs,
    (SELECT COUNT(*) FROM etudiants WHERE statut_stage = 'SANS_STAGE') AS etudiants_sans_stage,
    (SELECT COUNT(*) FROM etudiants WHERE statut_stage = 'EN_COURS')   AS etudiants_en_stage,
    (SELECT COUNT(*) FROM etudiants WHERE statut_stage = 'VALIDE')     AS stages_valides,
    (SELECT COUNT(*) FROM offres_stage WHERE statut = 'VALIDEE')       AS offres_actives,
    (SELECT COUNT(*) FROM offres_stage WHERE statut = 'EN_ATTENTE')    AS offres_en_attente,
    (SELECT COUNT(*) FROM candidatures WHERE statut = 'EN_ATTENTE')    AS candidatures_en_attente,
    (SELECT ROUND(AVG(moyenne), 2) FROM etudiants)                     AS moyenne_generale_etudiants;
