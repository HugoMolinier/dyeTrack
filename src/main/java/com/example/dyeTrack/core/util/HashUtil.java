package com.example.dyeTrack.core.util;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HashUtil {

    private static final String ALGO = "AES";

    public static String encryptEmail(String email, String base64SecretKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64SecretKey);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGO);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // ou un mode plus sûr
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(email.trim().getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt email", e);
        }
    }

    public static String decryptEmail(String encryptedEmail, String base64SecretKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64SecretKey);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGO);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(encryptedEmail);
            return new String(cipher.doFinal(decoded), "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt email", e);
        }
    }
}