package com.example.dyeTrack.out.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.port.out.JwtServicePort;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService implements JwtServicePort {

    private final String secretKey;

    public JWTService(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Génère un token JWT contenant uniquement l'id de l'utilisateur.
     * 
     * @param userId l'identifiant unique de l'utilisateur
     * @return le JWT en String
     */
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7j
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long extractUserId(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            return Long.parseLong(claimsJws.getBody().getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }
}
