package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.Candidature;
import com.gestion.stages.model.enums.StatutCandidature;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatureDAO {

    private static final String SELECT_JOIN =
        "SELECT c.*, " +
        "CONCAT(et.prenom, ' ', et.nom) AS nom_etudiant, " +
        "o.titre AS titre_offre, " +
        "en.nom AS nom_entreprise " +
        "FROM candidatures c " +
        "LEFT JOIN etudiants et ON c.id_etudiant = et.id_etudiant " +
        "LEFT JOIN offres_stage o ON c.id_offre = o.id_offre " +
        "LEFT JOIN entreprises en ON o.id_entreprise = en.id_entreprise ";

    public Candidature creer(Candidature c) {
        String sql = "INSERT INTO candidatures (id_etudiant, id_offre, date_candidature, statut, lettre_motivation) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getIdEtudiant());
            ps.setInt(2, c.getIdOffre());
            ps.setDate(3, Date.valueOf(c.getDateCandidature()));
            ps.setString(4, c.getStatut().name());
            ps.setString(5, c.getLettreMotivation());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) c.setIdCandidature(rs.getInt(1));
            return c;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création candidature: " + e.getMessage(), e);
        }
    }

    public boolean changerStatut(int id, StatutCandidature statut, String commentaire) {
        String sql = "UPDATE candidatures SET statut=?, commentaire_entreprise=? WHERE id_candidature=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            ps.setString(2, commentaire);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur changement statut candidature: " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM candidatures WHERE id_candidature=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression candidature: " + e.getMessage(), e);
        }
    }

    public Candidature trouverParId(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE c.id_candidature=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche candidature: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Candidature> listerParEtudiant(int idEtudiant) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE c.id_etudiant=? ORDER BY c.date_candidature DESC")) {
            ps.setInt(1, idEtudiant);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste candidatures étudiant: " + e.getMessage(), e);
        }
    }

    public List<Candidature> listerParOffre(int idOffre) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE c.id_offre=? ORDER BY c.date_candidature")) {
            ps.setInt(1, idOffre);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste candidatures par offre: " + e.getMessage(), e);
        }
    }

    public List<Candidature> listerTous() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "ORDER BY c.date_candidature DESC")) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste candidatures: " + e.getMessage(), e);
        }
    }

    public boolean candidatureExiste(int idEtudiant, int idOffre) {
        String sql = "SELECT COUNT(*) FROM candidatures WHERE id_etudiant=? AND id_offre=? AND statut != 'ANNULEE'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEtudiant); ps.setInt(2, idOffre);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur vérification candidature: " + e.getMessage(), e);
        }
    }

    private List<Candidature> mapperListe(ResultSet rs) throws SQLException {
        List<Candidature> liste = new ArrayList<>();
        while (rs.next()) liste.add(mapper(rs));
        return liste;
    }

    private Candidature mapper(ResultSet rs) throws SQLException {
        Candidature c = new Candidature();
        c.setIdCandidature(rs.getInt("id_candidature"));
        c.setIdEtudiant(rs.getInt("id_etudiant"));
        c.setIdOffre(rs.getInt("id_offre"));
        try { c.setNomEtudiant(rs.getString("nom_etudiant")); } catch (SQLException ignored) {}
        try { c.setTitreOffre(rs.getString("titre_offre")); } catch (SQLException ignored) {}
        try { c.setNomEntreprise(rs.getString("nom_entreprise")); } catch (SQLException ignored) {}
        Date d = rs.getDate("date_candidature");
        if (d != null) c.setDateCandidature(d.toLocalDate());
        c.setStatut(StatutCandidature.valueOf(rs.getString("statut")));
        c.setLettreMotivation(rs.getString("lettre_motivation"));
        c.setCommentaireEntreprise(rs.getString("commentaire_entreprise"));
        return c;
    }
}
