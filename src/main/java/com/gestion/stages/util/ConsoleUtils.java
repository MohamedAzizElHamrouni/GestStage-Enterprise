package com.gestion.stages.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleUtils {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final String RESET  = "\u001B[0m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String CYAN   = "\u001B[36m";
    public static final String BOLD   = "\u001B[1m";

    public static void afficherTitre(String titre) {
        int len = titre.length() + 4;
        String ligne = "=".repeat(len);
        System.out.println(BOLD + BLUE + ligne + RESET);
        System.out.println(BOLD + BLUE + "| " + titre + " |" + RESET);
        System.out.println(BOLD + BLUE + ligne + RESET);
    }

    public static void afficherSousTitre(String titre) {
        System.out.println(BOLD + CYAN + "--- " + titre + " ---" + RESET);
    }

    public static void afficherSucces(String message) {
        System.out.println(GREEN + "[OK] " + message + RESET);
    }

    public static void afficherErreur(String message) {
        System.out.println(RED + "[ERREUR] " + message + RESET);
    }

    public static void afficherInfo(String message) {
        System.out.println(YELLOW + "[INFO] " + message + RESET);
    }

    public static void afficherSeparateur() {
        System.out.println("-".repeat(60));
    }

    public static String lireChaine(String invite) {
        System.out.print(invite);
        return scanner.nextLine().trim();
    }

    public static String lireChaineMandatoire(String invite) {
        String valeur;
        do {
            valeur = lireChaine(invite);
            if (valeur.isEmpty()) afficherErreur("Ce champ est obligatoire.");
        } while (valeur.isEmpty());
        return valeur;
    }

    public static int lireEntier(String invite) {
        while (true) {
            System.out.print(invite);
            String ligne = scanner.nextLine().trim();
            try {
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez saisir un nombre entier valide.");
            }
        }
    }

    public static int lireEntierEntre(String invite, int min, int max) {
        int valeur;
        do {
            valeur = lireEntier(invite);
            if (valeur < min || valeur > max) {
                afficherErreur("Valeur entre " + min + " et " + max + " requise.");
            }
        } while (valeur < min || valeur > max);
        return valeur;
    }

    public static double lireDouble(String invite) {
        while (true) {
            System.out.print(invite);
            String ligne = scanner.nextLine().trim();
            try {
                return Double.parseDouble(ligne.replace(",", "."));
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez saisir un nombre décimal valide.");
            }
        }
    }

    public static LocalDate lireDate(String invite) {
        while (true) {
            System.out.print(invite + " (jj/mm/aaaa) : ");
            String ligne = scanner.nextLine().trim();
            try {
                return LocalDate.parse(ligne, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                afficherErreur("Format de date invalide. Utilisez jj/mm/aaaa.");
            }
        }
    }

    public static boolean lireOuiNon(String invite) {
        while (true) {
            System.out.print(invite + " (o/n) : ");
            String reponse = scanner.nextLine().trim().toLowerCase();
            if (reponse.equals("o") || reponse.equals("oui")) return true;
            if (reponse.equals("n") || reponse.equals("non")) return false;
            afficherErreur("Répondez par 'o' (oui) ou 'n' (non).");
        }
    }

    public static String lireMotDePasse(String invite) {
        // En console pure, on ne peut pas masquer sans librairie externe
        System.out.print(invite);
        return scanner.nextLine().trim();
    }

    public static void attendreTouche() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    public static void viderEcran() {
        System.out.println("\n".repeat(3));
    }

    public static Scanner getScanner() {
        return scanner;
    }
}
