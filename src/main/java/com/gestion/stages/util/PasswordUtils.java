package com.gestion.stages.util;

import java.security.SecureRandom;

public class PasswordUtils {

    /** Génère un mot de passe temporaire aléatoire. */
    public static String genererMotDePasseTemporaire() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#!";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        sb.append((char) ('A' + random.nextInt(26)));
        sb.append((char) ('0' + random.nextInt(10)));
        for (int i = 2; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
