package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.Entreprise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntrepriseDAO {

    public Entreprise creer(Entreprise en) {
        String sql = "INSERT INTO entreprises (id_utilisateur, nom, domaine, adresse, telephone, email, description, responsable_rh, valide, note) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, en.getIdUtilisateur());
            ps.setString(2, en.getNom());
            ps.setString(3, en.getDomaine());
            ps.setString(4, en.getAdresse());
            ps.setString(5, en.getTelephone());
            ps.setString(6, en.getEmail());
            ps.setString(7, en.getDescription());
            ps.setString(8, en.getResponsableRH());
            ps.setBoolean(9, en.isValide());
            ps.setDouble(10, en.getNote());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) en.setIdEntreprise(rs.getInt(1));
            return en;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création entreprise: " + e.getMessage(), e);
        }
    }

    public boolean modifier(Entreprise en) {
        String sql = "UPDATE entreprises SET nom=?, domaine=?, adresse=?, telephone=?, email=?, description=?, responsable_rh=? WHERE id_entreprise=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, en.getNom()); ps.setString(2, en.getDomaine());
            ps.setString(3, en.getAdresse()); ps.setString(4, en.getTelephone());
            ps.setString(5, en.getEmail()); ps.setString(6, en.getDescription());
            ps.setString(7, en.getResponsableRH()); ps.setInt(8, en.getIdEntreprise());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur modification entreprise: " + e.getMessage(), e);
        }
    }

    public boolean valider(int id, boolean valide) {
        String sql = "UPDATE entreprises SET valide=? WHERE id_entreprise=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, valide); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur validation entreprise: " + e.getMessage(), e);
        }
    }

    public boolean noterEntreprise(int id, double note) {
        String sql = "UPDATE entreprises SET note=? WHERE id_entreprise=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, note); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur notation entreprise: " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM entreprises WHERE id_entreprise=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression entreprise: " + e.getMessage(), e);
        }
    }

    public Entreprise trouverParId(int id) {
        String sql = "SELECT * FROM entreprises WHERE id_entreprise=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche entreprise: " + e.getMessage(), e);
        }
        return null;
    }

    public Entreprise trouverParUtilisateur(int idUtilisateur) {
        String sql = "SELECT * FROM entreprises WHERE id_utilisateur=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche entreprise par user: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Entreprise> listerTous() {
        String sql = "SELECT * FROM entreprises ORDER BY nom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste entreprises: " + e.getMessage(), e);
        }
    }

    public List<Entreprise> listerValidees() {
        String sql = "SELECT * FROM entreprises WHERE valide=true ORDER BY nom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste entreprises validées: " + e.getMessage(), e);
        }
    }

    public List<Entreprise> listerEnAttenteValidation() {
        String sql = "SELECT * FROM entreprises WHERE valide=false ORDER BY nom";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste entreprises en attente: " + e.getMessage(), e);
        }
    }

    public List<Entreprise> rechercherParDomaine(String domaine) {
        String sql = "SELECT * FROM entreprises WHERE domaine LIKE ? AND valide=true ORDER BY note DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + domaine + "%");
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche par domaine: " + e.getMessage(), e);
        }
    }

    public List<Entreprise> classementParNote() {
        String sql = "SELECT * FROM entreprises WHERE valide=true ORDER BY note DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur classement entreprises: " + e.getMessage(), e);
        }
    }

    private List<Entreprise> mapperListe(ResultSet rs) throws SQLException {
        List<Entreprise> liste = new ArrayList<>();
        while (rs.next()) liste.add(mapper(rs));
        return liste;
    }

    private Entreprise mapper(ResultSet rs) throws SQLException {
        Entreprise en = new Entreprise();
        en.setIdEntreprise(rs.getInt("id_entreprise"));
        en.setIdUtilisateur(rs.getInt("id_utilisateur"));
        en.setNom(rs.getString("nom"));
        en.setDomaine(rs.getString("domaine"));
        en.setAdresse(rs.getString("adresse"));
        en.setTelephone(rs.getString("telephone"));
        en.setEmail(rs.getString("email"));
        en.setDescription(rs.getString("description"));
        en.setResponsableRH(rs.getString("responsable_rh"));
        en.setValide(rs.getBoolean("valide"));
        en.setNote(rs.getDouble("note"));
        return en;
    }
}
