package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.LogConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogConnexionDAO {

    public void enregistrer(LogConnexion log) {
        String sql = "INSERT INTO logs_connexion (id_utilisateur, date_heure, action, adresse_ip, succes) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, log.getIdUtilisateur());
            ps.setTimestamp(2, Timestamp.valueOf(log.getDateHeure()));
            ps.setString(3, log.getAction());
            ps.setString(4, log.getAdresseIP());
            ps.setBoolean(5, log.isSucces());
            ps.executeUpdate();
        } catch (SQLException e) {
            // Ne pas propager: les logs ne doivent jamais bloquer l'application
            System.err.println("Avertissement - log non enregistré: " + e.getMessage());
        }
    }

    public List<LogConnexion> listerTous() {
        String sql = "SELECT l.*, CONCAT(u.prenom, ' ', u.nom) AS nom_utilisateur FROM logs_connexion l LEFT JOIN utilisateurs u ON l.id_utilisateur = u.id ORDER BY l.date_heure DESC LIMIT 200";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste logs: " + e.getMessage(), e);
        }
    }

    public List<LogConnexion> listerParUtilisateur(int idUtilisateur) {
        String sql = "SELECT l.*, CONCAT(u.prenom, ' ', u.nom) AS nom_utilisateur FROM logs_connexion l LEFT JOIN utilisateurs u ON l.id_utilisateur = u.id WHERE l.id_utilisateur=? ORDER BY l.date_heure DESC LIMIT 50";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste logs utilisateur: " + e.getMessage(), e);
        }
    }

    public int compterEchecsRecents(String email, int minutesFenetre) {
        String sql = "SELECT COUNT(*) FROM logs_connexion l JOIN utilisateurs u ON l.id_utilisateur = u.id WHERE u.email=? AND l.succes=false AND l.date_heure > DATE_SUB(NOW(), INTERVAL ? MINUTE)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, minutesFenetre);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    private List<LogConnexion> mapperListe(ResultSet rs) throws SQLException {
        List<LogConnexion> liste = new ArrayList<>();
        while (rs.next()) {
            LogConnexion l = new LogConnexion();
            l.setIdLog(rs.getInt("id_log"));
            l.setIdUtilisateur(rs.getInt("id_utilisateur"));
            try { l.setNomUtilisateur(rs.getString("nom_utilisateur")); } catch (SQLException ignored) {}
            Timestamp ts = rs.getTimestamp("date_heure");
            if (ts != null) l.setDateHeure(ts.toLocalDateTime());
            l.setAction(rs.getString("action"));
            l.setAdresseIP(rs.getString("adresse_ip"));
            l.setSucces(rs.getBoolean("succes"));
            liste.add(l);
        }
        return liste;
    }
}
