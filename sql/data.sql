-- ============================================================
-- DONNÉES D'EXEMPLE - Gestion des Stages Universitaires
-- Exécuter après schema.sql
-- ============================================================

USE gestion_stages;

-- ============================================================
-- Comptes utilisateurs (mots de passe en clair indiqués en commentaire)
-- NOTE: Les vrais mots de passe sont hachés par l'application.
-- Ces insertions sont pour les tests UNIQUEMENT.
-- Utilisez l'application pour créer de vrais comptes.
-- ============================================================

-- Pour les tests, on insère un hash fictif et on peut reset le mdp via l'app
-- Mot de passe sera réinitialisé via l'application

-- ADMIN (créer via l'app avec: Admin@2024)
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif) VALUES
('Benali', 'Mohammed', 'admin@universite.dz',
 'PLACEHOLDER_HASH_USE_APP_TO_RESET', 'ADMIN', true);

-- ENCADRANTS (créer via l'app)
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif) VALUES
('Khelif', 'Amina',    'a.khelif@universite.dz',    'PLACEHOLDER_HASH', 'ENCADRANT', true),
('Bouzid', 'Karim',    'k.bouzid@universite.dz',    'PLACEHOLDER_HASH', 'ENCADRANT', true);

INSERT INTO encadrants (id_utilisateur, nom, prenom, email, telephone, departement, specialite, grade) VALUES
(2, 'Khelif', 'Amina', 'a.khelif@universite.dz', '0550000001', 'Informatique', 'Génie Logiciel', 'Maître de Conférences A'),
(3, 'Bouzid', 'Karim', 'k.bouzid@universite.dz', '0550000002', 'Informatique', 'Réseaux et Systèmes', 'Professeur');

-- ENTREPRISES
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif) VALUES
('TechSoft', 'DZ',      'contact@techsoft.dz',    'PLACEHOLDER_HASH', 'ENTREPRISE', true),
('Sonatrach', 'Digital', 'digital@sonatrach.dz',  'PLACEHOLDER_HASH', 'ENTREPRISE', true),
('Ooredoo', 'IT',        'it@ooredoo.dz',          'PLACEHOLDER_HASH', 'ENTREPRISE', true);

INSERT INTO entreprises (id_utilisateur, nom, domaine, adresse, telephone, email, description, responsable_rh, valide, note) VALUES
(4, 'TechSoft DZ',       'Développement Logiciel',  'Alger, Hydra',          '023000001', 'contact@techsoft.dz',    'Entreprise spécialisée en développement d''applications web et mobile.',            'Mme. Saïdi Nadia',  true, 4.5),
(5, 'Sonatrach Digital', 'Énergie / Informatique',  'Alger, Val d''Hydra',   '023000002', 'digital@sonatrach.dz',   'Division numérique de Sonatrach — transformation digitale du secteur énergétique.',  'M. Amrani Sofiane', true, 4.8),
(6, 'Ooredoo Algérie IT','Télécommunications',       'Alger, El Mouradia',    '023000003', 'it@ooredoo.dz',          'Direction IT d''Ooredoo Algérie — réseaux, cloud et cybersécurité.',                  'M. Meziane Lyes',   true, 4.2);

-- ÉTUDIANTS
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif) VALUES
('Maidi',   'Yacine',   'y.maidi@etudiant.univ.dz',   'PLACEHOLDER_HASH', 'ETUDIANT', true),
('Cherif',  'Sonia',    's.cherif@etudiant.univ.dz',   'PLACEHOLDER_HASH', 'ETUDIANT', true),
('Hamdani', 'Amine',    'a.hamdani@etudiant.univ.dz',  'PLACEHOLDER_HASH', 'ETUDIANT', true),
('Rouibah', 'Fatima',   'f.rouibah@etudiant.univ.dz',  'PLACEHOLDER_HASH', 'ETUDIANT', true),
('Talbi',   'Mehdi',    'm.talbi@etudiant.univ.dz',    'PLACEHOLDER_HASH', 'ETUDIANT', true);

INSERT INTO etudiants (id_utilisateur, matricule, nom, prenom, email, telephone, specialite, niveau, cv, competences, moyenne, statut_stage) VALUES
(7,  '2024-INF-001', 'Maidi',   'Yacine',  'y.maidi@etudiant.univ.dz',   '0770000001', 'Informatique - Génie Logiciel',    'M2',       'Projet PFE: Développement d''une app mobile de gestion RH.', 'Java,Spring Boot,React Native,MySQL,Git',     15.75, 'SANS_STAGE'),
(8,  '2024-INF-002', 'Cherif',  'Sonia',   's.cherif@etudiant.univ.dz',   '0770000002', 'Informatique - Réseaux',           'M1',       'Stage L3 chez Mobilis. Projet réseau campus.',               'Cisco,Linux,Python,Wireshark,Docker',        16.20, 'SANS_STAGE'),
(9,  '2024-INF-003', 'Hamdani', 'Amine',   'a.hamdani@etudiant.univ.dz',  '0770000003', 'Informatique - Intelligence Artificielle', 'M2', 'Mémoire: Détection d''anomalies par ML.',                    'Python,TensorFlow,Keras,SQL,R',              17.50, 'SANS_STAGE'),
(10, '2024-INF-004', 'Rouibah', 'Fatima',  'f.rouibah@etudiant.univ.dz',  '0770000004', 'Informatique - Génie Logiciel',    'L3',       'Projet L2: Site web de gestion bibliothèque.',               'HTML,CSS,JavaScript,PHP,MySQL',              13.80, 'SANS_STAGE'),
(11, '2024-INF-005', 'Talbi',   'Mehdi',   'm.talbi@etudiant.univ.dz',    '0770000005', 'Informatique - Cybersécurité',     'M2',       'Audit sécurité: Pentest réseau entreprise locale.',          'Kali Linux,Metasploit,Python,Wireshark,SIEM',18.00, 'SANS_STAGE');

-- OFFRES DE STAGE
INSERT INTO offres_stage (id_entreprise, titre, description, domaine, duree_mois, type, date_debut, date_fin, date_publication, date_limite, remuneration, competences_requises, niveau_requis, nombre_places, statut) VALUES
(1, 'Développeur Full Stack Java/React',
   'Stage de fin d''études en développement full stack. Participation à la conception et au développement d''une application SaaS de gestion RH.',
   'Développement Logiciel', 6, 'PFE', '2024-07-01', '2024-12-31', '2024-04-01', '2024-06-15',
   25000.00, 'Java,Spring Boot,React,MySQL,Git', 'M2', 2, 'VALIDEE'),

(1, 'Développeur Mobile Android/Flutter',
   'Stage en développement mobile. Développement d''une application de tracking livraison.',
   'Développement Mobile', 4, 'Stage d''été', '2024-06-01', '2024-09-30', '2024-04-01', '2024-05-20',
   20000.00, 'Flutter,Dart,Android,Firebase', 'M1', 1, 'VALIDEE'),

(2, 'Data Scientist - Analyse Prédictive',
   'Stage en science des données pour l''optimisation de la production pétrolière. Modèles de prédiction de pannes.',
   'Intelligence Artificielle', 6, 'PFE', '2024-07-15', '2025-01-15', '2024-04-01', '2024-06-30',
   35000.00, 'Python,Machine Learning,TensorFlow,SQL,Pandas', 'M2', 1, 'VALIDEE'),

(3, 'Ingénieur Réseaux Junior',
   'Stage en administration réseaux et systèmes. Déploiement et monitoring d''infrastructure réseau.',
   'Réseaux & Télécommunications', 5, 'PFE', '2024-07-01', '2024-11-30', '2024-04-01', '2024-06-15',
   30000.00, 'Cisco,Linux,BGP,OSPF,Wireshark', 'M1,M2', 2, 'VALIDEE'),

(3, 'Analyste Cybersécurité',
   'Stage en sécurité informatique. Audit, tests d''intrusion et mise en place de politiques de sécurité.',
   'Cybersécurité', 6, 'PFE', '2024-07-01', '2024-12-31', '2024-04-01', '2024-06-20',
   32000.00, 'Kali Linux,Pentest,Python,SIEM,ISO 27001', 'M2', 1, 'VALIDEE');

-- ============================================================
-- Résultat:
-- 3 rôles admin/encadrant/entreprise + 5 étudiants
-- 5 offres de stage validées
-- Prêt pour les candidatures !
-- 
-- IMPORTANT: Réinitialisez les mots de passe via l'application
-- avant toute utilisation en production.
-- ============================================================

SELECT 'Base de données initialisée avec succès !' AS message;
SELECT * FROM v_statistiques;
