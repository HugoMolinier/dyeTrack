package com.example.dyeTrack.in;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.in.utils.dtoUtil.ErrorResponseDTO;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* --- 400 Bad Request --- */
    @ExceptionHandler({ IllegalArgumentException.class })
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request.getRequestURI());
    }

    /* --- 403 Forbidden --- */
    @ExceptionHandler(ForbiddenException.class)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<ErrorResponseDTO> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage(), request.getRequestURI());
    }

    /* --- 404 Not Found --- */
    @ExceptionHandler(EntityNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<ErrorResponseDTO> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request.getRequestURI());
    }

    /* --- 500 Internal Server Error --- */
    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<ErrorResponseDTO> handleAll(Exception ex, HttpServletRequest request) {
        ex.printStackTrace(); // debug
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(),
                request.getRequestURI());
    }

    /* --- Helper pour construire la réponse d'erreur --- */
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String error, String message,
            String path) {
        ErrorResponseDTO body = new ErrorResponseDTO();
        body.setStatus(status.value());
        body.setError(error);
        body.setMessage(message);
        body.setPath(path);
        body.setTimestamp(Instant.now());
        return ResponseEntity.status(status).body(body);
    }
}
