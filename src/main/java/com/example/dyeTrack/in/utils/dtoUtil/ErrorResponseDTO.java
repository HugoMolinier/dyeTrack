package com.example.dyeTrack.in.utils.dtoUtil;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponseDTO {
    @Schema(description = "Code HTTP de l'erreur", example = "404")
    private int status;

    @Schema(description = "Nom de l'erreur", example = "Not Found")
    private String error;

    @Schema(description = "Message détaillé", example = "Utilisateur introuvable")
    private String message;

    @Schema(description = "Chemin de la requête ayant provoqué l'erreur", example = "/api/user/123")
    private String path;

    @Schema(description = "Horodatage de l'erreur", example = "2025-10-10T12:34:56.789Z")
    private Instant timestamp = Instant.now();

    // getters / setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
