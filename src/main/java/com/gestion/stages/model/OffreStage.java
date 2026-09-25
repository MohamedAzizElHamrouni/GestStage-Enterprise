package com.gestion.stages.model;

import com.gestion.stages.model.enums.StatutOffre;
import java.time.LocalDate;

public class OffreStage {

    private int idOffre;
    private int idEntreprise;
    private String nomEntreprise;
    private String titre;
    private String description;
    private String domaine;
    private int dureeEnMois;
    private String type;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDate datePublication;
    private LocalDate dateLimite;
    private double remuneration;
    private String competencesRequises;
    private String niveauRequis;
    private int nombrePlaces;
    private StatutOffre statut;

    public OffreStage() {
        this.statut = StatutOffre.EN_ATTENTE;
        this.datePublication = LocalDate.now();
        this.nombrePlaces = 1;
    }

    public int getIdOffre() { return idOffre; }
    public void setIdOffre(int idOffre) { this.idOffre = idOffre; }

    public int getIdEntreprise() { return idEntreprise; }
    public void setIdEntreprise(int idEntreprise) { this.idEntreprise = idEntreprise; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDomaine() { return domaine; }
    public void setDomaine(String domaine) { this.domaine = domaine; }

    public int getDureeEnMois() { return dureeEnMois; }
    public void setDureeEnMois(int dureeEnMois) { this.dureeEnMois = dureeEnMois; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public LocalDate getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }

    public LocalDate getDateLimite() { return dateLimite; }
    public void setDateLimite(LocalDate dateLimite) { this.dateLimite = dateLimite; }

    public double getRemuneration() { return remuneration; }
    public void setRemuneration(double remuneration) { this.remuneration = remuneration; }

    public String getCompetencesRequises() { return competencesRequises; }
    public void setCompetencesRequises(String competencesRequises) { this.competencesRequises = competencesRequises; }

    public String getNiveauRequis() { return niveauRequis; }
    public void setNiveauRequis(String niveauRequis) { this.niveauRequis = niveauRequis; }

    public int getNombrePlaces() { return nombrePlaces; }
    public void setNombrePlaces(int nombrePlaces) { this.nombrePlaces = nombrePlaces; }

    public StatutOffre getStatut() { return statut; }
    public void setStatut(StatutOffre statut) { this.statut = statut; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Entreprise: %s | Domaine: %s | Durée: %d mois | Rémun: %.0f DA | Places: %d | Statut: %s",
                idOffre, titre, nomEntreprise != null ? nomEntreprise : "ID#" + idEntreprise,
                domaine, dureeEnMois, remuneration, nombrePlaces, statut.getLibelle());
    }
}
