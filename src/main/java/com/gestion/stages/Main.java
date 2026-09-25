package com.gestion.stages;

import com.gestion.stages.config.DatabaseConfig;
import com.gestion.stages.ui.MenuPrincipal;
import com.gestion.stages.util.ConsoleUtils;

public class Main {
    public static void main(String[] args) {
        // Test de la connexion à la base de données
        System.out.println(ConsoleUtils.BOLD + "Initialisation du système..." + ConsoleUtils.RESET);
        try {
            DatabaseConfig.testConnection();
        } catch (Exception e) {
            ConsoleUtils.afficherErreur("Impossible de se connecter à la base de données.");
            ConsoleUtils.afficherInfo("Vérifiez votre fichier config.properties et que MySQL est démarré.");
            System.out.println("Détail: " + e.getMessage());
            System.exit(1);
        }

        // Démarrage de l'application
        MenuPrincipal menuPrincipal = new MenuPrincipal();
        menuPrincipal.demarrer();
    }
}
