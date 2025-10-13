package com.example.dyeTrack.core.entity.infoExerciseUser;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InfoExerciseUserId implements Serializable {

    @Column(name = "id_user")
    private Long userId;

    @Column(name = "exercise_id")
    private Long idExercise;

    public InfoExerciseUserId() {
    }

    public InfoExerciseUserId(Long userId, Long idExercise) {
        this.userId = userId;
        this.idExercise = idExercise;
    }

    // --- Getters & Setters ---
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExerciseId() {
        return idExercise;
    }

    public void setExerciseId(Long exerciseId) {
        this.idExercise = exerciseId;
    }

    // --- equals & hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof InfoExerciseUserId))
            return false;
        InfoExerciseUserId that = (InfoExerciseUserId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(idExercise, that.idExercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, idExercise);
    }
}
