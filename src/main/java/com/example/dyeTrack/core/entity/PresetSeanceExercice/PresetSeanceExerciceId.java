package com.example.dyeTrack.core.entity.PresetSeanceExercice;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PresetSeanceExerciceId implements Serializable {

    private Long presetSeanceId;
    private Long exerciceId;

    public PresetSeanceExerciceId() {
    }

    public PresetSeanceExerciceId(Long presetSeanceId, Long exerciceId) {
        this.presetSeanceId = presetSeanceId;
        this.exerciceId = exerciceId;
    }

    // getters et setters
    public Long getPresetSeanceId() {
        return presetSeanceId;
    }

    public void setMuscleId(Long presetSeanceId) {
        this.presetSeanceId = presetSeanceId;
    }

    public Long getExerciceId() {
        return exerciceId;
    }

    public void setExerciceId(Long exerciceId) {
        this.exerciceId = exerciceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PresetSeanceExerciceId))
            return false;
        PresetSeanceExerciceId that = (PresetSeanceExerciceId) o;
        return Objects.equals(presetSeanceId, that.presetSeanceId) &&
                Objects.equals(exerciceId, that.exerciceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(presetSeanceId, exerciceId);
    }
}
