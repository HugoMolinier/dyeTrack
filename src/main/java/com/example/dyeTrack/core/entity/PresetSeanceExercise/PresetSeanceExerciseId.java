package com.example.dyeTrack.core.entity.PresetSeanceExercise;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PresetSeanceExerciseId implements Serializable {

    private Long presetSeanceId;
    private Long exerciseId;

    public PresetSeanceExerciseId() {
    }

    public PresetSeanceExerciseId(Long presetSeanceId, Long exerciseId) {
        this.presetSeanceId = presetSeanceId;
        this.exerciseId = exerciseId;
    }

    // Getters et setters
    public Long getPresetSeanceId() {
        return presetSeanceId;
    }

    public void setPresetSeanceId(Long presetSeanceId) {
        this.presetSeanceId = presetSeanceId;
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
        if (!(o instanceof PresetSeanceExerciseId))
            return false;
        PresetSeanceExerciseId that = (PresetSeanceExerciseId) o;
        return Objects.equals(presetSeanceId, that.presetSeanceId) &&
                Objects.equals(exerciseId, that.exerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(presetSeanceId, exerciseId);
    }
}
