package com.example.dyeTrack.in;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dyeTrack.core.exception.ExerciseCreationException;
import com.example.dyeTrack.core.exception.ForbiddenException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExerciseCreationException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(ExerciseCreationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(ForbiddenException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAll(Exception ex) {
        ex.printStackTrace();
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error" + ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
