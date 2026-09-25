package com.gestion.stages.model;

import com.gestion.stages.model.enums.StatutStage;
import java.time.LocalDate;

public class Stage {

    private int idStage;
    private int idEtudiant;
    private int idEntreprise;
    private int idOffre;
    private int idEncadrant;
    private String nomEtudiant;
    private String nomEntreprise;
    private String sujet;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutStage statut;
    private String rapport;
    private double noteEncadrant;
    private double noteEntreprise;
    private String commentaireEncadrant;
    private String commentaireEntreprise;
    private boolean attestationGeneree;

    public Stage() {
        this.statut = StatutStage.EN_COURS;
        this.noteEncadrant = 0.0;
        this.noteEntreprise = 0.0;
        this.attestationGeneree = false;
    }

    public int getIdStage() { return idStage; }
    public void setIdStage(int idStage) { this.idStage = idStage; }

    public int getIdEtudiant() { return idEtudiant; }
    public void setIdEtudiant(int idEtudiant) { this.idEtudiant = idEtudiant; }

    public int getIdEntreprise() { return idEntreprise; }
    public void setIdEntreprise(int idEntreprise) { this.idEntreprise = idEntreprise; }

    public int getIdOffre() { return idOffre; }
    public void setIdOffre(int idOffre) { this.idOffre = idOffre; }

    public int getIdEncadrant() { return idEncadrant; }
    public void setIdEncadrant(int idEncadrant) { this.idEncadrant = idEncadrant; }

    public String getNomEtudiant() { return nomEtudiant; }
    public void setNomEtudiant(String nomEtudiant) { this.nomEtudiant = nomEtudiant; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public StatutStage getStatut() { return statut; }
    public void setStatut(StatutStage statut) { this.statut = statut; }

    public String getRapport() { return rapport; }
    public void setRapport(String rapport) { this.rapport = rapport; }

    public double getNoteEncadrant() { return noteEncadrant; }
    public void setNoteEncadrant(double noteEncadrant) { this.noteEncadrant = noteEncadrant; }

    public double getNoteEntreprise() { return noteEntreprise; }
    public void setNoteEntreprise(double noteEntreprise) { this.noteEntreprise = noteEntreprise; }

    public String getCommentaireEncadrant() { return commentaireEncadrant; }
    public void setCommentaireEncadrant(String commentaireEncadrant) { this.commentaireEncadrant = commentaireEncadrant; }

    public String getCommentaireEntreprise() { return commentaireEntreprise; }
    public void setCommentaireEntreprise(String commentaireEntreprise) { this.commentaireEntreprise = commentaireEntreprise; }

    public boolean isAttestationGeneree() { return attestationGeneree; }
    public void setAttestationGeneree(boolean attestationGeneree) { this.attestationGeneree = attestationGeneree; }

    public double getNoteMoyenne() {
        if (noteEncadrant > 0 && noteEntreprise > 0) return (noteEncadrant + noteEntreprise) / 2;
        if (noteEncadrant > 0) return noteEncadrant;
        if (noteEntreprise > 0) return noteEntreprise;
        return 0.0;
    }

    @Override
    public String toString() {
        return String.format("[%d] Etudiant: %s | Entreprise: %s | Sujet: %s | %s → %s | Statut: %s | Note: %.1f/20",
                idStage,
                nomEtudiant != null ? nomEtudiant : "ID#" + idEtudiant,
                nomEntreprise != null ? nomEntreprise : "ID#" + idEntreprise,
                sujet, dateDebut, dateFin, statut.getLibelle(), getNoteMoyenne());
    }
}
