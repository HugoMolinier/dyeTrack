package com.example.dyeTrack.core.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HashUtil {

    private static final String HMAC_ALGO = "HmacSHA256";

    public static String hashEmail(String email, String secretKey) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), HMAC_ALGO);
            mac.init(keySpec);
            byte[] hashed = mac.doFinal(email.toLowerCase().trim().getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash email", e);
        }
    }
}
