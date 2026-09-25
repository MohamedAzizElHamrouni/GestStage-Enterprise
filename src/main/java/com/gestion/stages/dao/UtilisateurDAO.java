package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.Utilisateur;
import com.gestion.stages.model.enums.Role;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public Utilisateur creer(Utilisateur u) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, date_creation, actif) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMotDePasse());
            ps.setString(5, u.getRole().name());
            ps.setTimestamp(6, Timestamp.valueOf(u.getDateCreation()));
            ps.setBoolean(7, u.isActif());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) u.setId(rs.getInt(1));
            return u;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création utilisateur: " + e.getMessage(), e);
        }
    }

    public boolean modifier(Utilisateur u) {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, email=?, mot_de_passe=?, role=?, actif=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMotDePasse());
            ps.setString(5, u.getRole().name());
            ps.setBoolean(6, u.isActif());
            ps.setInt(7, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur modification utilisateur: " + e.getMessage(), e);
        }
    }

    public boolean desactiver(int id) {
        String sql = "UPDATE utilisateurs SET actif=false WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur désactivation: " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression: " + e.getMessage(), e);
        }
    }

    public Utilisateur trouverParId(int id) {
        String sql = "SELECT * FROM utilisateurs WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche par id: " + e.getMessage(), e);
        }
        return null;
    }

    public Utilisateur trouverParEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche par email: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Utilisateur> listerTous() {
        return lister("SELECT * FROM utilisateurs ORDER BY nom, prenom");
    }

    public List<Utilisateur> listerParRole(Role role) {
        String sql = "SELECT * FROM utilisateurs WHERE role=? ORDER BY nom, prenom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role.name());
            ResultSet rs = ps.executeQuery();
            return mapperListe(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste par rôle: " + e.getMessage(), e);
        }
    }

    public List<Utilisateur> rechercher(String terme) {
        String sql = "SELECT * FROM utilisateurs WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? ORDER BY nom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String p = "%" + terme + "%";
            ps.setString(1, p); ps.setString(2, p); ps.setString(3, p);
            ResultSet rs = ps.executeQuery();
            return mapperListe(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche: " + e.getMessage(), e);
        }
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE email=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur vérification email: " + e.getMessage(), e);
        }
    }

    private List<Utilisateur> lister(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste utilisateurs: " + e.getMessage(), e);
        }
    }

    private List<Utilisateur> mapperListe(ResultSet rs) throws SQLException {
        List<Utilisateur> liste = new ArrayList<>();
        while (rs.next()) liste.add(mapper(rs));
        return liste;
    }

    private Utilisateur mapper(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setRole(Role.valueOf(rs.getString("role")));
        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) u.setDateCreation(ts.toLocalDateTime());
        u.setActif(rs.getBoolean("actif"));
        return u;
    }
}
