package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.Etudiant;
import com.gestion.stages.model.enums.NiveauEtude;
import com.gestion.stages.model.enums.StatutStage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO {

    public Etudiant creer(Etudiant e) {
        String sql = "INSERT INTO etudiants (id_utilisateur, matricule, nom, prenom, email, telephone, specialite, niveau, cv, competences, moyenne, statut_stage) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getIdUtilisateur());
            ps.setString(2, e.getMatricule());
            ps.setString(3, e.getNom());
            ps.setString(4, e.getPrenom());
            ps.setString(5, e.getEmail());
            ps.setString(6, e.getTelephone());
            ps.setString(7, e.getSpecialite());
            ps.setString(8, e.getNiveau() != null ? e.getNiveau().name() : null);
            ps.setString(9, e.getCv());
            ps.setString(10, e.getCompetences());
            ps.setDouble(11, e.getMoyenne());
            ps.setString(12, e.getStatutStage().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) e.setIdEtudiant(rs.getInt(1));
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur création étudiant: " + ex.getMessage(), ex);
        }
    }

    public boolean modifier(Etudiant e) {
        String sql = "UPDATE etudiants SET nom=?, prenom=?, email=?, telephone=?, specialite=?, niveau=?, cv=?, competences=?, moyenne=?, statut_stage=? WHERE id_etudiant=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNom()); ps.setString(2, e.getPrenom());
            ps.setString(3, e.getEmail()); ps.setString(4, e.getTelephone());
            ps.setString(5, e.getSpecialite());
            ps.setString(6, e.getNiveau() != null ? e.getNiveau().name() : null);
            ps.setString(7, e.getCv()); ps.setString(8, e.getCompetences());
            ps.setDouble(9, e.getMoyenne()); ps.setString(10, e.getStatutStage().name());
            ps.setInt(11, e.getIdEtudiant());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur modification étudiant: " + ex.getMessage(), ex);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM etudiants WHERE id_etudiant=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression étudiant: " + e.getMessage(), e);
        }
    }

    public Etudiant trouverParId(int id) {
        String sql = "SELECT * FROM etudiants WHERE id_etudiant=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche étudiant: " + e.getMessage(), e);
        }
        return null;
    }

    public Etudiant trouverParUtilisateur(int idUtilisateur) {
        String sql = "SELECT * FROM etudiants WHERE id_utilisateur=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche étudiant par user: " + e.getMessage(), e);
        }
        return null;
    }

    public Etudiant trouverParMatricule(String matricule) {
        String sql = "SELECT * FROM etudiants WHERE matricule=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricule);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche par matricule: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Etudiant> listerTous() {
        String sql = "SELECT * FROM etudiants ORDER BY nom, prenom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste étudiants: " + e.getMessage(), e);
        }
    }

    public List<Etudiant> rechercherParSpecialite(String specialite) {
        String sql = "SELECT * FROM etudiants WHERE specialite LIKE ? ORDER BY moyenne DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + specialite + "%");
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche par spécialité: " + e.getMessage(), e);
        }
    }

    public List<Etudiant> rechercherParNiveau(NiveauEtude niveau) {
        String sql = "SELECT * FROM etudiants WHERE niveau=? ORDER BY moyenne DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, niveau.name());
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche par niveau: " + e.getMessage(), e);
        }
    }

    public List<Etudiant> rechercherParStatut(StatutStage statut) {
        String sql = "SELECT * FROM etudiants WHERE statut_stage=? ORDER BY nom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche par statut: " + e.getMessage(), e);
        }
    }

    public List<Etudiant> classementParMoyenne() {
        String sql = "SELECT * FROM etudiants ORDER BY moyenne DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur classement: " + e.getMessage(), e);
        }
    }

    public List<Etudiant> rechercherGlobal(String terme) {
        String sql = "SELECT * FROM etudiants WHERE nom LIKE ? OR prenom LIKE ? OR matricule LIKE ? OR specialite LIKE ? ORDER BY nom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String p = "%" + terme + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, p);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche globale: " + e.getMessage(), e);
        }
    }

    private List<Etudiant> mapperListe(ResultSet rs) throws SQLException {
        List<Etudiant> liste = new ArrayList<>();
        while (rs.next()) liste.add(mapper(rs));
        return liste;
    }

    private Etudiant mapper(ResultSet rs) throws SQLException {
        Etudiant e = new Etudiant();
        e.setIdEtudiant(rs.getInt("id_etudiant"));
        e.setIdUtilisateur(rs.getInt("id_utilisateur"));
        e.setMatricule(rs.getString("matricule"));
        e.setNom(rs.getString("nom"));
        e.setPrenom(rs.getString("prenom"));
        e.setEmail(rs.getString("email"));
        e.setTelephone(rs.getString("telephone"));
        e.setSpecialite(rs.getString("specialite"));
        String niv = rs.getString("niveau");
        if (niv != null) e.setNiveau(NiveauEtude.valueOf(niv));
        e.setCv(rs.getString("cv"));
        e.setCompetences(rs.getString("competences"));
        e.setMoyenne(rs.getDouble("moyenne"));
        e.setStatutStage(StatutStage.valueOf(rs.getString("statut_stage")));
        return e;
    }
}
