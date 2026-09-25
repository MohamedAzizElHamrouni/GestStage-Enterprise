package com.gestion.stages.model;

public class Entreprise {

    private int idEntreprise;
    private int idUtilisateur;
    private String nom;
    private String domaine;
    private String adresse;
    private String telephone;
    private String email;
    private String description;
    private String responsableRH;
    private boolean valide;
    private double note;

    public Entreprise() {
        this.valide = false;
        this.note = 0.0;
    }

    public int getIdEntreprise() { return idEntreprise; }
    public void setIdEntreprise(int idEntreprise) { this.idEntreprise = idEntreprise; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDomaine() { return domaine; }
    public void setDomaine(String domaine) { this.domaine = domaine; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getResponsableRH() { return responsableRH; }
    public void setResponsableRH(String responsableRH) { this.responsableRH = responsableRH; }

    public boolean isValide() { return valide; }
    public void setValide(boolean valide) { this.valide = valide; }

    public double getNote() { return note; }
    public void setNote(double note) { this.note = note; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | %s | RH: %s | Validée: %s | Note: %.1f/5",
                idEntreprise, nom, domaine, adresse, responsableRH,
                valide ? "Oui" : "Non", note);
    }
}
