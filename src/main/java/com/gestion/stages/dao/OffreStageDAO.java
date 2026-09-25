package com.gestion.stages.dao;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.model.OffreStage;
import com.gestion.stages.model.enums.StatutOffre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreStageDAO {

    private static final String SELECT_JOIN =
        "SELECT o.*, e.nom AS nom_entreprise FROM offres_stage o " +
        "LEFT JOIN entreprises e ON o.id_entreprise = e.id_entreprise ";

    public OffreStage creer(OffreStage o) {
        String sql = "INSERT INTO offres_stage (id_entreprise, titre, description, domaine, duree_mois, type, date_debut, date_fin, date_publication, date_limite, remuneration, competences_requises, niveau_requis, nombre_places, statut) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getIdEntreprise());
            ps.setString(2, o.getTitre());
            ps.setString(3, o.getDescription());
            ps.setString(4, o.getDomaine());
            ps.setInt(5, o.getDureeEnMois());
            ps.setString(6, o.getType());
            ps.setDate(7, o.getDateDebut() != null ? Date.valueOf(o.getDateDebut()) : null);
            ps.setDate(8, o.getDateFin() != null ? Date.valueOf(o.getDateFin()) : null);
            ps.setDate(9, Date.valueOf(o.getDatePublication()));
            ps.setDate(10, o.getDateLimite() != null ? Date.valueOf(o.getDateLimite()) : null);
            ps.setDouble(11, o.getRemuneration());
            ps.setString(12, o.getCompetencesRequises());
            ps.setString(13, o.getNiveauRequis());
            ps.setInt(14, o.getNombrePlaces());
            ps.setString(15, o.getStatut().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) o.setIdOffre(rs.getInt(1));
            return o;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création offre: " + e.getMessage(), e);
        }
    }

    public boolean modifier(OffreStage o) {
        String sql = "UPDATE offres_stage SET titre=?, description=?, domaine=?, duree_mois=?, type=?, date_debut=?, date_fin=?, date_limite=?, remuneration=?, competences_requises=?, niveau_requis=?, nombre_places=? WHERE id_offre=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getTitre()); ps.setString(2, o.getDescription());
            ps.setString(3, o.getDomaine()); ps.setInt(4, o.getDureeEnMois());
            ps.setString(5, o.getType());
            ps.setDate(6, o.getDateDebut() != null ? Date.valueOf(o.getDateDebut()) : null);
            ps.setDate(7, o.getDateFin() != null ? Date.valueOf(o.getDateFin()) : null);
            ps.setDate(8, o.getDateLimite() != null ? Date.valueOf(o.getDateLimite()) : null);
            ps.setDouble(9, o.getRemuneration());
            ps.setString(10, o.getCompetencesRequises());
            ps.setString(11, o.getNiveauRequis());
            ps.setInt(12, o.getNombrePlaces());
            ps.setInt(13, o.getIdOffre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur modification offre: " + e.getMessage(), e);
        }
    }

    public boolean changerStatut(int id, StatutOffre statut) {
        String sql = "UPDATE offres_stage SET statut=? WHERE id_offre=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name()); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur changement statut offre: " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM offres_stage WHERE id_offre=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression offre: " + e.getMessage(), e);
        }
    }

    public OffreStage trouverParId(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE o.id_offre=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche offre: " + e.getMessage(), e);
        }
        return null;
    }

    public List<OffreStage> listerTous() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "ORDER BY o.date_publication DESC")) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste offres: " + e.getMessage(), e);
        }
    }

    public List<OffreStage> listerValidees() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE o.statut='VALIDEE' ORDER BY o.date_publication DESC")) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste offres validées: " + e.getMessage(), e);
        }
    }

    public List<OffreStage> listerParEntreprise(int idEntreprise) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE o.id_entreprise=? ORDER BY o.date_publication DESC")) {
            ps.setInt(1, idEntreprise);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste offres par entreprise: " + e.getMessage(), e);
        }
    }

    public List<OffreStage> rechercherParDomaine(String domaine) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE o.domaine LIKE ? AND o.statut='VALIDEE' ORDER BY o.date_publication DESC")) {
            ps.setString(1, "%" + domaine + "%");
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche offres par domaine: " + e.getMessage(), e);
        }
    }

    public List<OffreStage> rechercherGlobal(String terme) {
        String sql = SELECT_JOIN + "WHERE (o.titre LIKE ? OR o.domaine LIKE ? OR o.competences_requises LIKE ? OR e.nom LIKE ?) AND o.statut='VALIDEE' ORDER BY o.date_publication DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String p = "%" + terme + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, p);
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche globale offres: " + e.getMessage(), e);
        }
    }

    public List<OffreStage> listerEnAttenteValidation() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_JOIN + "WHERE o.statut='EN_ATTENTE' ORDER BY o.date_publication")) {
            return mapperListe(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur liste offres en attente: " + e.getMessage(), e);
        }
    }

    private List<OffreStage> mapperListe(ResultSet rs) throws SQLException {
        List<OffreStage> liste = new ArrayList<>();
        while (rs.next()) liste.add(mapper(rs));
        return liste;
    }

    private OffreStage mapper(ResultSet rs) throws SQLException {
        OffreStage o = new OffreStage();
        o.setIdOffre(rs.getInt("id_offre"));
        o.setIdEntreprise(rs.getInt("id_entreprise"));
        try { o.setNomEntreprise(rs.getString("nom_entreprise")); } catch (SQLException ignored) {}
        o.setTitre(rs.getString("titre"));
        o.setDescription(rs.getString("description"));
        o.setDomaine(rs.getString("domaine"));
        o.setDureeEnMois(rs.getInt("duree_mois"));
        o.setType(rs.getString("type"));
        Date dd = rs.getDate("date_debut"); if (dd != null) o.setDateDebut(dd.toLocalDate());
        Date df = rs.getDate("date_fin");   if (df != null) o.setDateFin(df.toLocalDate());
        Date dp = rs.getDate("date_publication"); if (dp != null) o.setDatePublication(dp.toLocalDate());
        Date dl = rs.getDate("date_limite"); if (dl != null) o.setDateLimite(dl.toLocalDate());
        o.setRemuneration(rs.getDouble("remuneration"));
        o.setCompetencesRequises(rs.getString("competences_requises"));
        o.setNiveauRequis(rs.getString("niveau_requis"));
        o.setNombrePlaces(rs.getInt("nombre_places"));
        o.setStatut(StatutOffre.valueOf(rs.getString("statut")));
        return o;
    }
}
