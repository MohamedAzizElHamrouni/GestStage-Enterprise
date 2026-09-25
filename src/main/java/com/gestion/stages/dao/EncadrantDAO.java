package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.Encadrant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EncadrantDAO {

    public Encadrant creer(Encadrant enc) {
        String sql = "INSERT INTO encadrants (id_utilisateur, nom, prenom, email, telephone, departement, specialite, grade) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, enc.getIdUtilisateur());
            ps.setString(2, enc.getNom());
            ps.setString(3, enc.getPrenom());
            ps.setString(4, enc.getEmail());
            ps.setString(5, enc.getTelephone());
            ps.setString(6, enc.getDepartement());
            ps.setString(7, enc.getSpecialite());
            ps.setString(8, enc.getGrade());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) enc.setIdEncadrant(rs.getInt(1));
            return enc;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création encadrant: " + e.getMessage(), e);
        }
    }

    public boolean modifier(Encadrant enc) {
        String sql = "UPDATE encadrants SET nom=?, prenom=?, email=?, telephone=?, departement=?, specialite=?, grade=? WHERE id_encadrant=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, enc.getNom()); ps.setString(2, enc.getPrenom());
            ps.setString(3, enc.getEmail()); ps.setString(4, enc.getTelephone());
            ps.setString(5, enc.getDepartement()); ps.setString(6, enc.getSpecialite());
            ps.setString(7, enc.getGrade()); ps.setInt(8, enc.getIdEncadrant());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur modification encadrant: " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM encadrants WHERE id_encadrant=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression encadrant: " + e.getMessage(), e);
        }
    }

    public Encadrant trouverParId(int id) {
        String sql = "SELECT * FROM encadrants WHERE id_encadrant=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche encadrant: " + e.getMessage(), e);
        }
        return null;
    }

    public Encadrant trouverParUtilisateur(int idUtilisateur) {
        String sql = "SELECT * FROM encadrants WHERE id_utilisateur=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche encadrant par user: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Encadrant> listerTous() {
        String sql = "SELECT * FROM encadrants ORDER BY nom, prenom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste encadrants: " + e.getMessage(), e);
        }
    }

    public List<Encadrant> rechercherParDepartement(String departement) {
        String sql = "SELECT * FROM encadrants WHERE departement LIKE ? ORDER BY nom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + departement + "%");
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche encadrants: " + e.getMessage(), e);
        }
    }

    private List<Encadrant> mapperListe(ResultSet rs) throws SQLException {
        List<Encadrant> liste = new ArrayList<>();
        while (rs.next()) liste.add(mapper(rs));
        return liste;
    }

    private Encadrant mapper(ResultSet rs) throws SQLException {
        Encadrant enc = new Encadrant();
        enc.setIdEncadrant(rs.getInt("id_encadrant"));
        enc.setIdUtilisateur(rs.getInt("id_utilisateur"));
        enc.setNom(rs.getString("nom"));
        enc.setPrenom(rs.getString("prenom"));
        enc.setEmail(rs.getString("email"));
        enc.setTelephone(rs.getString("telephone"));
        enc.setDepartement(rs.getString("departement"));
        enc.setSpecialite(rs.getString("specialite"));
        enc.setGrade(rs.getString("grade"));
        return enc;
    }
}
