package com.example.dyeTrack.core.entity.RelExerciseMuscle;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RelExerciseMuscleId implements Serializable {

    private Long muscleId;
    private Long exerciseId;

    public RelExerciseMuscleId() {
    }

    public RelExerciseMuscleId(Long muscleId, Long exerciseId) {
        this.muscleId = muscleId;
        this.exerciseId = exerciseId;
    }

    // getters et setters
    public Long getMuscleId() {
        return muscleId;
    }

    public void setMuscleId(Long muscleId) {
        this.muscleId = muscleId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RelExerciseMuscleId))
            return false;
        RelExerciseMuscleId that = (RelExerciseMuscleId) o;
        return Objects.equals(muscleId, that.muscleId) &&
                Objects.equals(exerciseId, that.exerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(muscleId, exerciseId);
    }
}
