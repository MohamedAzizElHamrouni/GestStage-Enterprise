package com.gestion.stages.util;

import com.gestion.stages.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportUtils {

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // ===================== CSV =====================

    public static String exporterEtudiantsCSV(List<Etudiant> etudiants) {
        String nomFichier = "etudiants_" + LocalDateTime.now().format(DT) + ".csv";
        try (PrintWriter pw = new PrintWriter(nomFichier, "UTF-8")) {
            pw.println("Matricule,Nom,Prénom,Email,Téléphone,Spécialité,Niveau,Moyenne,Statut Stage,Score");
            for (Etudiant e : etudiants) {
                pw.printf("%s,%s,%s,%s,%s,%s,%s,%.2f,%s,%d%n",
                        e.getMatricule(), e.getNom(), e.getPrenom(), e.getEmail(),
                        e.getTelephone(), e.getSpecialite(),
                        e.getNiveau() != null ? e.getNiveau().getLibelle() : "",
                        e.getMoyenne(), e.getStatutStage().getLibelle(), e.calculerScore());
            }
            return nomFichier;
        } catch (IOException e) {
            throw new RuntimeException("Erreur export CSV: " + e.getMessage(), e);
        }
    }

    public static String exporterOffresCSV(List<OffreStage> offres) {
        String nomFichier = "offres_stage_" + LocalDateTime.now().format(DT) + ".csv";
        try (PrintWriter pw = new PrintWriter(nomFichier, "UTF-8")) {
            pw.println("ID,Titre,Entreprise,Domaine,Durée(mois),Type,Date Début,Date Fin,Rémunération,Places,Statut");
            for (OffreStage o : offres) {
                pw.printf("%d,%s,%s,%s,%d,%s,%s,%s,%.0f,%d,%s%n",
                        o.getIdOffre(), o.getTitre(), o.getNomEntreprise(), o.getDomaine(),
                        o.getDureeEnMois(), o.getType(), o.getDateDebut(), o.getDateFin(),
                        o.getRemuneration(), o.getNombrePlaces(), o.getStatut().getLibelle());
            }
            return nomFichier;
        } catch (IOException e) {
            throw new RuntimeException("Erreur export CSV: " + e.getMessage(), e);
        }
    }

    public static String exporterStagesCSV(List<Stage> stages) {
        String nomFichier = "stages_" + LocalDateTime.now().format(DT) + ".csv";
        try (PrintWriter pw = new PrintWriter(nomFichier, "UTF-8")) {
            pw.println("ID,Étudiant,Entreprise,Sujet,Date Début,Date Fin,Statut,Note Encadrant,Note Entreprise,Note Moyenne");
            for (Stage s : stages) {
                pw.printf("%d,%s,%s,%s,%s,%s,%s,%.1f,%.1f,%.1f%n",
                        s.getIdStage(), s.getNomEtudiant(), s.getNomEntreprise(),
                        s.getSujet(), s.getDateDebut(), s.getDateFin(),
                        s.getStatut().getLibelle(), s.getNoteEncadrant(),
                        s.getNoteEntreprise(), s.getNoteMoyenne());
            }
            return nomFichier;
        } catch (IOException e) {
            throw new RuntimeException("Erreur export CSV: " + e.getMessage(), e);
        }
    }

    // ===================== PDF =====================

    public static String exporterEtudiantsPDF(List<Etudiant> etudiants) {
        String nomFichier = "etudiants_" + LocalDateTime.now().format(DT) + ".pdf";
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();

            Font titreFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font enteteFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            Font cellFont   = new Font(Font.FontFamily.HELVETICA, 9);

            Paragraph titre = new Paragraph("Liste des Étudiants", titreFont);
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setSpacingAfter(15);
            document.add(titre);

            document.add(new Paragraph("Exporté le : " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), cellFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2f, 2f, 3f, 2f, 1.5f, 1.5f});

            String[] entetes = {"Matricule", "Nom", "Prénom", "Spécialité", "Niveau", "Moyenne", "Statut"};
            for (String entete : entetes) {
                PdfPCell cell = new PdfPCell(new Phrase(entete, enteteFont));
                cell.setBackgroundColor(new BaseColor(41, 128, 185));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            for (Etudiant e : etudiants) {
                table.addCell(new Phrase(e.getMatricule(), cellFont));
                table.addCell(new Phrase(e.getNom(), cellFont));
                table.addCell(new Phrase(e.getPrenom(), cellFont));
                table.addCell(new Phrase(e.getSpecialite(), cellFont));
                table.addCell(new Phrase(e.getNiveau() != null ? e.getNiveau().getLibelle() : "", cellFont));
                table.addCell(new Phrase(String.format("%.2f", e.getMoyenne()), cellFont));
                table.addCell(new Phrase(e.getStatutStage().getLibelle(), cellFont));
            }

            document.add(table);
            document.close();
            return nomFichier;
        } catch (Exception e) {
            throw new RuntimeException("Erreur export PDF: " + e.getMessage(), e);
        }
    }

    public static String exporterAttestationStage(Stage stage) {
        String nomFichier = "attestation_stage_" + stage.getIdStage() + "_" + LocalDateTime.now().format(DT) + ".pdf";
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();

            Font titreFont      = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Font sousTitreFont  = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font texteFont      = new Font(Font.FontFamily.HELVETICA, 12);
            Font petitFont      = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

            document.add(Chunk.NEWLINE);

            Paragraph univ = new Paragraph("UNIVERSITÉ - DÉPARTEMENT INFORMATIQUE", sousTitreFont);
            univ.setAlignment(Element.ALIGN_CENTER);
            document.add(univ);
            document.add(Chunk.NEWLINE);

            Paragraph att = new Paragraph("ATTESTATION DE STAGE", titreFont);
            att.setAlignment(Element.ALIGN_CENTER);
            document.add(att);
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("-".repeat(60), texteFont));
            document.add(Chunk.NEWLINE);

            String corps = "Nous soussignés certifions que :\n\n"
                    + "M./Mme " + stage.getNomEtudiant() + "\n\n"
                    + "a effectué un stage au sein de l'entreprise " + stage.getNomEntreprise() + "\n"
                    + "du " + stage.getDateDebut() + " au " + stage.getDateFin() + "\n\n"
                    + "Sujet du stage : " + stage.getSujet() + "\n\n"
                    + "Note obtenue : " + String.format("%.1f/20", stage.getNoteMoyenne()) + "\n\n"
                    + "Cette attestation est délivrée pour servir et valoir ce que de droit.";

            document.add(new Paragraph(corps, texteFont));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Paragraph date = new Paragraph("Fait le : " + java.time.LocalDate.now(), petitFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);

            document.close();
            return nomFichier;
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération attestation: " + e.getMessage(), e);
        }
    }

    public static String exporterCVEtudiant(Etudiant etudiant) {
        String nomFichier = "cv_" + etudiant.getMatricule() + "_" + LocalDateTime.now().format(DT) + ".pdf";
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();

            Font nomFont       = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
            Font sectionFont   = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
            Font texteFont     = new Font(Font.FontFamily.HELVETICA, 11);
            Font italFont      = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC);

            Paragraph nomP = new Paragraph(etudiant.getNomComplet(), nomFont);
            nomP.setAlignment(Element.ALIGN_CENTER);
            document.add(nomP);

            Paragraph infoP = new Paragraph(etudiant.getEmail() + " | " + etudiant.getTelephone(), italFont);
            infoP.setAlignment(Element.ALIGN_CENTER);
            document.add(infoP);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("FORMATION", sectionFont));
            document.add(new Paragraph("Spécialité : " + etudiant.getSpecialite(), texteFont));
            document.add(new Paragraph("Niveau : " + (etudiant.getNiveau() != null ? etudiant.getNiveau().getLibelle() : "N/A"), texteFont));
            document.add(new Paragraph("Moyenne générale : " + String.format("%.2f/20", etudiant.getMoyenne()), texteFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("COMPÉTENCES", sectionFont));
            if (etudiant.getCompetences() != null && !etudiant.getCompetences().isEmpty()) {
                for (String comp : etudiant.getCompetences().split(",")) {
                    document.add(new Paragraph("  - " + comp.trim(), texteFont));
                }
            }
            document.add(Chunk.NEWLINE);

            if (etudiant.getCv() != null && !etudiant.getCv().isEmpty()) {
                document.add(new Paragraph("EXPÉRIENCES / INFORMATIONS COMPLÉMENTAIRES", sectionFont));
                document.add(new Paragraph(etudiant.getCv(), texteFont));
            }

            document.close();
            return nomFichier;
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération CV: " + e.getMessage(), e);
        }
    }
}
