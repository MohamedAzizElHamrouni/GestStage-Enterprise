package com.gestion.stages.model;

import com.gestion.stages.model.enums.NiveauEtude;
import com.gestion.stages.model.enums.StatutStage;

public class Etudiant {

    private int idEtudiant;
    private int idUtilisateur;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String specialite;
    private NiveauEtude niveau;
    private String cv;
    private String competences;
    private double moyenne;
    private StatutStage statutStage;

    public Etudiant() {
        this.statutStage = StatutStage.SANS_STAGE;
        this.moyenne = 0.0;
    }

    public int getIdEtudiant() { return idEtudiant; }
    public void setIdEtudiant(int idEtudiant) { this.idEtudiant = idEtudiant; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public NiveauEtude getNiveau() { return niveau; }
    public void setNiveau(NiveauEtude niveau) { this.niveau = niveau; }

    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }

    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }

    public double getMoyenne() { return moyenne; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }

    public StatutStage getStatutStage() { return statutStage; }
    public void setStatutStage(StatutStage statutStage) { this.statutStage = statutStage; }

    public String getNomComplet() { return prenom + " " + nom; }

    public int calculerScore() {
        int score = (int) (moyenne * 5);
        if (competences != null && !competences.isEmpty()) {
            score += competences.split(",").length * 2;
        }
        return Math.min(score, 100);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s | %s | %s | Moy: %.2f/20 | Statut: %s",
                matricule, prenom, nom, specialite,
                niveau != null ? niveau.getLibelle() : "N/A",
                moyenne, statutStage.getLibelle());
    }
}
