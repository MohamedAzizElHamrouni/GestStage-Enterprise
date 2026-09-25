package com.gestion.stages.model;

public class Encadrant {

    private int idEncadrant;
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String departement;
    private String specialite;
    private String grade;

    public Encadrant() {}

    public int getIdEncadrant() { return idEncadrant; }
    public void setIdEncadrant(int idEncadrant) { this.idEncadrant = idEncadrant; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getNomComplet() { return prenom + " " + nom; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s | %s | %s | %s",
                idEncadrant, prenom, nom, email, departement, grade);
    }
}
