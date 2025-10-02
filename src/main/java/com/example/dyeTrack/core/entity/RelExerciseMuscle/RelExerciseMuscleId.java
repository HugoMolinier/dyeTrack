package com.example.dyeTrack.core.entity.RelExerciseMuscle;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RelExerciseMuscleId implements Serializable {

    private Long muscleId;
    private Long exerciceId;

    public RelExerciseMuscleId() {}

    public RelExerciseMuscleId(Long muscleId, Long exerciceId) {
        this.muscleId = muscleId;
        this.exerciceId = exerciceId;
    }

    // getters et setters
    public Long getMuscleId() { return muscleId; }
    public void setMuscleId(Long muscleId) { this.muscleId = muscleId; }

    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }

    // hashCode et equals obligatoires
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelExerciseMuscleId)) return false;
        RelExerciseMuscleId that = (RelExerciseMuscleId) o;
        return Objects.equals(muscleId, that.muscleId) &&
               Objects.equals(exerciceId, that.exerciceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(muscleId, exerciceId);
    }
}
