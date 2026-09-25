package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.Stage;
import com.gestion.stages.model.enums.StatutStage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StageDAO {

    private static final String SELECT_JOIN =
        "SELECT s.*, " +
        "CONCAT(et.prenom, ' ', et.nom) AS nom_etudiant, " +
        "en.nom AS nom_entreprise " +
        "FROM stages s " +
        "LEFT JOIN etudiants et ON s.id_etudiant = et.id_etudiant " +
        "LEFT JOIN entreprises en ON s.id_entreprise = en.id_entreprise ";

    public Stage creer(Stage s) {
        String sql = "INSERT INTO stages (id_etudiant, id_entreprise, id_offre, id_encadrant, sujet, date_debut, date_fin, statut) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getIdEtudiant());
            ps.setInt(2, s.getIdEntreprise());
            ps.setInt(3, s.getIdOffre());
            ps.setInt(4, s.getIdEncadrant());
            ps.setString(5, s.getSujet());
            ps.setDate(6, s.getDateDebut() != null ? Date.valueOf(s.getDateDebut()) : null);
            ps.setDate(7, s.getDateFin() != null ? Date.valueOf(s.getDateFin()) : null);
            ps.setString(8, s.getStatut().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) s.setIdStage(rs.getInt(1));
            return s;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création stage: " + e.getMessage(), e);
        }
    }

    public boolean modifier(Stage s) {
        String sql = "UPDATE stages SET sujet=?, date_debut=?, date_fin=?, statut=?, rapport=?, note_encadrant=?, note_entreprise=?, commentaire_encadrant=?, commentaire_entreprise=?, attestation_generee=? WHERE id_stage=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getSujet());
            ps.setDate(2, s.getDateDebut() != null ? Date.valueOf(s.getDateDebut()) : null);
            ps.setDate(3, s.getDateFin() != null ? Date.valueOf(s.getDateFin()) : null);
            ps.setString(4, s.getStatut().name());
            ps.setString(5, s.getRapport());
            ps.setDouble(6, s.getNoteEncadrant());
            ps.setDouble(7, s.getNoteEntreprise());
            ps.setString(8, s.getCommentaireEncadrant());
            ps.setString(9, s.getCommentaireEntreprise());
            ps.setBoolean(10, s.isAttestationGeneree());
            ps.setInt(11, s.getIdStage());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur modification stage: " + e.getMessage(), e);
        }
    }

    public boolean changerStatut(int id, StatutStage statut) {
        String sql = "UPDATE stages SET statut=? WHERE id_stage=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name()); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur changement statut stage: " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM stages WHERE id_stage=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression stage: " + e.getMessage(), e);
        }
    }

    public Stage trouverParId(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE s.id_stage=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche stage: " + e.getMessage(), e);
        }
        return null;
    }

    public Stage trouverStageActifEtudiant(int idEtudiant) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE s.id_etudiant=? AND s.statut='EN_COURS'")) {
            ps.setInt(1, idEtudiant);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche stage actif: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Stage> listerTous() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "ORDER BY s.date_debut DESC")) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste stages: " + e.getMessage(), e);
        }
    }

    public List<Stage> listerParEtudiant(int idEtudiant) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE s.id_etudiant=? ORDER BY s.date_debut DESC")) {
            ps.setInt(1, idEtudiant);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste stages étudiant: " + e.getMessage(), e);
        }
    }

    public List<Stage> listerParEncadrant(int idEncadrant) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE s.id_encadrant=? ORDER BY s.date_debut DESC")) {
            ps.setInt(1, idEncadrant);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste stages encadrant: " + e.getMessage(), e);
        }
    }

    public List<Stage> listerParStatut(StatutStage statut) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE s.statut=? ORDER BY s.date_debut DESC")) {
            ps.setString(1, statut.name());
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste stages par statut: " + e.getMessage(), e);
        }
    }

    public int compterParStatut(StatutStage statut) {
        String sql = "SELECT COUNT(*) FROM stages WHERE statut=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur comptage stages: " + e.getMessage(), e);
        }
    }

    private List<Stage> mapperListe(ResultSet rs) throws SQLException {
        List<Stage> liste = new ArrayList<>();
        while (rs.next()) liste.add(mapper(rs));
        return liste;
    }

    private Stage mapper(ResultSet rs) throws SQLException {
        Stage s = new Stage();
        s.setIdStage(rs.getInt("id_stage"));
        s.setIdEtudiant(rs.getInt("id_etudiant"));
        s.setIdEntreprise(rs.getInt("id_entreprise"));
        s.setIdOffre(rs.getInt("id_offre"));
        s.setIdEncadrant(rs.getInt("id_encadrant"));
        try { s.setNomEtudiant(rs.getString("nom_etudiant")); } catch (SQLException ignored) {}
        try { s.setNomEntreprise(rs.getString("nom_entreprise")); } catch (SQLException ignored) {}
        s.setSujet(rs.getString("sujet"));
        Date dd = rs.getDate("date_debut"); if (dd != null) s.setDateDebut(dd.toLocalDate());
        Date df = rs.getDate("date_fin");   if (df != null) s.setDateFin(df.toLocalDate());
        s.setStatut(StatutStage.valueOf(rs.getString("statut")));
        s.setRapport(rs.getString("rapport"));
        s.setNoteEncadrant(rs.getDouble("note_encadrant"));
        s.setNoteEntreprise(rs.getDouble("note_entreprise"));
        s.setCommentaireEncadrant(rs.getString("commentaire_encadrant"));
        s.setCommentaireEntreprise(rs.getString("commentaire_entreprise"));
        s.setAttestationGeneree(rs.getBoolean("attestation_generee"));
        return s;
    }
}
