package com.gestion.stages.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    // =========================
    // CONFIG FILE
    // =========================
    private static final String CONFIG_FILE = "config.properties";

    // =========================
    // DATABASE VARIABLES
    // =========================
    private static String url;
    private static String username;
    private static String password;

    // Load config when class is loaded
    static {
        loadConfig();
    }

    // =========================
    // LOAD CONFIGURATION
    // =========================
    private static void loadConfig() {

        Properties props = new Properties();

        try (InputStream in = new FileInputStream(CONFIG_FILE)) {

            props.load(in);

            url = props.getProperty(
                    "db.url",
                    "jdbc:mysql://localhost:3306/gestion_stages?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            );

            username = props.getProperty("db.username", "root");
            password = props.getProperty("db.password", "root");

            // DEBUG (remove in production)
            System.out.println("=== DB CONFIG LOADED ===");
            System.out.println("URL      = " + url);
            System.out.println("USERNAME = " + username);
            System.out.println("PASSWORD = " + password);
            System.out.println("========================");

        } catch (IOException e) {

            System.err.println("⚠ Config file not found, using fallback values.");

            url = "jdbc:mysql://localhost:3306/gestion_stages?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            username = "root";
            password = "root";

            System.out.println("=== FALLBACK CONFIG USED ===");
        }
    }

    // =========================
    // GET DATABASE CONNECTION
    // =========================
    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found: " + e.getMessage());
        }

        return DriverManager.getConnection(url, username, password);
    }

    // =========================
    // TEST CONNECTION
    // =========================
    public static void testConnection() {

        System.out.println("Testing database connection...");

        try (Connection conn = getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successful!");
                System.out.println("Database: " + conn.getCatalog());
            }

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    // =========================
    // GETTERS
    // =========================
    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }
}