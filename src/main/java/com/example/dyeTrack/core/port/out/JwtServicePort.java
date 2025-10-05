package com.example.dyeTrack.core.port.out;

public interface JwtServicePort {
    String generateToken(Long userId);
    Long extractUserId(String token);
}