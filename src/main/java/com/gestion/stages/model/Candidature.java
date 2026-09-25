package com.gestion.stages.model;

import com.gestion.stages.model.enums.StatutCandidature;
import java.time.LocalDate;

public class Candidature {

    private int idCandidature;
    private int idEtudiant;
    private int idOffre;
    private String nomEtudiant;
    private String titreOffre;
    private String nomEntreprise;
    private LocalDate dateCandidature;
    private StatutCandidature statut;
    private String lettreMotivation;
    private String commentaireEntreprise;

    public Candidature() {
        this.statut = StatutCandidature.EN_ATTENTE;
        this.dateCandidature = LocalDate.now();
    }

    public int getIdCandidature() { return idCandidature; }
    public void setIdCandidature(int idCandidature) { this.idCandidature = idCandidature; }

    public int getIdEtudiant() { return idEtudiant; }
    public void setIdEtudiant(int idEtudiant) { this.idEtudiant = idEtudiant; }

    public int getIdOffre() { return idOffre; }
    public void setIdOffre(int idOffre) { this.idOffre = idOffre; }

    public String getNomEtudiant() { return nomEtudiant; }
    public void setNomEtudiant(String nomEtudiant) { this.nomEtudiant = nomEtudiant; }

    public String getTitreOffre() { return titreOffre; }
    public void setTitreOffre(String titreOffre) { this.titreOffre = titreOffre; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }

    public LocalDate getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(LocalDate dateCandidature) { this.dateCandidature = dateCandidature; }

    public StatutCandidature getStatut() { return statut; }
    public void setStatut(StatutCandidature statut) { this.statut = statut; }

    public String getLettreMotivation() { return lettreMotivation; }
    public void setLettreMotivation(String lettreMotivation) { this.lettreMotivation = lettreMotivation; }

    public String getCommentaireEntreprise() { return commentaireEntreprise; }
    public void setCommentaireEntreprise(String commentaireEntreprise) { this.commentaireEntreprise = commentaireEntreprise; }

    @Override
    public String toString() {
        return String.format("[%d] Etudiant: %s | Offre: %s (%s) | Date: %s | Statut: %s",
                idCandidature,
                nomEtudiant != null ? nomEtudiant : "ID#" + idEtudiant,
                titreOffre != null ? titreOffre : "ID#" + idOffre,
                nomEntreprise != null ? nomEntreprise : "",
                dateCandidature, statut.getLibelle());
    }
}
