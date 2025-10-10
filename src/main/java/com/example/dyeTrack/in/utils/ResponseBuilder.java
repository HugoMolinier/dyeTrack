package com.example.dyeTrack.in.utils;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

public class ResponseBuilder {

    // DTO générique
    public static class ResponseDTO<T> {
        @Schema(description = "Message de retour", example = "Opération réussie")
        private String message;
        @Schema(description = "Données retournées")
        private T data;
        @Schema(description = "Horodatage de la réponse", example = "2025-10-10T12:34:56.789Z")
        private Instant timestamp = Instant.now();
        @Schema(description = "Code HTTP de la réponse", example = "200")
        private int status;

        public ResponseDTO(String message, T data, int status) {
            this.message = message;
            this.data = data;
            this.status = status;
        }

        // getters / setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static <T> ResponseEntity<ResponseDTO<T>> success(T data, String message) {
        return ResponseEntity.ok(new ResponseDTO<>(message, data, HttpStatus.OK.value()));
    }

    public static <T> ResponseEntity<ResponseDTO<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(message, data, HttpStatus.CREATED.value()));
    }
}
