package com.example.dyeTrack.core.entity.infoExerciceUser;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InfoExerciceUserId implements Serializable {

    @Column(name = "id_user")
    private Long userId;

    @Column(name = "exercice_id")
    private Long idExercise;

    public InfoExerciceUserId() {
    }

    public InfoExerciceUserId(Long userId, Long idExercise) {
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

    public Long getExerciceId() {
        return idExercise;
    }

    public void setExerciceId(Long exerciceId) {
        this.idExercise = exerciceId;
    }

    // --- equals & hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof InfoExerciceUserId))
            return false;
        InfoExerciceUserId that = (InfoExerciceUserId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(idExercise, that.idExercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, idExercise);
    }
}
