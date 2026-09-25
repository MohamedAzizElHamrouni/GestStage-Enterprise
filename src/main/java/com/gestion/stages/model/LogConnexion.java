package com.gestion.stages.model;

import java.time.LocalDateTime;

public class LogConnexion {

    private int idLog;
    private int idUtilisateur;
    private String nomUtilisateur;
    private LocalDateTime dateHeure;
    private String action;
    private String adresseIP;
    private boolean succes;

    public LogConnexion() {
        this.dateHeure = LocalDateTime.now();
    }

    public LogConnexion(int idUtilisateur, String action, boolean succes) {
        this();
        this.idUtilisateur = idUtilisateur;
        this.action = action;
        this.succes = succes;
    }

    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getAdresseIP() { return adresseIP; }
    public void setAdresseIP(String adresseIP) { this.adresseIP = adresseIP; }

    public boolean isSucces() { return succes; }
    public void setSucces(boolean succes) { this.succes = succes; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Action: %s | Résultat: %s",
                dateHeure, nomUtilisateur != null ? nomUtilisateur : "ID#" + idUtilisateur,
                action, succes ? "Succès" : "Échec");
    }
}
